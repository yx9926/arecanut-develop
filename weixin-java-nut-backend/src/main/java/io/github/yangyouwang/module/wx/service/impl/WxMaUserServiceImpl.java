package io.github.yangyouwang.module.wx.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.yangyouwang.module.wx.entity.WxMaUser;
import io.github.yangyouwang.module.wx.mapper.WxMaUserMapper;
import io.github.yangyouwang.module.wx.service.WxMaUserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 微信小程序用户Service实现类
 *
 * @author yangyouwang
 */
@Slf4j
@Service
public class WxMaUserServiceImpl extends ServiceImpl<WxMaUserMapper, WxMaUser> implements WxMaUserService {

    @Autowired
    private WxMaService wxMaService;

    /**
     * 根据openid查询用户
     * @param openid 用户唯一标识
     * @return 微信小程序用户
     */
    @Override
    public WxMaUser getByOpenid(String openid) {
        return lambdaQuery()
                .eq(WxMaUser::getOpenid, openid)
                .one();
    }

    /**
     * 微信登录
     * @param appid 小程序appid
     * @param code 登录code
     * @return 微信小程序用户
     */
    @Override
    public WxMaUser wxLogin(String appid, String code) {
        if (!wxMaService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            String openid = session.getOpenid();
            String sessionKey = session.getSessionKey();

            // 查找用户是否已存在
            WxMaUser user = getByOpenid(openid);
            if (user == null) {
                // 新建用户
                user = new WxMaUser();
                user.setAppid(appid);
                user.setOpenid(openid);
                user.setSessionKey(sessionKey);
                user.setStatus(0); // 正常状态
                user.setBalance(BigDecimal.ZERO); // 初始余额为0
                save(user);
            } else {
                // 更新sessionKey和最后登录时间
                user.setSessionKey(sessionKey);
                user.setLastLoginTime(new Date());
                updateById(user);
            }

            return user;
        } catch (WxErrorException e) {
            log.error("微信登录失败: {}", e.getMessage(), e);
            throw new RuntimeException("微信登录失败", e);
        }
    }

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 更新结果
     */
    @Override
    public boolean updateUserInfo(WxMaUser user) {
        if (user == null || user.getOpenid() == null) {
            return false;
        }

        WxMaUser dbUser = getByOpenid(user.getOpenid());
        if (dbUser == null) {
            return false;
        }

        // 更新用户信息
        dbUser.setNickName(user.getNickName());
        dbUser.setAvatarUrl(user.getAvatarUrl());
        dbUser.setGender(user.getGender());
        dbUser.setCountry(user.getCountry());
        dbUser.setProvince(user.getProvince());
        dbUser.setCity(user.getCity());
        dbUser.setLanguage(user.getLanguage());

        return updateById(dbUser);
    }

    /**
     * 绑定手机号
     * @param openid 用户唯一标识
     * @param phoneNumber 手机号
     * @return 绑定结果
     */
    @Override
    public boolean bindPhone(String openid, String phoneNumber) {
        WxMaUser user = getByOpenid(openid);
        if (user == null) {
            return false;
        }

        user.setPhoneNumber(phoneNumber);
        return updateById(user);
    }
}