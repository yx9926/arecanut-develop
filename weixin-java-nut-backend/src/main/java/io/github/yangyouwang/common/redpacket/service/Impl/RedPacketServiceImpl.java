package io.github.yangyouwang.common.redpacket.service.Impl;

import io.github.yangyouwang.common.redpacket.entity.RedPacket;
import io.github.yangyouwang.common.redpacket.enums.RedPacketStatus;
import io.github.yangyouwang.common.redpacket.mapper.RedPacketMapper;
import io.github.yangyouwang.common.redpacket.service.RedPacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class RedPacketServiceImpl implements RedPacketService {

    @Autowired
    private RedPacketMapper redPacketMapper;

    @Override
    public void setNewPacket(RedPacket redPacket) {
        //设置唯一id
        redPacket.setPacketId(UUID.randomUUID().toString());
        if(redPacket.getPassTime() == null){
            //没设置过期时间就是默认6个月过期
            LocalDate passTime = LocalDate.now().plusMonths(6);
            redPacket.setPassTime(passTime);
            redPacket.setSymbol(RedPacketStatus.AVAILABLE.getCode());
        }
    }
}
