package com.example.photographmanger.controller;

import com.example.photographmanger.entity.UploadRecord;
import com.example.photographmanger.repository.UploadRecordRepository;
import com.example.photographmanger.service.RedisTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private UploadRecordRepository uploadRecordRepository;

    @Autowired
    private RedisTestService redisTestService;

    @GetMapping("/mysql")
    public String testMySQL() {
        UploadRecord record = new UploadRecord();
        record.setFileName("test.txt");
        record.setFileUrl("http://example.com/test.txt");
        record.setFileSize(1024L);
        uploadRecordRepository.save(record);

        return "MySQL test passed. Records count: " + uploadRecordRepository.count();
    }

    @GetMapping("/redis")
    public String testRedis() {
        String key = "test:key";
        String value = "Hello, Redis!";

        redisTestService.setValue(key, value);
        String retrievedValue = redisTestService.getValue(key);

        return "Redis test passed. Value: " + retrievedValue;
    }
    @GetMapping("/text")
    public String testtext() {

        return "text test "+"-----" ;
    }
}
