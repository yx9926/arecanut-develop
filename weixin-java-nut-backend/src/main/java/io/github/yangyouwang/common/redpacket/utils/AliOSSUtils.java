package io.github.yangyouwang.common.redpacket.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
public class AliOSSUtils {
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    /**
     * 上传文件到OSS
     * @param file 要上传的文件
     * @param directory 存储的目录（可选，如：avatars/）
     * @return 文件访问URL
     */
    public String uploadFile(MultipartFile file, String directory) throws IOException {
        // 获取上传的文件的输入流
        InputStream inputStream = file.getInputStream();

        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = (directory != null ? directory : "") + UUID.randomUUID().toString() + extension;

        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            // 上传文件到OSS
            ossClient.putObject(bucketName, fileName, inputStream);

            // 构建文件访问路径
            return "https://" + bucketName + "." + endpoint + "/" + fileName;
        } finally {
            // 关闭OSSClient
            if (ossClient != null) {
                ossClient.shutdown();
            }
            // 关闭输入流
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /**
     * 上传图片到OSS（简化方法，不指定目录）
     */
    public String uploadImage(MultipartFile file) throws IOException {
        return uploadFile(file, "images/");
    }

    /**
     * 上传头像到OSS
     */
    public String uploadAvatar(MultipartFile file) throws IOException {
        return uploadFile(file, "avatars/");
    }
}
