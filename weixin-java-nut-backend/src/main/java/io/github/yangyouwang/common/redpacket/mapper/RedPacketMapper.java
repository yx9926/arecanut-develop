package io.github.yangyouwang.common.redpacket.mapper;

import io.github.yangyouwang.common.redpacket.entity.RedPacket;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RedPacketMapper {

    void setNewPacket(RedPacket redPacket);
}
