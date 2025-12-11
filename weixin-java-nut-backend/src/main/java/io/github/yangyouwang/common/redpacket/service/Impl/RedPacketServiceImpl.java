package io.github.yangyouwang.common.redpacket.service.Impl;

import io.github.yangyouwang.common.redpacket.entity.RedPacket;
import io.github.yangyouwang.common.redpacket.enums.RedPacketStatus;
import io.github.yangyouwang.common.redpacket.mapper.RedPacketMapper;
import io.github.yangyouwang.common.redpacket.service.RedPacketService;
import io.github.yangyouwang.common.redpacket.utils.AliOSSUtils;
import io.github.yangyouwang.common.redpacket.utils.QRCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class RedPacketServiceImpl implements RedPacketService {

    @Autowired
    private RedPacketMapper redPacketMapper;
    @Autowired
    private AliOSSUtils aliOSSUtils;

    @Override
    public void setNewPacket(RedPacket redPacket) {

        if(redPacket.getPassTime() == null){
            //没设置过期时间就是默认6个月过期
            LocalDate passTime = LocalDate.now().plusMonths(6);
            redPacket.setPassTime(passTime);
            redPacket.setSymbol(RedPacketStatus.AVAILABLE.getCode());

            try {
                //可以使用oss自自生线程实现，加快速度
                String QRCodeUrl = aliOSSUtils.uploadImage(QRCodeUtil.generateQRCodeFile(redPacket));
                redPacket.setPacketUrl(QRCodeUrl);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
