package com.example.photographmanger.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "equipment_borrow_records")
@Data
public class EquipmentBorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false)
    private StaffProfile staff;

    @Column(nullable = false)
    private LocalDateTime borrowTime;

    private LocalDateTime returnTime;

    @Column(length = 500)
    private String purpose;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BorrowStatus status = BorrowStatus.ACTIVE;

    public enum BorrowStatus {
        ACTIVE, RETURNED, OVERDUE
    }

    @PrePersist
    protected void onCreate() {
        borrowTime = LocalDateTime.now();
    }
}