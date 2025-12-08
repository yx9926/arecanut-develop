package io.github.yangyouwang.module.wx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.yangyouwang.module.wx.entity.WxWithdrawRecord;

import java.util.List;

/**
 * 微信用户提现记录Service接口
 *
 * @author yangyouwang
 */
public interface WxWithdrawRecordService extends IService<WxWithdrawRecord> {

    /**
     * 创建提现记录
     * @param record 提现记录
     * @param appid 小程序appid
     * @return 是否成功
     */
    boolean createWithdrawRecord(WxWithdrawRecord record, String appid);

    /**
     * 更新提现记录状态
     * @param id 记录ID
     * @param status 状态
     * @param remark 备注
     * @return 是否成功
     */
    boolean updateWithdrawStatus(Long id, Integer status, String remark);

    /**
     * 根据用户openid查询提现记录
     * @param openid 用户openid
     * @return 提现记录列表
     */
    List<WxWithdrawRecord> getByOpenid(String openid);

    /**
     * 根据ID查询提现记录
     * @param id 记录ID
     * @return 提现记录
     */
    WxWithdrawRecord getById(Long id);

}