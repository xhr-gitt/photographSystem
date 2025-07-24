package com.example.photographmanger.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Data
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    @Column(name = "staff_id")
    private Long staffId;

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "created_time")
    protected LocalDateTime createdTime;
    @Column(name = "appointment_time", nullable = false)
    private LocalDateTime appointmentTime;  // 新增的预约时间字段
    @PrePersist
    protected void onCreate() {
        this.createdTime=LocalDateTime.now();
    }
}

