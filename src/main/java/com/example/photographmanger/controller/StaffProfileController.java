package com.example.photographmanger.controller;

import com.example.photographmanger.dto.StaffCreateDTO;
import com.example.photographmanger.dto.StaffResponseDTO;
//import com.example.photographmanger.dto.StaffUpdateDTO;
import com.example.photographmanger.dto.StaffUpdateDTO;
import com.example.photographmanger.entity.StaffProfile;
import com.example.photographmanger.service.StaffProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "员工档案管理")
@RestController
@RequestMapping("/api/staff")
public class StaffProfileController {
    private final StaffProfileService staffProfileService;

    public StaffProfileController(StaffProfileService staffProfileService) {
        this.staffProfileService = staffProfileService;
    }

    @PostMapping
    @Operation(summary = "创建员工档案")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StaffProfile> createStaff(@Validated @RequestBody StaffCreateDTO dto) {
        return ResponseEntity.ok(staffProfileService.createStaff(dto));
    }

    @GetMapping("/{username}")
    @Operation(summary = "根据用户名查询员工档案")
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StaffProfile> getStaffByUsername(@PathVariable String username) {
        return staffProfileService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/sync-and-list")
    @Operation(summary = "同步并获取所有员工信息")
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StaffResponseDTO>> syncAndGetAllStaff() {
        return ResponseEntity.ok(staffProfileService.syncAndGetAllStaff());
    }
    @PutMapping("/{username}")
    @Operation(summary = "更新员工档案信息")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StaffProfile> updateStaff(
            @PathVariable String username,
            @Validated @RequestBody StaffUpdateDTO dto) {
        return ResponseEntity.ok(staffProfileService.updateStaff(username, dto));
    }
}