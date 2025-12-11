package io.github.yangyouwang.common.redpacket.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RedPacket {

    //由uuid生成
    private String packetId;

    private Integer packetNum;

    //红包是否有钱的标志：0无1有
    private Integer symbol;
    //过期时间
    private LocalDate passTime;

    private String packetUrl;
}
