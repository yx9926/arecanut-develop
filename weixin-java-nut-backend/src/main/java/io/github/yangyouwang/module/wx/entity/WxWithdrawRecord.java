package io.github.yangyouwang.module.wx.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.yangyouwang.common.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信用户提现记录表
 *
 * @author yangyouwang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wx_withdraw_record")
@ApiModel(value = "WxWithdrawRecord对象", description = "微信用户提现记录")
public class WxWithdrawRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "小程序appid")
    private String appid;

    @ApiModelProperty(value = "微信用户openid")
    private String openid;

    @ApiModelProperty(value = "提现金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "提现状态：0-申请中，1-已处理，2-已拒绝")
    private Integer status;

    @ApiModelProperty(value = "提现方式：0-微信支付")
    private Integer method;

    @ApiModelProperty(value = "申请时间")
    @TableField("apply_time")
    private Date applyTime;

    @ApiModelProperty(value = "处理时间")
    @TableField("process_time")
    private Date processTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "拒绝原因")
    @TableField("reject_reason")
    private String rejectReason;

    @ApiModelProperty(value = "交易单号")
    @TableField("trade_no")
    private String tradeNo;

}