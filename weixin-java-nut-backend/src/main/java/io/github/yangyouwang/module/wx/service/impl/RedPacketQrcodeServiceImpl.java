package io.github.yangyouwang.module.wx.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.binarywang.wxpay.bean.entpay.EntPayRequest;
import com.github.binarywang.wxpay.bean.entpay.EntPayResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import io.github.yangyouwang.module.wx.entity.RedPacketBatch;
import io.github.yangyouwang.module.wx.entity.RedPacketQrcode;
import io.github.yangyouwang.module.wx.mapper.RedPacketBatchMapper;
import io.github.yangyouwang.module.wx.mapper.RedPacketQrcodeMapper;
import io.github.yangyouwang.module.wx.service.RedPacketQrcodeService;
import io.github.yangyouwang.framework.config.properties.WeChatProperties;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 红包二维码Service实现类
 * @author yangyouwang
 * @date 2023-xx-xx
 */
@Service
@AllArgsConstructor
@Slf4j
public class RedPacketQrcodeServiceImpl extends ServiceImpl<RedPacketQrcodeMapper, RedPacketQrcode> implements RedPacketQrcodeService {

    private final RedPacketQrcodeMapper redPacketQrcodeMapper;
    private final RedPacketBatchMapper redPacketBatchMapper;
    private final WxPayService wxPayService;
    private final WeChatProperties weChatProperties;
    @Autowired
    private WxMaService wxMaService;

    @Override
    public RedPacketQrcode getByQrcodeId(String qrcodeId) {
        return redPacketQrcodeMapper.selectByQrcodeId(qrcodeId);
    }

    @Override
    public RedPacketQrcode getById(Long id) {
        return redPacketQrcodeMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> generateQrcodes(String batchId, Double totalAmount, Integer count) {
        // 检查批次是否已存在
//        RedPacketBatch batch = redPacketBatchMapper.selectByBatchId(batchId);
//        if (batch != null) {
//            throw new RuntimeException("批次ID已存在");
//        }

        // 创建批次信息
        RedPacketBatch newBatch = new RedPacketBatch();
        newBatch.setBatchId(batchId);
        newBatch.setTotalAmount(BigDecimal.valueOf(totalAmount));
        newBatch.setTotalCount(count);
        newBatch.setStatus(0); // 未生成
        redPacketBatchMapper.insert(newBatch);

        // 生成随机金额的红包
        List<Double> amounts = generateRandomAmounts(totalAmount, count);
        List<String> qrcodeIds = new ArrayList<>();
        String pagePath = "/pages/redPacket/redPacket";

        for (Double amount : amounts) {
            String qrcodeId = IdUtil.fastSimpleUUID();
            qrcodeIds.add(qrcodeId);

            // 创建红包二维码记录
            RedPacketQrcode qrcode = new RedPacketQrcode();
            qrcode.setBatchId(batchId);
            qrcode.setQrcodeId(qrcodeId);
            qrcode.setAmount(BigDecimal.valueOf(amount));
            qrcode.setStatus(0); // 未使用
            qrcode.setWithdrawStatus(0); // 未提现
            qrcode.setTotalAmount(BigDecimal.valueOf(totalAmount));
            qrcode.setTotalCount(count);
            redPacketQrcodeMapper.insert(qrcode);
            
            // 使用数据库ID作为hid参数
            Long hid = qrcode.getId();
            
            // 构建小程序码字符串（不生成实际图片）
            String miniProgramUrl = pagePath + "?hid=" + hid;
            String qrcodeString = generateMiniProgramCodeString(hid);

            log.info("小程序码字符串生成成功: {}", qrcodeString);

            // 更新二维码记录，存储小程序码字符串而非文件路径
            qrcode.setMiniProgramUrl(miniProgramUrl);
            qrcode.setQrcodePath(qrcodeString); // 存储小程序码字符串
            redPacketQrcodeMapper.updateById(qrcode);
        }

        // 更新批次状态为已生成
        newBatch.setStatus(1);
        redPacketBatchMapper.updateById(newBatch);

        return qrcodeIds;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean useQrcode(String qrcodeId, String userId) {
        RedPacketQrcode qrcode = redPacketQrcodeMapper.selectByQrcodeId(qrcodeId);
        if (qrcode == null) {
            return false;
        }

        if (qrcode.getStatus() != 0) {
            return false; // 已使用或已过期
        }

        // 更新二维码状态
        qrcode.setStatus(1); // 已使用
        qrcode.setUsedTime(new Date());
        qrcode.setUserId(userId);
        redPacketQrcodeMapper.updateById(qrcode);

        // 更新批次已使用数量
        redPacketBatchMapper.incrementUsedCount(qrcode.getBatchId());

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean withdraw(String qrcodeId, String userId) {
        RedPacketQrcode qrcode = redPacketQrcodeMapper.selectByQrcodeId(qrcodeId);
        if (qrcode == null) {
            return false;
        }

        if (!userId.equals(qrcode.getUserId())) {
            return false; // 不是同一用户
        }

        if (qrcode.getWithdrawStatus() != 0) {
            return false; // 已提现或提现中
        }

        // 调用企业付款接口
        EntPayRequest request = new EntPayRequest();
        request.setPartnerTradeNo("ORDER" + System.currentTimeMillis());
        request.setOpenid(userId); // 假设userId是用户的openid
        request.setCheckName("NO_CHECK"); // 不校验真实姓名
        request.setAmount(qrcode.getAmount().multiply(new BigDecimal(100)).intValue()); // 单位为分
        request.setDescription("红包提现");
        request.setSpbillCreateIp("127.0.0.1"); // 添加必要的IP参数

        EntPayResult result;
        try {
            result = wxPayService.getEntPayService().entPay(request);
        } catch (WxPayException e) {
            log.error("企业付款失败: {}", e.getMessage(), e);
            qrcode.setWithdrawStatus(3); // 提现失败
            redPacketQrcodeMapper.updateById(qrcode);
            return false;
        }

        // 更新提现状态
        qrcode.setWithdrawStatus(2); // 提现成功
        qrcode.setWithdrawTime(new Date());
        qrcode.setOrderNo(request.getPartnerTradeNo());
        redPacketQrcodeMapper.updateById(qrcode);

        // 更新批次已提现数量
        redPacketBatchMapper.incrementWithdrawnCount(qrcode.getBatchId());

        return true;
    }

    /**
     * 生成小程序码字符串（不生成实际图片）
     * @param hid 红包ID
     * @return 小程序码参数字符串
     */
    private String generateMiniProgramCodeString(Long hid) {
        // 构建小程序码参数字符串
        // 格式: hid=123456&timestamp=1623456789&random=abcd
        StringBuilder paramsBuilder = new StringBuilder();
        paramsBuilder.append("hid=").append(hid);
        paramsBuilder.append("&timestamp=").append(System.currentTimeMillis());
        paramsBuilder.append("&random=").append(IdUtil.fastSimpleUUID().substring(0, 8));
        
        return paramsBuilder.toString();
    }

    /**
     * 生成小程序码（已弃用，改为存储字符串）
     * @param appid 小程序appid
     * @param pagePath 页面路径
     * @param params 参数
     * @param width 宽度
     * @return 小程序码文件
     */
    @Deprecated
    private File createMiniProgramCode(String appid, String pagePath, Map<String, String> params, Integer width) {
        try {
            // 切换小程序配置
            wxMaService.switchover(appid);

            // 构建场景值
            StringBuilder sceneBuilder = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sceneBuilder.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
            }
            // 移除最后一个 '&'
            if (sceneBuilder.length() > 0) {
                sceneBuilder.deleteCharAt(sceneBuilder.length() - 1);
            }
            String scene = sceneBuilder.toString();

            // 处理页面路径，移除开头的 '/'（如果有）
            String page = pagePath.startsWith("/") ? pagePath.substring(1) : pagePath;

            // 生成小程序码
            File qrcodeFile = wxMaService.getQrcodeService().createQrcode(scene, width);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Files.copy(qrcodeFile.toPath(), outputStream);

            // 保存为临时文件
            String tempDir = System.getProperty("java.io.tmpdir");
            String fileName = "mini_program_code_" + IdUtil.fastSimpleUUID() + ".png";
            File file = Paths.get(tempDir, fileName).toFile();
            Files.write(file.toPath(), outputStream.toByteArray());

            return file;
        } catch (Exception e) {
            log.error("生成小程序码失败: {}", e.getMessage(), e);
            throw new RuntimeException("小程序码生成失败");
        }
    }

    /**
     * 生成随机金额列表 - 使用更合理的随机分配策略
     * @param totalAmount 总金额
     * @param count 数量
     * @return 随机金额列表
     */
    private List<Double> generateRandomAmounts(double totalAmount, int count) {
        List<Double> amounts = new ArrayList<>();
        Random random = new Random();

        // 最小金额限制
        double minAmount = 0.10; // 最小0.1元
        double maxAmount = totalAmount / count * 3; // 最大不超过平均值的3倍
        
        // 确保总金额足够分配
        if (totalAmount < minAmount * count) {
            minAmount = totalAmount / count * 0.8; // 调整最小金额
        }

        // 使用正态分布思想，先生成相对均匀的随机值
        double[] rawAmounts = new double[count];
        double sum = 0;
        
        for (int i = 0; i < count; i++) {
            // 生成基础随机值（0.5-1.5之间）
            double base = 0.5 + random.nextDouble();
            rawAmounts[i] = base;
            sum += base;
        }
        
        // 按比例分配金额，并添加随机波动
        double allocated = 0;
        for (int i = 0; i < count - 1; i++) {
            // 按比例分配并添加小幅度随机波动
            double ratio = rawAmounts[i] / sum;
            double amount = totalAmount * ratio;
            
            // 限制在合理范围内
            amount = Math.max(minAmount, Math.min(amount, maxAmount));
            amount = Math.round(amount * 100) / 100.0;
            
            amounts.add(amount);
            allocated += amount;
        }
        
        // 最后一个红包分配剩余金额
        double lastAmount = Math.round((totalAmount - allocated) * 100) / 100.0;
        
        // 确保最后一个红包也在合理范围内
        if (lastAmount < minAmount && count > 1) {
            // 如果最后一个太小，从前面调整
            double deficit = minAmount - lastAmount;
            for (int i = 0; i < amounts.size() && deficit > 0.01; i++) {
                double canReduce = Math.min(amounts.get(i) - minAmount, deficit);
                if (canReduce > 0.01) {
                    amounts.set(i, amounts.get(i) - canReduce);
                    lastAmount += canReduce;
                    deficit -= canReduce;
                }
            }
            lastAmount = Math.round(lastAmount * 100) / 100.0;
        }
        
        amounts.add(lastAmount);
        
        // 打乱顺序，避免金额排序可预测
        Collections.shuffle(amounts);
        
        return amounts;
    }
}