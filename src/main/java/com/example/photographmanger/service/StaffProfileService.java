package com.example.photographmanger.service;

import com.example.photographmanger.dto.StaffCreateDTO;
import com.example.photographmanger.dto.StaffResponseDTO;

import com.example.photographmanger.dto.StaffUpdateDTO;
import com.example.photographmanger.entity.StaffProfile;

import java.util.List;
import java.util.Optional;

public interface StaffProfileService {
    StaffProfile createStaff(StaffCreateDTO dto);
    Optional<StaffProfile> findByUsername(String username);
    List<StaffResponseDTO> syncAndGetAllStaff();
    // 新增方法
   StaffProfile getStaffEntity(Long id);
    StaffResponseDTO convertToResponseDTO(StaffProfile staff);
    StaffProfile updateStaff(String username, StaffUpdateDTO dto);
}