package com.example.photographmanger.service.impl;

import com.example.photographmanger.dto.EquipmentDTO;
import com.example.photographmanger.entity.Equipment;
import com.example.photographmanger.entity.Equipment.EquipmentStatus;
import com.example.photographmanger.exception.ResourceNotFoundException;
import com.example.photographmanger.repository.EquipmentRepository;
import com.example.photographmanger.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;

    @Override
    public Equipment getEquipmentEntity(Long id) {
        return equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("设备不存在"));
    }

    @Override
    public void updateEquipmentStatus(Long id, EquipmentStatus status) {
        Equipment equipment = getEquipmentEntity(id);
        equipment.setStatus(status);
        equipmentRepository.save(equipment);
    }

    @Override
    @Transactional
    public EquipmentDTO createEquipment(EquipmentDTO equipmentDTO) {
        Equipment equipment = equipmentDTO.toEntity();
        Equipment savedEquipment = equipmentRepository.save(equipment);
        return EquipmentDTO.fromEntity(savedEquipment);
    }

    @Override
    public EquipmentDTO getEquipmentById(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found with id: " + id));
        return EquipmentDTO.fromEntity(equipment);
    }

    @Override
    public List<EquipmentDTO> getAllEquipment() {
        return equipmentRepository.findAll().stream()
                .map(EquipmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EquipmentDTO updateEquipment(Long id, EquipmentDTO equipmentDTO) {
        Equipment existingEquipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found with id: " + id));

        existingEquipment.setName(equipmentDTO.getName());
        existingEquipment.setPurchaseDate(equipmentDTO.getPurchaseDate());
        existingEquipment.setStatus(equipmentDTO.getStatus());

        Equipment updatedEquipment = equipmentRepository.save(existingEquipment);
        return EquipmentDTO.fromEntity(updatedEquipment);
    }

    @Override
    @Transactional
    public void deleteEquipment(Long id) {
        equipmentRepository.deleteById(id);
    }

    @Override
    public List<EquipmentDTO> findByNameContaining(String name) {
        return equipmentRepository.findByNameContaining(name).stream()
                .map(EquipmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<EquipmentDTO> findByPurchaseDateBetween(LocalDate startDate, LocalDate endDate) {
        return equipmentRepository.findByPurchaseDateBetween(startDate, endDate).stream()
                .map(EquipmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<EquipmentDTO> findByStatus(EquipmentStatus status) {
        return equipmentRepository.findByStatus(status).stream()
                .map(EquipmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EquipmentDTO changeEquipmentStatus(Long id, EquipmentStatus newStatus) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found with id: " + id));

        equipment.setStatus(newStatus);
        Equipment updatedEquipment = equipmentRepository.save(equipment);
        return EquipmentDTO.fromEntity(updatedEquipment);
    }
}