package com.example.photographmanger.repository;

import com.example.photographmanger.entity.EquipmentBorrowRecord;
import com.example.photographmanger.entity.EquipmentBorrowRecord.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EquipmentBorrowRecordRepository extends JpaRepository<EquipmentBorrowRecord, Long> {
    List<EquipmentBorrowRecord> findByEquipmentId(Long equipmentId);
    List<EquipmentBorrowRecord> findByStaffId(Long staffId);
    List<EquipmentBorrowRecord> findByStatus(BorrowStatus status);
    boolean existsByEquipmentIdAndStatus(Long equipmentId, BorrowStatus status);
}