package io.github.yangyouwang.common.redpacket.utils;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class QRCodeUtil {

    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;

    /**
     * 将任意对象生成二维码图片文件
     *
     * @param content 任意对象，会调用 toString() 转为字符串
     * @param filePath 图片保存路径，例如 "qrcode.png"
     * @param <T> 泛型对象类型
     * @throws Exception
     */
    public static <T> void generateQRCodeToFile(T content, String filePath) throws Exception {
        if (content == null) {
            throw new IllegalArgumentException("二维码内容不能为空");
        }

        String text = content.toString(); // 泛型对象转字符串
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        BitMatrix matrix = new MultiFormatWriter().encode(
                text,
                BarcodeFormat.QR_CODE,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT,
                hints
        );

        Path path = new File(filePath).toPath();
        MatrixToImageWriter.writeToPath(matrix, getFileExtension(filePath), path);
    }

    private static String getFileExtension(String filePath) {
        int index = filePath.lastIndexOf('.');
        return index > 0 ? filePath.substring(index + 1) : "png";
    }
}