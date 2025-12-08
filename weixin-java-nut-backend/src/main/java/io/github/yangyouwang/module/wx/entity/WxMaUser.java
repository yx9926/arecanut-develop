package io.github.yangyouwang.module.wx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.yangyouwang.common.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信小程序用户实体类
 *
 * @author yangyouwang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="微信小程序用户实体类", description="微信小程序用户表")
@TableName("wx_ma_user")
public class WxMaUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 小程序appid
     */
    @ApiModelProperty(value = "小程序appid")
    private String appid;

    /**
     * 用户唯一标识
     */
    @ApiModelProperty(value = "用户唯一标识")
    private String openid;

    /**
     * 会话密钥
     */
    @ApiModelProperty(value = "会话密钥")
    private String sessionKey;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    /**
     * 用户头像URL
     */
    @ApiModelProperty(value = "用户头像URL")
    private String avatarUrl;

    /**
     * 性别 0-未知、1-男、2-女
     */
    @ApiModelProperty(value = "性别 0-未知、1-男、2-女")
    private Integer gender;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;

    /**
     * 国家/地区
     */
    @ApiModelProperty(value = "国家/地区")
    private String country;

    /**
     * 省份
     */
    @ApiModelProperty(value = "省份")
    private String province;

    /**
     * 城市
     */
    @ApiModelProperty(value = "城市")
    private String city;

    /**
     * 语言
     */
    @ApiModelProperty(value = "语言")
    private String language;

    /**
     * 用户状态 0-正常、1-禁用
     */
    @ApiModelProperty(value = "用户状态 0-正常、1-禁用")
    private Integer status;

    /**
     * 余额
     */
    @ApiModelProperty(value = "余额")
    private BigDecimal balance;

    /**
     * 最后登录时间
     */
    @ApiModelProperty(value = "最后登录时间")
    private Date lastLoginTime;

    /**
     * 最后登录IP
     */
    @ApiModelProperty(value = "最后登录IP")
    private String lastLoginIp;
}