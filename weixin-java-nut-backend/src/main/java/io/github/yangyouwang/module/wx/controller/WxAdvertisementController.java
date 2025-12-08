package io.github.yangyouwang.module.wx.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import io.github.yangyouwang.common.base.domain.Result;
import io.github.yangyouwang.module.wx.entity.WxAdvertisement;
import io.github.yangyouwang.module.wx.service.WxAdvertisementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 广告图控制器
 *
 * @author yangyouwang
 */
@Api(tags = "微信小程序广告图")
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/wx/advertisement/{appid}")
public class WxAdvertisementController {

    private final WxMaService wxMaService;
    private final WxAdvertisementService wxAdvertisementService;

    /**
     * 获取最新的启用广告图
     */
    @ApiOperation(value = "获取最新广告图")
    @GetMapping("/latest")
    public Result<WxAdvertisement> getLatestAdvertisement(@PathVariable String appid) {
        if (!wxMaService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        try {
            // 获取最新的启用广告图
            WxAdvertisement advertisement = wxAdvertisementService.getLatestEnabledAdvertisement();
            
            if (advertisement != null) {
                return Result.success(advertisement);
            } else {
                return Result.success(null);
            }
        } catch (Exception e) {
            log.error("获取广告图失败: {}", e.getMessage(), e);
            return Result.failure("获取广告图失败");
        }
    }
}