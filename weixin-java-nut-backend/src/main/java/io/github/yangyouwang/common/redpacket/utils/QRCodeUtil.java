package io.github.yangyouwang.common.redpacket.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class QRCodeUtil {

    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;

    public static <T> MultipartFile generateQRCodeFile(T content) throws Exception {
        BufferedImage image = generateImage(content);
        byte[] bytes = imageToBytes(image);

        return new ByteArrayMultipartFile(
                "file",
                "qrcode.png",
                "image/png",
                bytes
        );
    }

    private static <T> BufferedImage generateImage(T content) throws Exception {
        if (content == null) {
            throw new IllegalArgumentException("二维码内容不能为空");
        }

        String text = content.toString();

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        BitMatrix matrix = new MultiFormatWriter().encode(
                text,
                BarcodeFormat.QR_CODE,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT,
                hints
        );

        return MatrixToImageWriter.toBufferedImage(matrix);
    }

    private static byte[] imageToBytes(BufferedImage image) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    /**
     * 自定义 MultipartFile（不用 MockMultipartFile）
     */
    public static class ByteArrayMultipartFile implements MultipartFile {

        private final String name;
        private final String originalFilename;
        private final String contentType;
        private final byte[] content;

        public ByteArrayMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
            this.name = name;
            this.originalFilename = originalFilename;
            this.contentType = contentType;
            this.content = content;
        }

        @Override
        public String getName() { return name; }

        @Override
        public String getOriginalFilename() { return originalFilename; }

        @Override
        public String getContentType() { return contentType; }

        @Override
        public boolean isEmpty() { return content.length == 0; }

        @Override
        public long getSize() { return content.length; }

        @Override
        public byte[] getBytes() { return content; }

        @Override
        public InputStream getInputStream() { return new ByteArrayInputStream(content); }

        @Override
        public void transferTo(File dest) throws IOException {
            try (FileOutputStream fos = new FileOutputStream(dest)) {
                fos.write(content);
            }
        }
    }
}