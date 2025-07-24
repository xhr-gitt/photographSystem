package com.example.photographmanger.controller;

import com.example.photographmanger.dto.EquipmentDTO;
import com.example.photographmanger.entity.Equipment.EquipmentStatus;
import com.example.photographmanger.service.EquipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 设备管理控制器，提供设备的CRUD操作及相关查询功能
 */
@Tag(name = "设备管理")
@RestController
@RequestMapping("/api/equipment")  // 基础路径为/api/equipment
@RequiredArgsConstructor  // Lombok注解，自动生成构造函数注入依赖
public class EquipmentController {

    private final EquipmentService equipmentService;  // 设备服务层依赖

    /**
     * 创建新设备
     * @param equipmentDTO 设备数据传输对象，包含设备信息
     * @return ResponseEntity<EquipmentDTO> 包含创建成功的设备信息和HTTP状态码
     */
    @Operation(summary = "设备新增")
    @PostMapping("/create")
    public ResponseEntity<EquipmentDTO> createEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        EquipmentDTO createdEquipment = equipmentService.createEquipment(equipmentDTO);
        return ResponseEntity.ok(createdEquipment);  // 返回200 OK和创建的设备信息
    }

    /**
     * 根据ID获取设备详情
     * @param id 设备ID
     * @return ResponseEntity<EquipmentDTO> 包含查询到的设备信息和HTTP状态码
     */
    @Operation(summary = "根据id查找设备")
    @GetMapping("/GetById/{id}")
    public ResponseEntity<EquipmentDTO> getEquipmentById(@PathVariable Long id) {
        EquipmentDTO equipmentDTO = equipmentService.getEquipmentById(id);
        return ResponseEntity.ok(equipmentDTO);  // 返回200 OK和查询到的设备信息
    }

    /**
     * 获取所有设备列表
     * @return ResponseEntity<List<EquipmentDTO>> 包含所有设备列表和HTTP状态码
     */
    @Operation(summary = "查找所有设备")
    @GetMapping("/GetAll")
    public ResponseEntity<List<EquipmentDTO>> getAllEquipment() {
        List<EquipmentDTO> equipmentList = equipmentService.getAllEquipment();
        return ResponseEntity.ok(equipmentList);  // 返回200 OK和设备列表
    }

    /**
     * 更新设备信息
     * @param id 要更新的设备ID
     * @param equipmentDTO 包含更新后的设备信息
     * @return ResponseEntity<EquipmentDTO> 包含更新后的设备信息和HTTP状态码
     */
    @Operation(summary = "通过id更新设备")
    @PutMapping("/updateInformationById/{id}")
    public ResponseEntity<EquipmentDTO> updateEquipment(
            @PathVariable Long id, @RequestBody EquipmentDTO equipmentDTO) {
        EquipmentDTO updatedEquipment = equipmentService.updateEquipment(id, equipmentDTO);
        return ResponseEntity.ok(updatedEquipment);  // 返回200 OK和更新后的设备信息
    }

    /**
     * 删除指定设备
     * @param id 要删除的设备ID
     * @return ResponseEntity<Void> 只返回HTTP状态码，无内容
     */
    @Operation(summary = "通过id删除设备")
    @DeleteMapping("/DeleteById/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
        return ResponseEntity.noContent().build();  // 返回204 No Content
    }

    /**
     * 设备搜索接口，支持按名称、购买日期范围或状态搜索
     * @param name 设备名称(模糊匹配)
     * @param startDate 购买日期范围开始日期
     * @param endDate 购买日期范围结束日期
     * @param status 设备状态
     * @return ResponseEntity<List<EquipmentDTO>> 包含符合条件的设备列表和HTTP状态码
     */
    @Operation(summary = "模糊查找设备")
    @GetMapping("/LikeSearch")
    public ResponseEntity<List<EquipmentDTO>> searchEquipment(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) EquipmentStatus status) {

        if (name != null) {
            // 按名称模糊查询
            return ResponseEntity.ok(equipmentService.findByNameContaining(name));
        } else if (startDate != null && endDate != null) {
            // 按购买日期范围查询
            return ResponseEntity.ok(equipmentService.findByPurchaseDateBetween(startDate, endDate));
        } else if (status != null) {
            // 按状态查询
            return ResponseEntity.ok(equipmentService.findByStatus(status));
        } else {
            // 无查询条件时返回400 Bad Request
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 更新设备状态
     * @param id 要更新的设备ID
     * @param newStatus 新的设备状态
     * @return ResponseEntity<EquipmentDTO> 包含更新后的设备信息和HTTP状态码
     */
    @Operation(summary = "通过id更新设备状态")
    @PatchMapping("updateStatusByid/{id}")
    public ResponseEntity<EquipmentDTO> changeEquipmentStatus(
            @PathVariable Long id, @RequestParam EquipmentStatus newStatus) {
        EquipmentDTO updatedEquipment = equipmentService.changeEquipmentStatus(id, newStatus);
        return ResponseEntity.ok(updatedEquipment);  // 返回200 OK和更新后的设备信息
    }
}