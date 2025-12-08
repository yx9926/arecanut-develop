package io.github.yangyouwang.module.wx.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.yangyouwang.common.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 微信小程序用户表
 * </p>
 *
 * @author paul
 * @since 2025-08-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wx_ma_user")
@ApiModel(value="MaUser对象", description="微信小程序用户表")
public class MaUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "小程序appid")
    private String appid;

    @ApiModelProperty(value = "用户唯一标识")
    private String openid;

    @ApiModelProperty(value = "会话密钥")
    private String sessionKey;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户头像URL")
    private String avatarUrl;

    @ApiModelProperty(value = "性别 0-未知、1-男、2-女")
    private Integer gender;

    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;

    @ApiModelProperty(value = "国家/地区")
    private String country;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "语言")
    private String language;

    @ApiModelProperty(value = "用户状态 0-正常、1-禁用")
    private Integer status;

    @ApiModelProperty(value = "余额")
    private BigDecimal balance;

    @ApiModelProperty(value = "删除标志 0-未删除、1-已删除")
    private Integer delFlag;


}
