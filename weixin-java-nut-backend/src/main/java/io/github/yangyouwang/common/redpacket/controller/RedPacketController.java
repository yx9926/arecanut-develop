package io.github.yangyouwang.common.redpacket.controller;

import io.github.yangyouwang.common.base.domain.Result;
import io.github.yangyouwang.common.enums.ResultStatus;
import io.github.yangyouwang.common.redpacket.entity.RedPacket;
import io.github.yangyouwang.common.redpacket.service.RedPacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/packet")
public class RedPacketController {

    @Autowired
    private RedPacketService redPacketService;

    @PostMapping("/set")
    public Result<String> setNewPacket(@RequestBody RedPacket redPacket){
        String packetUrl = redPacketService.setNewPacket(redPacket);
        if(packetUrl == null&& packetUrl.isEmpty()){
            return Result.failure("创建红包失败");
        }
        return Result.success(ResultStatus.SUCCESS.message,packetUrl);
    }
}
