package io.github.yangyouwang.module.wx.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.yangyouwang.common.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 广告图实体类
 *
 * @author yangyouwang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wx_advertisement")
@ApiModel(value = "WxAdvertisement对象", description = "广告图信息")
public class WxAdvertisement extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "广告标题")
    private String title;

    @ApiModelProperty(value = "广告图片URL")
    @TableField("image_url")
    private String imageUrl;

    @ApiModelProperty(value = "广告展示时长（秒）")
    private Integer duration;

    @ApiModelProperty(value = "广告点击跳转链接")
    @TableField("link_url")
    private String linkUrl;

    @ApiModelProperty(value = "开始展示时间")
    @TableField("start_time")
    private Date startTime;

    @ApiModelProperty(value = "结束展示时间")
    @TableField("end_time")
    private Date endTime;

    @ApiModelProperty(value = "广告状态：0-禁用，1-启用")
    private Integer status;
}