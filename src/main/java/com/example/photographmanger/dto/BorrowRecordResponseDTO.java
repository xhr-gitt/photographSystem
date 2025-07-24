package com.example.photographmanger.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BorrowRecordResponseDTO {
    private Long id;
    private EquipmentDTO equipment;
    private StaffResponseDTO staff;
    private LocalDateTime borrowTime;
    private LocalDateTime returnTime;
    private String purpose;
    private String status;
}