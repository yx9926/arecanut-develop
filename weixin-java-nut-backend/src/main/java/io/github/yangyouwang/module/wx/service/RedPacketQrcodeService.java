package io.github.yangyouwang.module.wx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.yangyouwang.module.wx.entity.RedPacketQrcode;
import java.util.List;

/**
 * 红包二维码Service接口
 * @author yangyouwang
 * @date 2023-xx-xx
 */
public interface RedPacketQrcodeService extends IService<RedPacketQrcode> {

    /**
     * 根据二维码ID查询红包信息
     * @param qrcodeId 二维码ID
     * @return 红包信息
     */
    RedPacketQrcode getByQrcodeId(String qrcodeId);

    /**
     * 根据ID查询红包信息
     * @param id 红包ID
     * @return 红包信息
     */
    RedPacketQrcode getById(Long id);

    /**
     * 生成二维码红包
     * @param batchId 批次ID
     * @param totalAmount 总金额
     * @param count 数量
     * @return 生成的二维码列表
     */
    List<String> generateQrcodes(String batchId, Double totalAmount, Integer count);

    /**
     * 使用二维码
     * @param qrcodeId 二维码ID
     * @param userId 用户ID
     * @return 是否使用成功
     */
    boolean useQrcode(String qrcodeId, String userId);

    /**
     * 提现处理
     * @param qrcodeId 二维码ID
     * @param userId 用户ID
     * @return 提现结果
     */
    boolean withdraw(String qrcodeId, String userId);
}