package io.github.yangyouwang.module.wx.controller;

import io.github.yangyouwang.common.annotation.PassToken;
import io.github.yangyouwang.common.base.domain.Result;
import io.github.yangyouwang.framework.security.util.JwtTokenUtil;
import io.github.yangyouwang.module.wx.entity.WxMaUser;
import io.github.yangyouwang.module.wx.service.WxMaUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信Token控制器
 * 用于为wx包下的接口生成token
 */
@Api(tags = "微信Token管理")
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/wx/token")
public class WxTokenController {

    private final WxMaUserService wxMaUserService;

    /**
     * 为wx包接口生成token
     * @param appid 小程序appid
     * @param openid 用户openid
     * @return token信息
     */
    @ApiOperation(value = "生成wx接口token")
    @PassToken
    @PostMapping("/generate")
    public Result generateToken(
            @ApiParam(name = "appid", value = "小程序appid", required = true) @RequestParam String appid,
            @ApiParam(name = "openid", value = "用户openid", required = true) @RequestParam String openid) {

        if (StringUtils.isBlank(appid) || StringUtils.isBlank(openid)) {
            return Result.failure("appid和openid不能为空");
        }

        try {
            // 查找用户是否存在
            WxMaUser user = wxMaUserService.getByOpenid(openid);
            if (user == null) {
                return Result.failure("未找到该用户");
            }

            // 验证appid是否匹配
            if (!appid.equals(user.getAppid())) {
                return Result.failure("appid与用户不匹配");
            }

            // 使用JwtTokenUtil静态方法生成token
            // 设置token有效期为1天（86400秒）
            String token = JwtTokenUtil.buildJWT(user.getId().toString(), null, null, null, null, 86400);

            return Result.success(token);
        } catch (Exception e) {
            log.error("生成token失败: {}", e.getMessage(), e);
            return Result.failure("生成token失败: " + e.getMessage());
        }
    }
}