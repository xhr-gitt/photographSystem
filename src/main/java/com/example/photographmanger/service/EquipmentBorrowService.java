package com.example.photographmanger.service;

import com.example.photographmanger.dto.BorrowRecordDTO;
import com.example.photographmanger.dto.BorrowRecordResponseDTO;
import java.util.List;

public interface EquipmentBorrowService {
    BorrowRecordResponseDTO borrowEquipment(BorrowRecordDTO dto);
    BorrowRecordResponseDTO returnEquipment(Long recordId);
    List<BorrowRecordResponseDTO> getBorrowRecordsByEquipment(Long equipmentId);
    List<BorrowRecordResponseDTO> getBorrowRecordsByStaff(Long staffId);
    List<BorrowRecordResponseDTO> getActiveBorrowRecords();
    boolean isEquipmentBorrowed(Long equipmentId);
}