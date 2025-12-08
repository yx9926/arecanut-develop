package io.github.yangyouwang.module.wx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.yangyouwang.module.wx.entity.WxMaUser;

/**
 * 微信小程序用户Service接口
 *
 * @author yangyouwang
 */
public interface WxMaUserService extends IService<WxMaUser> {

    /**
     * 根据openid查询用户
     * @param openid 用户唯一标识
     * @return 微信小程序用户
     */
    WxMaUser getByOpenid(String openid);

    /**
     * 微信登录
     * @param appid 小程序appid
     * @param code 登录code
     * @return 微信小程序用户
     */
    WxMaUser wxLogin(String appid, String code);

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 更新结果
     */
    boolean updateUserInfo(WxMaUser user);

    /**
     * 绑定手机号
     * @param openid 用户唯一标识
     * @param phoneNumber 手机号
     * @return 绑定结果
     */
    boolean bindPhone(String openid, String phoneNumber);
}