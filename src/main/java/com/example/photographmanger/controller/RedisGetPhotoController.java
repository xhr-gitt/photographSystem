package com.example.photographmanger.controller;

import com.example.photographmanger.service.RedisTestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Tag(name = "Redis查询图片")
@RestController
@RequestMapping("/api/oss")
public class RedisGetPhotoController {
    @Autowired
    RedisTestService redisTestService;

    /**
     * 根据键名获取Redis中存储的图片路径
     * @param key Redis键名
     * @return 图片路径或错误信息
     */
    @GetMapping("/photo")
    public ResponseEntity<?> getRedisPhoto(@RequestParam String key) {
        try {
            String photoUrl = redisTestService.getValue(key);

            if (photoUrl == null || photoUrl.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Arrays.asList(
                                "code", 404,
                                "message", "未找到对应的图片路径",
                                "data", null
                        ));
            }

            return ResponseEntity.ok(Arrays.asList(
                    "code", 200,
                    "message", "成功获取图片路径",
                    "data", photoUrl
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Arrays.asList(
                            "code", 500,
                            "message", "获取图片路径时发生错误: " + e.getMessage(),
                            "data", null
                    ));
        }
    }

    /**
     * 批量获取图片路径
     * @param keys Redis键名列表
     * @return 图片路径映射
     */
    @GetMapping("/photos")
    public ResponseEntity<?> getRedisPhotos(@RequestParam List<String> keys) {
        try {
            Map<String, String> photoMap = new HashMap<>();

            for (String key : keys) {
                String value = redisTestService.getValue(key);
                if (value != null && !value.isEmpty()) {
                    photoMap.put(key, value);
                }
            }

            if (photoMap.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Arrays.asList(
                                "code", 404,
                                "message", "未找到任何图片路径",
                                "data", null
                        ));
            }

            return ResponseEntity.ok(Arrays.asList(
                    "code", 200,
                    "message", "成功获取图片路径",
                    "data", photoMap
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Arrays.asList(
                            "code", 500,
                            "message", "批量获取图片路径时发生错误: " + e.getMessage(),
                            "data", null
                    ));
        }
    }
}
