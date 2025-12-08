package io.github.yangyouwang.module.wx.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import io.github.yangyouwang.common.base.domain.Result;
import io.github.yangyouwang.module.wx.entity.WxWithdrawRecord;
import io.github.yangyouwang.module.wx.service.WxWithdrawRecordService;
import io.github.yangyouwang.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 微信用户提现记录控制器
 *
 * @author yangyouwang
 */
@Api(tags = "微信用户提现记录")
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/wx/withdraw/{appid}")
public class WxWithdrawRecordController {

    private final WxMaService wxMaService;
    private final WxWithdrawRecordService wxWithdrawRecordService;

    /**
     * 申请提现
     */
    @ApiOperation(value = "申请提现")
    @PostMapping("/apply")
    public Result applyWithdraw(@PathVariable String appid, @RequestBody WxWithdrawRecord record) {
        if (!wxMaService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        try {
            if (record == null || StringUtils.isBlank(record.getOpenid()) || record.getAmount() == null) {
                return Result.failure("参数错误");
            }

            // 检查金额是否大于0
            if (record.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                return Result.failure("提现金额必须大于0");
            }

            boolean result = wxWithdrawRecordService.createWithdrawRecord(record, appid);
            if (result) {
                return Result.success("申请成功");
            } else {
                return Result.failure("申请失败");
            }
        } catch (Exception e) {
            log.error("申请提现失败: {}", e.getMessage(), e);
            return Result.failure("系统异常: " + e.getMessage());
        } finally {
            WxMaConfigHolder.remove();//清理ThreadLocal
        }
    }

    /**
     * 查询用户提现记录
     */
    @ApiOperation(value = "查询用户提现记录")
    @GetMapping("/list")
    public Result<List<WxWithdrawRecord>> getWithdrawList(@PathVariable String appid, String openid) {
        if (!wxMaService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        try {
            if (StringUtils.isBlank(openid)) {
                return Result.failure("参数错误");
            }

            List<WxWithdrawRecord> records = wxWithdrawRecordService.getByOpenid(openid);
            return Result.success(records);
        } catch (Exception e) {
            log.error("查询提现记录失败: {}", e.getMessage(), e);
            return Result.failure("系统异常: " + e.getMessage());
        } finally {
            WxMaConfigHolder.remove();//清理ThreadLocal
        }
    }

    /**
     * 查询提现记录详情
     */
    @ApiOperation(value = "查询提现记录详情")
    @GetMapping("/detail")
    public Result<WxWithdrawRecord> getWithdrawDetail(@PathVariable String appid, Long id) {
        if (!wxMaService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        try {
            if (id == null) {
                return Result.failure("参数错误");
            }

            WxWithdrawRecord record = wxWithdrawRecordService.getById(id);
            return Result.success(record);
        } catch (Exception e) {
            log.error("查询提现记录详情失败: {}", e.getMessage(), e);
            return Result.failure("系统异常: " + e.getMessage());
        } finally {
            WxMaConfigHolder.remove();//清理ThreadLocal
        }
    }

    /**
     * 更新提现状态
     */
    @ApiOperation(value = "更新提现状态")
    @PostMapping("/updateStatus")
    public String updateWithdrawStatus(@PathVariable String appid, @RequestBody WxWithdrawRecord record) {
        if (!wxMaService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        try {
            if (record == null || record.getId() == null || record.getStatus() == null) {
                return JsonUtils.toJson("参数错误");
            }

            boolean result = wxWithdrawRecordService.updateWithdrawStatus(
                    record.getId(), record.getStatus(), record.getRemark());
            if (result) {
                return JsonUtils.toJson("更新成功");
            } else {
                return JsonUtils.toJson("更新失败");
            }
        } catch (Exception e) {
            log.error("更新提现状态失败: {}", e.getMessage(), e);
            return JsonUtils.toJson("系统异常");
        } finally {
            WxMaConfigHolder.remove();//清理ThreadLocal
        }
    }
}