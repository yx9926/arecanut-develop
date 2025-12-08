package io.github.yangyouwang.module.wx.service;

import io.github.yangyouwang.module.wx.entity.RedPacketQrcode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 红包二维码Service测试
 * @author yangyouwang
 * @date 2023-xx-xx
 */
@SpringBootTest
public class RedPacketQrcodeServiceTest {

    @Autowired
    private RedPacketQrcodeService redPacketQrcodeService;

    @Test
    public void testGenerateQrcodes() {
        String batchId = "test_batch_" + System.currentTimeMillis();
        Double totalAmount = 100.0;
        Integer count = 10;

        List<String> qrcodeIds = redPacketQrcodeService.generateQrcodes(batchId, totalAmount, count);
        assertNotNull(qrcodeIds);
        assertEquals(count, qrcodeIds.size());
        System.out.println("生成的二维码ID列表: " + qrcodeIds);
    }

    @Test
    public void testUseQrcode() {
        // 先生成一个二维码
        String batchId = "test_batch_" + System.currentTimeMillis();
        Double totalAmount = 10.0;
        Integer count = 1;

        List<String> qrcodeIds = redPacketQrcodeService.generateQrcodes(batchId, totalAmount, count);
        String qrcodeId = qrcodeIds.get(0);

        // 使用二维码
        String userId = "test_user_id";
        boolean result = redPacketQrcodeService.useQrcode(qrcodeId, userId);
        assertTrue(result);

        // 验证状态已更新
        RedPacketQrcode qrcode = redPacketQrcodeService.getByQrcodeId(qrcodeId);
        assertEquals(1, qrcode.getStatus());
        assertEquals(userId, qrcode.getUserId());
        assertNotNull(qrcode.getUsedTime());
    }

    @Test
    public void testWithdraw() {
        // 先生成并使用一个二维码
        String batchId = "test_batch_" + System.currentTimeMillis();
        Double totalAmount = 10.0;
        Integer count = 1;

        List<String> qrcodeIds = redPacketQrcodeService.generateQrcodes(batchId, totalAmount, count);
        String qrcodeId = qrcodeIds.get(0);
        String userId = "test_user_id";
        redPacketQrcodeService.useQrcode(qrcodeId, userId);

        // 提现
        boolean result = redPacketQrcodeService.withdraw(qrcodeId, userId);
        assertTrue(result);

        // 验证状态已更新
        RedPacketQrcode qrcode = redPacketQrcodeService.getByQrcodeId(qrcodeId);
        assertEquals(2, qrcode.getWithdrawStatus());
        assertNotNull(qrcode.getWithdrawTime());
        assertNotNull(qrcode.getOrderNo());
    }
}