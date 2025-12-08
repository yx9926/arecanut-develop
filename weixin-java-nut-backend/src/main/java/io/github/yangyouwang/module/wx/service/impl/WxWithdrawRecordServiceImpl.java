package io.github.yangyouwang.module.wx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.yangyouwang.module.wx.entity.WxWithdrawRecord;
import io.github.yangyouwang.module.wx.mapper.WxWithdrawRecordMapper;
import io.github.yangyouwang.module.wx.service.WxWithdrawRecordService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 微信用户提现记录Service实现类
 *
 * @author yangyouwang
 */
@Service
@AllArgsConstructor
@Slf4j
public class WxWithdrawRecordServiceImpl extends ServiceImpl<WxWithdrawRecordMapper, WxWithdrawRecord> implements WxWithdrawRecordService {

    private final WxWithdrawRecordMapper wxWithdrawRecordMapper;

    /**
     * 创建提现记录
     * @param record 提现记录
     * @param appid 小程序appid
     * @return 是否成功
     */
    @Override
    public boolean createWithdrawRecord(WxWithdrawRecord record, String appid) {
        if (record == null || appid == null) {
            return false;
        }

        // 设置默认值和appid
        record.setAppid(appid);
        record.setApplyTime(new Date());
        record.setStatus(0); // 0-申请中

        try {
            return save(record);
        } catch (Exception e) {
            log.error("创建提现记录失败: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 更新提现记录状态
     * @param id 记录ID
     * @param status 状态
     * @param remark 备注
     * @return 是否成功
     */
    @Override
    public boolean updateWithdrawStatus(Long id, Integer status, String remark) {
        if (id == null || status == null) {
            return false;
        }

        WxWithdrawRecord record = getById(id);
        if (record == null) {
            return false;
        }

        record.setStatus(status);
        record.setProcessTime(new Date());
        record.setRemark(remark);

        try {
            return updateById(record);
        } catch (Exception e) {
            log.error("更新提现记录状态失败: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 根据用户openid查询提现记录
     * @param openid 用户openid
     * @return 提现记录列表
     */
    @Override
    public List<WxWithdrawRecord> getByOpenid(String openid) {
        if (openid == null) {
            return null;
        }

        try {
            return lambdaQuery()
                    .eq(WxWithdrawRecord::getOpenid, openid)
                    .orderByDesc(WxWithdrawRecord::getApplyTime)
                    .list();
        } catch (Exception e) {
            log.error("根据openid查询提现记录失败: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 根据ID查询提现记录
     * @param id 记录ID
     * @return 提现记录
     */
    @Override
    public WxWithdrawRecord getById(Long id) {
        if (id == null) {
            return null;
        }

        try {
            return super.getById(id);
        } catch (Exception e) {
            log.error("根据ID查询提现记录失败: {}", e.getMessage(), e);
            return null;
        }
    }
}