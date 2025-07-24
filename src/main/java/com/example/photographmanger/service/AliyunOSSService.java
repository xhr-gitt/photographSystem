package com.example.photographmanger.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class AliyunOSSService {
    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Autowired
    private OSS ossClient;

    public String uploadFile(MultipartFile file, String filePath) throws IOException {
        // 生成文件名（保留原始扩展名）
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = filePath + "/" + UUID.randomUUID() + fileExtension;

        // 关键：设置正确的元数据
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType(file.getContentType()); // 自动识别MIME类型
        meta.setContentDisposition("inline"); // 强制浏览器预览
        meta.setHeader("x-oss-force-download", "false"); // 关闭OSS强制下载

        // 上传文件（带元数据）
        ossClient.putObject(bucketName, fileName, file.getInputStream(), meta);

        // 返回URL（无需编码，OSS会自动处理）
        return "https://" + bucketName + "." + endpoint.replace("http://", "") + "/" + fileName;
    }


    public void deleteFile(String fileUrl) {
        String bucketUrl = "https://" + bucketName + "." + endpoint.replace("http://", "") + "/";
        String fileName = fileUrl.replace(bucketUrl, "");
        ossClient.deleteObject(bucketName, fileName);
    }
}