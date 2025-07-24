package com.example.photographmanger.repository;

import com.example.photographmanger.entity.Equipment;
import com.example.photographmanger.entity.Equipment.EquipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    List<Equipment> findByNameContaining(String name);
    List<Equipment> findByPurchaseDateBetween(LocalDate startDate, LocalDate endDate);
    List<Equipment> findByStatus(EquipmentStatus status);
}