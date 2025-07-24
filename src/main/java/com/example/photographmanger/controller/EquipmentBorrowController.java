package com.example.photographmanger.controller;

import com.example.photographmanger.dto.BorrowRecordDTO;
import com.example.photographmanger.dto.BorrowRecordResponseDTO;
import com.example.photographmanger.service.EquipmentBorrowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "设备借用管理")
@RestController
@RequestMapping("/api/equipment-borrow")
@RequiredArgsConstructor
public class EquipmentBorrowController {

    private final EquipmentBorrowService equipmentBorrowService;

    @PostMapping("/borrow")
    @Operation(summary = "借用设备")
    @PreAuthorize("hasAnyRole('ADMIN', 'PHOTOGRAPHER')")
    public ResponseEntity<BorrowRecordResponseDTO> borrowEquipment(
            @RequestBody BorrowRecordDTO borrowRecordDTO) {
        return ResponseEntity.ok(equipmentBorrowService.borrowEquipment(borrowRecordDTO));
    }

    @PostMapping("/return/{recordId}")
    @Operation(summary = "归还设备")
    @PreAuthorize("hasAnyRole('ADMIN', 'PHOTOGRAPHER')")
    public ResponseEntity<BorrowRecordResponseDTO> returnEquipment(
            @PathVariable Long recordId) {
        return ResponseEntity.ok(equipmentBorrowService.returnEquipment(recordId));
    }

    @GetMapping("/equipment/{equipmentId}")
    @Operation(summary = "获取设备的借用记录")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BorrowRecordResponseDTO>> getBorrowRecordsByEquipment(
            @PathVariable Long equipmentId) {
        return ResponseEntity.ok(equipmentBorrowService.getBorrowRecordsByEquipment(equipmentId));
    }

    @GetMapping("/staff/{staffId}")
    @Operation(summary = "获取员工的借用记录")
    @PreAuthorize("hasAnyRole('ADMIN', 'PHOTOGRAPHER')")
    public ResponseEntity<List<BorrowRecordResponseDTO>> getBorrowRecordsByStaff(
            @PathVariable Long staffId) {
        return ResponseEntity.ok(equipmentBorrowService.getBorrowRecordsByStaff(staffId));
    }

    @GetMapping("/active")
    @Operation(summary = "获取所有未归还的借用记录")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BorrowRecordResponseDTO>> getActiveBorrowRecords() {
        return ResponseEntity.ok(equipmentBorrowService.getActiveBorrowRecords());
    }

    @GetMapping("/is-borrowed/{equipmentId}")
    @Operation(summary = "检查设备是否被借用")
    public ResponseEntity<Boolean> isEquipmentBorrowed(@PathVariable Long equipmentId) {
        return ResponseEntity.ok(equipmentBorrowService.isEquipmentBorrowed(equipmentId));
    }
}