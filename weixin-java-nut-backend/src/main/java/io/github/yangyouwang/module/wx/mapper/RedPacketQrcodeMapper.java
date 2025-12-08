package io.github.yangyouwang.module.wx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.yangyouwang.module.wx.entity.RedPacketQrcode;
import org.apache.ibatis.annotations.Mapper;

/**
 * 红包二维码Mapper接口
 * @author yangyouwang
 * @date 2023-xx-xx
 */
@Mapper
public interface RedPacketQrcodeMapper extends BaseMapper<RedPacketQrcode> {

    /**
     * 根据二维码ID查询红包信息
     * @param qrcodeId 二维码ID
     * @return 红包信息
     */
    RedPacketQrcode selectByQrcodeId(String qrcodeId);

    /**
     * 更新二维码状态
     * @param qrcodeId 二维码ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(String qrcodeId, Integer status);

    /**
     * 更新提现状态
     * @param qrcodeId 二维码ID
     * @param withdrawStatus 提现状态
     * @param orderNo 订单号
     * @return 影响行数
     */
    int updateWithdrawStatus(String qrcodeId, Integer withdrawStatus, String orderNo);
}