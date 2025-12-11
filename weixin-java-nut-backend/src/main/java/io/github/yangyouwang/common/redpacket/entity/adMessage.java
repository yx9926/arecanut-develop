package io.github.yangyouwang.common.redpacket.entity;

import lombok.Data;

@Data
public class adMessage {
    //广告oss存储路径
    private String imageUrl;
    //广告定位路径
    private String adUrl;
}
