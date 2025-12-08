package io.github.yangyouwang.module.wx.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import io.github.yangyouwang.common.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 广告图表
 * </p>
 *
 * @author paul
 * @since 2025-08-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wx_advertisement")
@ApiModel(value="Advertisement对象", description="广告图表")
public class Advertisement extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "广告标题")
    private String title;

    @ApiModelProperty(value = "广告图片URL")
    private String imageUrl;

    @ApiModelProperty(value = "广告展示时长（秒），默认5秒")
    private Integer duration;

    @ApiModelProperty(value = "广告点击跳转链接")
    private String linkUrl;

    @ApiModelProperty(value = "开始展示时间")
    private Date startTime;

    @ApiModelProperty(value = "结束展示时间")
    private Date endTime;

    @ApiModelProperty(value = "广告状态：0-禁用，1-启用")
    private Integer status;

    @ApiModelProperty(value = "删除标志 0-未删除、1-已删除")
    private Integer deleted;


}
