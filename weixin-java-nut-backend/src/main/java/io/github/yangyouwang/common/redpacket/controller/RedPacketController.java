package io.github.yangyouwang.common.redpacket.controller;

import io.github.yangyouwang.common.base.domain.Result;
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
    public Result setNewPacket(@RequestBody RedPacket redPacket){

        redPacketService.setNewPacket(redPacket);
        return Result.success("红包生成完毕（后续回显url");
    }
}
