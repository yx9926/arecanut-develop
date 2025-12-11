package io.github.yangyouwang.common.redpacket.enums;

import lombok.Getter;

@Getter
public enum RedPacketStatus {
    EMPTY(0),      // 红包为空
    AVAILABLE(1);  // 红包有

    private final Integer code;

    RedPacketStatus(Integer code) {
        this.code = code;
    }

    // 根据 code 获取枚举
    public static RedPacketStatus fromCode(Integer code) {
        if (code == null) return null;
        for (RedPacketStatus status : RedPacketStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知红包状态: " + code);
    }
}