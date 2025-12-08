package io.github.yangyouwang.module.wx.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import io.github.yangyouwang.common.annotation.PassToken;
import io.github.yangyouwang.common.base.domain.Result;
import io.github.yangyouwang.module.wx.entity.WxMaUser;
import io.github.yangyouwang.module.wx.service.WxMaUserService;
import io.github.yangyouwang.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信小程序用户接口
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Api(tags = "微信小程序用户")
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/wx/user/{appid}")
public class WxMaUserController {
    private final WxMaService wxMaService;
    private final WxMaUserService wxMaUserService;

    /**
     * 登陆接口
     */
    @ApiOperation(value = "登陆接口")
    @PassToken
    @GetMapping("/login")
    public Result login(@PathVariable String appid, String code) {
        if (StringUtils.isBlank(code)) {
            return Result.failure("code为空");
        }
        try {
            // 微信登录并获取用户信息
            WxMaUser user = wxMaUserService.wxLogin(appid, code);
            return Result.success(user);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            return Result.failure(e.getMessage());
        } catch (RuntimeException e) {
            log.error("登录失败: {}", e.getMessage(), e);
            return Result.failure("登录失败: " + e.getMessage());
        } finally {
            WxMaConfigHolder.remove();//清理ThreadLocal
        }
    }

    /**
     * <pre>
     * 获取用户信息接口
     * </pre>
     */
    @ApiOperation(value = "获取用户信息接口")
    @GetMapping("/info")
    public Result info(@PathVariable String appid, String sessionKey,
                       String signature, String rawData, String encryptedData, String iv) {
        if (!wxMaService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        try {
            // 用户信息校验
            if (!wxMaService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
                return Result.failure("用户信息校验失败");
            }

            // 解密用户信息
            WxMaUserInfo userInfo = wxMaService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
            return Result.success(userInfo);
        } catch (Exception e) {
            return Result.failure("获取用户信息失败: " + e.getMessage());
        } finally {
            WxMaConfigHolder.remove();//清理ThreadLocal
        }
    }

    /**
     * <pre>
     * 获取用户绑定手机号信息
     * </pre>
     */
    @ApiOperation(value = "获取用户绑定手机号信息")
    @GetMapping("/phone")
    public Result phone(@PathVariable String appid, String sessionKey, String signature,
                        String rawData, String encryptedData, String iv) {
        if (!wxMaService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        try {
            // 用户信息校验
            if (!wxMaService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
                return Result.failure("用户信息校验失败");
            }

            // 获取手机号信息
            WxMaPhoneNumberInfo phoneNoInfo = wxMaService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);
            return Result.success(phoneNoInfo);
        } catch (Exception e) {
            return Result.failure("获取手机号信息失败: " + e.getMessage());
        } finally {
            WxMaConfigHolder.remove();//清理ThreadLocal
        }
    }

}
