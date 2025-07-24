package com.example.photographmanger.dto;

import com.example.photographmanger.entity.Equipment;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class EquipmentDTO {

    private Long id;
    @NotBlank(message = "设备名不能为空")
    private String name;
    private LocalDate purchaseDate;
    private Equipment.EquipmentStatus status;

    // 从实体转换到DTO
    public static EquipmentDTO fromEntity(Equipment equipment) {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setId(equipment.getId());
        dto.setName(equipment.getName());
        dto.setPurchaseDate(equipment.getPurchaseDate());
        dto.setStatus(equipment.getStatus());
        return dto;
    }

    // 从DTO转换到实体
    public Equipment toEntity() {
        Equipment equipment = new Equipment();
        // 移除ID设置，让数据库自动生成
        equipment.setName(this.getName());
        equipment.setPurchaseDate(this.getPurchaseDate());
        equipment.setStatus(this.getStatus());
        return equipment;
    }
}