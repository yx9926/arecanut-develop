package io.github.yangyouwang.common.redpacket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

@Data
@TableName("redpacket")
public class RedPacket {

    //由uuid生成
    @TableId(type = IdType.AUTO)
    private Long packetId;

    private Integer packetNum;

    //红包是否有钱的标志：0无1有
    private Integer symbol;
    //过期时间
    private LocalDate passTime;

    private String packetUrl;
}
