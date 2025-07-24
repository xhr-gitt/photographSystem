package com.example.photographmanger.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentUpdateDTO {
    private Long staffId;
    private Long userId;
    private LocalDateTime appointmentTime;  // 新增
}