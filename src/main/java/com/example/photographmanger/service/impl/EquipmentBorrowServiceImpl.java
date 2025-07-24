package com.example.photographmanger.service.impl;

import com.example.photographmanger.dto.BorrowRecordDTO;
import com.example.photographmanger.dto.BorrowRecordResponseDTO;
import com.example.photographmanger.dto.EquipmentDTO;
import com.example.photographmanger.dto.StaffResponseDTO;
import com.example.photographmanger.entity.*;
import com.example.photographmanger.exception.ResourceNotFoundException;
import com.example.photographmanger.repository.EquipmentBorrowRecordRepository;
import com.example.photographmanger.service.EquipmentBorrowService;
import com.example.photographmanger.service.EquipmentService;
import com.example.photographmanger.service.StaffProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipmentBorrowServiceImpl implements EquipmentBorrowService {

    private final EquipmentBorrowRecordRepository borrowRecordRepository;
    private final EquipmentService equipmentService;
    private final StaffProfileService staffProfileService;

    @Override
    @Transactional
    public BorrowRecordResponseDTO borrowEquipment(BorrowRecordDTO dto) {
        Equipment equipment = equipmentService.getEquipmentEntity(dto.getEquipmentId());
        if (equipment.getStatus() != Equipment.EquipmentStatus.AVAILABLE) {
            throw new IllegalStateException("设备当前不可借用");
        }

        StaffProfile staff = staffProfileService.getStaffEntity(dto.getStaffId());

        EquipmentBorrowRecord record = new EquipmentBorrowRecord();
        record.setEquipment(equipment);
        record.setStaff(staff);
        record.setPurpose(dto.getPurpose());

        equipmentService.updateEquipmentStatus(equipment.getId(), Equipment.EquipmentStatus.ASSIGNED);

        EquipmentBorrowRecord savedRecord = borrowRecordRepository.save(record);
        return convertToResponseDTO(savedRecord);
    }

    @Override
    @Transactional
    public BorrowRecordResponseDTO returnEquipment(Long recordId) {
        EquipmentBorrowRecord record = borrowRecordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("借用记录不存在"));

        if (record.getStatus() == EquipmentBorrowRecord.BorrowStatus.RETURNED) {
            throw new IllegalStateException("设备已归还");
        }

        record.setReturnTime(LocalDateTime.now());
        record.setStatus(EquipmentBorrowRecord.BorrowStatus.RETURNED);

        Equipment equipment = record.getEquipment();
        equipmentService.updateEquipmentStatus(equipment.getId(), Equipment.EquipmentStatus.AVAILABLE);

        EquipmentBorrowRecord updatedRecord = borrowRecordRepository.save(record);
        return convertToResponseDTO(updatedRecord);
    }

    @Override
    public List<BorrowRecordResponseDTO> getBorrowRecordsByEquipment(Long equipmentId) {
        return borrowRecordRepository.findByEquipmentId(equipmentId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowRecordResponseDTO> getBorrowRecordsByStaff(Long staffId) {
        return borrowRecordRepository.findByStaffId(staffId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowRecordResponseDTO> getActiveBorrowRecords() {
        return borrowRecordRepository.findByStatus(EquipmentBorrowRecord.BorrowStatus.ACTIVE).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isEquipmentBorrowed(Long equipmentId) {
        return borrowRecordRepository.existsByEquipmentIdAndStatus(
                equipmentId, EquipmentBorrowRecord.BorrowStatus.ACTIVE);
    }

    private BorrowRecordResponseDTO convertToResponseDTO(EquipmentBorrowRecord record) {
        BorrowRecordResponseDTO dto = new BorrowRecordResponseDTO();
        dto.setId(record.getId());
        dto.setEquipment(EquipmentDTO.fromEntity(record.getEquipment()));
        dto.setStaff(staffProfileService.convertToResponseDTO(record.getStaff()));
        dto.setBorrowTime(record.getBorrowTime());
        dto.setReturnTime(record.getReturnTime());
        dto.setPurpose(record.getPurpose());
        dto.setStatus(record.getStatus().toString());
        return dto;
    }
}