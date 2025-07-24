package com.example.photographmanger.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class AppointmentCreateDTO {
    @NotNull(message = "Staff ID cannot be null")
    private Long staffId;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Appointment time cannot be null")
    private LocalDateTime appointmentTime;  // 新增
}