package com.example.photographmanger.repository;

import com.example.photographmanger.entity.UploadRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadRecordRepository extends JpaRepository<UploadRecord, Long> {
}