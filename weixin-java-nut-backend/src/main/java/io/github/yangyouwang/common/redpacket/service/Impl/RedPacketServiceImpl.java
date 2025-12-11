package io.github.yangyouwang.common.redpacket.service.Impl;

import io.github.yangyouwang.common.redpacket.entity.RedPacket;
import io.github.yangyouwang.common.redpacket.enums.RedPacketStatus;
import io.github.yangyouwang.common.redpacket.mapper.RedPacketMapper;
import io.github.yangyouwang.common.redpacket.service.RedPacketService;
import io.github.yangyouwang.common.redpacket.utils.AliOSSUtils;
import io.github.yangyouwang.common.redpacket.utils.QRCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class RedPacketServiceImpl implements RedPacketService {

    @Autowired
    private RedPacketMapper redPacketMapper;
    @Autowired
    private AliOSSUtils aliOSSUtils;

    @Override
    public String setNewPacket(RedPacket redPacket) {
        //设置唯一id
        redPacket.setPacketId(System.currentTimeMillis());
        if (redPacket.getPassTime() == null) {
            //没设置过期时间就是默认6个月过期
            LocalDate passTime = LocalDate.now().plusMonths(6);
            redPacket.setPassTime(passTime);
            //打上有钱的标志
            redPacket.setSymbol(RedPacketStatus.AVAILABLE.getCode());
        }
        try {
            String str = aliOSSUtils.uploadImage(QRCodeUtil.generateQRCodeFile(redPacket));
            redPacket.setPacketUrl(str);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        redPacketMapper.setNewPacket(redPacket);

        return redPacket.getPacketUrl();
    }
}
