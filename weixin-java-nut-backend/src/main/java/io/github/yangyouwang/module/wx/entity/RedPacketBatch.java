package io.github.yangyouwang.module.wx.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import java.math.BigDecimal;
import java.util.Date;
import io.github.yangyouwang.common.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 红包批次信息表
 * </p>
 *
 * @author paul
 * @since 2025-08-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="RedPacketBatch对象", description="红包批次信息表")
public class RedPacketBatch extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "批次ID")
    private String batchId;

    @ApiModelProperty(value = "总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "总数量")
    private Integer totalCount;

    @ApiModelProperty(value = "已使用数量")
    private Integer usedCount;

    @ApiModelProperty(value = "已提现数量")
    private Integer withdrawnCount;

    @ApiModelProperty(value = "批次状态：0-未生成，1-已生成，2-已过期")
    private Integer status;

    @ApiModelProperty(value = "金额类型：fixed-固定金额，random-随机金额")
    private String amountType;

//    @ApiModelProperty(value = "最小金额（随机金额时使用）")
//    private BigDecimal minAmount;
//
//    @ApiModelProperty(value = "最大金额（随机金额时使用）")
//    private BigDecimal maxAmount;

//    @ApiModelProperty(value = "单个金额（固定金额时使用）")
//    private BigDecimal singleAmount;

    @TableField(exist = false)
    @ApiModelProperty(value = "关联的二维码列表")
    private java.util.List<RedPacketQrcode> qrcodes;

}
