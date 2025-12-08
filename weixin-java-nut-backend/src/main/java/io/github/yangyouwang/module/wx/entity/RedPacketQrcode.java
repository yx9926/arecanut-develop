package io.github.yangyouwang.module.wx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 红包二维码实体类
 * @author yangyouwang
 * @date 2023-xx-xx
 */
@Data
@TableName("red_packet_qrcode")
public class RedPacketQrcode {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 批次ID，用于标识同一袋产品的二维码
     */
    private String batchId;

    /**
     * 二维码唯一标识
     */
    private String qrcodeId;

    /**
     * 红包金额
     */
    private BigDecimal amount;

    /**
     * 二维码状态：0-未使用，1-已使用，2-已过期
     */
    private Integer status;

    /**
     * 使用时间
     */
    private Date usedTime;

    /**
     * 使用用户ID
     */
    private String userId;

    /**
     * 提现状态：0-未提现，1-提现中，2-提现成功，3-提现失败
     */
    private Integer withdrawStatus;

    /**
     * 提现时间
     */
    private Date withdrawTime;

    /**
     * 企业付款订单号
     */
    private String orderNo;

    /**
     * 该批次总金额
     */
    private BigDecimal totalAmount;

    /**
     * 该批次总数量
     */
    private Integer totalCount;

    /**
     * 二维码图片路径
     */
    private String qrcodePath;

    /**
     * 小程序地址
     */
    private String miniProgramUrl;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}