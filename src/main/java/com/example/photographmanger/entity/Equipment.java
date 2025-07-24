package com.example.photographmanger.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "equipment")
@Data
public class Equipment {
    public enum EquipmentStatus {
        AVAILABLE, IN_MAINTENANCE, ASSIGNED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 确保使用IDENTITY策略
    private Long id;  // 设备ID，主键自增

    @Column(nullable = false, length = 100)
    private String name;  // 设备名称，支持中文、符号、英文

    @Column(nullable = false)
    private LocalDate purchaseDate;  // 购买日期，非空，手动填入

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EquipmentStatus status = EquipmentStatus.AVAILABLE;  // 设备状态

    @PrePersist
    @PreUpdate
    private void validate() {
        if (purchaseDate.isAfter(LocalDate.now())) {
            throw new IllegalStateException("购买日期不能是未来日期");
        }
    }
}