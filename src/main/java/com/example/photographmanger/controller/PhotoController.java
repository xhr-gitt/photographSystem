package com.example.photographmanger.controller;

import com.example.photographmanger.entity.Photo;
import com.example.photographmanger.entity.User;
import com.example.photographmanger.service.AliyunOSSService;
import com.example.photographmanger.service.PhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "照片管理")
@RestController
@RequestMapping("/api/photos")
public class PhotoController {
    private final PhotoService photoService;
    private final AliyunOSSService ossService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public PhotoController(PhotoService photoService, AliyunOSSService ossService) {
        this.photoService = photoService;
        this.ossService = ossService;
    }

    @PostMapping
    @Operation(summary = "上传照片并保存信息")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Photo> createPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("category") String category,
            Authentication authentication) {
        try {
            // 上传文件到OSS获取URL
            String fileUrl = ossService.uploadFile(file, "uploads");

            // 保存照片信息到数据库
            Photo photo = photoService.savePhoto(category, fileUrl);

            return ResponseEntity.ok(photo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "根据分类获取照片")
  //  @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Photo>> getPhotosByCategory(@PathVariable String category) {
        return ResponseEntity.ok(photoService.getPhotosByCategory(category));
    }

}