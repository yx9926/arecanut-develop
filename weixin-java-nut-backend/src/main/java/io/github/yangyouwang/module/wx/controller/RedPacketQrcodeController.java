package io.github.yangyouwang.module.wx.controller;

import io.github.yangyouwang.module.wx.entity.RedPacketQrcode;
import io.github.yangyouwang.module.wx.service.RedPacketQrcodeService;
import io.github.yangyouwang.common.base.domain.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 红包二维码控制器
 * @author yangyouwang
 * @date 2025-01-16
 */
@Api(tags = "红包二维码")
@RestController
@RequestMapping("/wx/redPacketQrcode")
public class RedPacketQrcodeController {

    @Autowired
    private RedPacketQrcodeService redPacketQrcodeService;

    /**
     * 根据ID获取红包详情
     * @param id 红包ID (对应小程序参数hid)
     * @return 红包信息
     */
    @ApiOperation(value = "根据ID获取红包详情")
    @GetMapping("/detail/{id}")
    public Result<RedPacketQrcode> getDetail(
            @ApiParam(value = "红包ID", required = true) 
            @PathVariable Long id) {
        RedPacketQrcode redPacket = redPacketQrcodeService.getById(id);
        if (redPacket == null) {
            return Result.failure("红包不存在");
        }
        
        // 如果红包已被使用，返回提示
        if (redPacket.getStatus() != 0) {
            return Result.failure("红包已被使用");
        }
        
        return Result.success(redPacket);
    }

    /**
     * 根据二维码ID获取红包信息（小程序专用）
     * @param qrcodeId 二维码ID
     * @return 红包信息
     */
    @ApiOperation(value = "根据二维码ID获取红包信息")
    @GetMapping("/qrcode/{qrcodeId}")
    public Result<RedPacketQrcode> getRedPacketByQrcodeId(
            @ApiParam(value = "二维码ID", required = true) 
            @PathVariable String qrcodeId) {
        RedPacketQrcode redPacket = redPacketQrcodeService.getByQrcodeId(qrcodeId);
        if (redPacket == null) {
            return Result.failure("红包不存在");
        }
        
        return Result.success(redPacket);
    }

    /**
     * 使用红包
     * @param id 红包ID (对应小程序参数hid)
     * @param openid 用户openid
     * @return 使用结果
     */
    @ApiOperation(value = "使用红包")
    @PostMapping("/use/{id}")
    public Result<Boolean> useRedPacket(
            @ApiParam(value = "红包ID", required = true) 
            @PathVariable Long id,
            @ApiParam(value = "用户openid", required = true) 
            @RequestParam String openid) {
        try {
            RedPacketQrcode redPacket = redPacketQrcodeService.getById(id);
            if (redPacket == null) {
                return Result.failure("红包不存在");
            }
            
            boolean success = redPacketQrcodeService.useQrcode(redPacket.getQrcodeId(), openid);
            return success ? Result.success(true) : Result.failure("红包使用失败");
        } catch (Exception e) {
            return Result.failure("红包使用失败: " + e.getMessage());
        }
    }
}