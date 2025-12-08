package io.github.yangyouwang.module.wx.controller;

import io.github.yangyouwang.common.annotation.PassToken;
import io.github.yangyouwang.module.wx.entity.RedPacketQrcode;
import io.github.yangyouwang.module.wx.service.RedPacketQrcodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.github.yangyouwang.common.base.domain.Result;

/**
 * 红包Controller
 * @author yangyouwang
 * @date 2023-xx-xx
 */
@Api(tags = "红包管理")
@RestController
@RequestMapping("/wx/redPacket")
@AllArgsConstructor
public class RedPacketController {

    private final RedPacketQrcodeService redPacketQrcodeService;

    /**
     * 生成红包二维码
     * @param batchId 批次ID
     * @param totalAmount 总金额
     * @param count 数量
     * @return 二维码ID列表
     */
    @ApiOperation(value = "生成红包二维码")
    @PostMapping("/generate")
    public Result<List<String>> generateQrcodes(@RequestParam String batchId, @RequestParam Double totalAmount, @RequestParam Integer count) {
        try {
            List<String> qrcodeIds = redPacketQrcodeService.generateQrcodes(batchId, totalAmount, count);
            return Result.success(qrcodeIds);
        } catch (Exception e) {
            return Result.failure("生成二维码失败: " + e.getMessage());
        }
    }

    /**
     * 查询红包信息
     * @param qrcodeId 二维码ID
     * @return 红包信息
     */
    @ApiOperation(value = "查询红包信息")
    @PassToken
    @GetMapping("/info/{qrcodeId}")
    public Result<RedPacketQrcode> getRedPacketInfo(@PathVariable String qrcodeId) {
        try {
            RedPacketQrcode redPacket = redPacketQrcodeService.getByQrcodeId(qrcodeId);
            if (redPacket == null) {
                return Result.failure("红包不存在");
            }
            return Result.success(redPacket);
        } catch (Exception e) {
            return Result.failure("查询红包信息失败: " + e.getMessage());
        }
    }

    /**
     * 使用红包
     * @param qrcodeId 二维码ID
     * @return 是否成功
     */
    @ApiOperation(value = "使用红包")
    @PassToken
    @PostMapping("/use/{qrcodeId}")
    public Result<Boolean> useRedPacket(@PathVariable String qrcodeId) {
        try {
            // 在实际应用中，需要获取当前登录用户的ID
            String userId = "current_user_id"; // 临时占位
            boolean success = redPacketQrcodeService.useQrcode(qrcodeId, userId);
            return success ? Result.success(true) : Result.failure("红包使用失败");
        } catch (Exception e) {
            return Result.failure("红包使用失败: " + e.getMessage());
        }
    }

    /**
     * 领取红包（小程序专用）
     * @param qrcodeId 二维码ID
     * @return 领取结果
     */
    @ApiOperation(value = "领取红包")
    @PassToken
    @PostMapping("/receive")
    public Result<Boolean> receiveRedPacket(@RequestParam String qrcodeId) {
        try {
            // 在实际应用中，需要获取当前登录用户的openid
            String openid = "current_user_openid"; // 临时占位
            boolean success = redPacketQrcodeService.useQrcode(qrcodeId, openid);
            return success ? Result.success(true) : Result.failure("红包领取失败");
        } catch (Exception e) {
            return Result.failure("红包领取失败: " + e.getMessage());
        }
    }

    /**
     * 提现
     * @param qrcodeId 二维码ID
     * @return 是否成功
     */
    @ApiOperation(value = "提现")
    @PostMapping("/withdraw/{qrcodeId}")
    public Result<Boolean> withdraw(@PathVariable String qrcodeId) {
        try {
            // 在实际应用中，需要获取当前登录用户的ID
            String userId = "current_user_id"; // 临时占位
            boolean success = redPacketQrcodeService.withdraw(qrcodeId, userId);
            return success ? Result.success(true) : Result.failure("提现失败");
        } catch (Exception e) {
            return Result.failure("提现失败: " + e.getMessage());
        }
    }
}