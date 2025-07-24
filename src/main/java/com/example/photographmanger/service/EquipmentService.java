package com.example.photographmanger.service;

import com.example.photographmanger.dto.EquipmentDTO;
import com.example.photographmanger.entity.Equipment;
import com.example.photographmanger.entity.Equipment.EquipmentStatus;

import java.time.LocalDate;
import java.util.List;

public interface EquipmentService {
    EquipmentDTO createEquipment(EquipmentDTO equipmentDTO);
    EquipmentDTO getEquipmentById(Long id);
    List<EquipmentDTO> getAllEquipment();
    EquipmentDTO updateEquipment(Long id, EquipmentDTO equipmentDTO);
    void deleteEquipment(Long id);
    List<EquipmentDTO> findByNameContaining(String name);
    List<EquipmentDTO> findByPurchaseDateBetween(LocalDate startDate, LocalDate endDate);
    List<EquipmentDTO> findByStatus(EquipmentStatus status);
    EquipmentDTO changeEquipmentStatus(Long id, EquipmentStatus newStatus);

    // 新增方法
    Equipment getEquipmentEntity(Long id);
    void updateEquipmentStatus(Long id, EquipmentStatus status);
}