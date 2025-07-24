package com.example.photographmanger.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "staff_profiles")
@Data
public class StaffProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "role", nullable = false, length = 20)
    private String role;  // ADMIN, PHOTOGRAPHER, CUSTOMER_SERVICE

    @Column(name = "staff_information", columnDefinition = "TEXT")
    private String staffInformation; // 员工详细信息
    @Column(name = "real_name", length = 50) // 新增realName字段
    private String realName;
}