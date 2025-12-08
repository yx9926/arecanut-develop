package io.github.yangyouwang.module.wx.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import io.github.yangyouwang.common.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 微信用户提现记录表
 * </p>
 *
 * @author paul
 * @since 2025-08-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wx_withdraw_record")
@ApiModel(value="WithdrawRecord对象", description="微信用户提现记录表")
public class WithdrawRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
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
    private Date applyTime;

    @ApiModelProperty(value = "处理时间")
    private Date processTime;

    @ApiModelProperty(value = "拒绝原因")
    private String rejectReason;

    @ApiModelProperty(value = "交易单号")
    private String tradeNo;

    @ApiModelProperty(value = "删除标志 0-未删除、1-已删除")
    private Integer delFlag;


}
