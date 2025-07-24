package com.example.photographmanger.service.impl;

import com.example.photographmanger.dto.StaffCreateDTO;
import com.example.photographmanger.dto.StaffResponseDTO;

import com.example.photographmanger.dto.StaffUpdateDTO;
import com.example.photographmanger.dto.UserResponseDTO;
import com.example.photographmanger.entity.StaffProfile;
import com.example.photographmanger.entity.User;
import com.example.photographmanger.exception.ResourceNotFoundException;
import com.example.photographmanger.repository.StaffProfileRepository;
import com.example.photographmanger.repository.UserRepository;
import com.example.photographmanger.service.StaffProfileService;
import com.example.photographmanger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StaffProfileServiceImpl implements StaffProfileService {
    private final StaffProfileRepository staffProfileRepository;
    private final UserRepository userRepository;

    public StaffProfileServiceImpl(StaffProfileRepository staffProfileRepository,
                                   UserRepository userRepository) {
        this.staffProfileRepository = staffProfileRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public StaffProfile createStaff(StaffCreateDTO dto) {
        // 先查找对应的用户
        User user = (User) userRepository.findByUsername(dto.getUsername());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 检查是否已经存在员工档案
        if (staffProfileRepository.existsByUserId(user.getId())) {
            throw new RuntimeException("该用户已有员工档案");
        }

        // 创建员工档案
        StaffProfile staffProfile = new StaffProfile();
        staffProfile.setUserId(user.getId());
        staffProfile.setUsername(dto.getUsername());
        staffProfile.setRole(dto.getRole());
        staffProfile.setRealName(dto.getRealName());

        return staffProfileRepository.save(staffProfile);
    }

    @Override
    public Optional<StaffProfile> findByUsername(String username) {
        return Optional.ofNullable(staffProfileRepository.findByUsername(username));
    }
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public List<StaffResponseDTO> syncAndGetAllStaff() {
        // 1. 获取User表中非ADMIN/USER用户
        List<UserResponseDTO> nonAdminUsers = userService.findNonAdminUsers();

        // 2. 遍历这些用户，检查是否在StaffProfile表中存在
        for (UserResponseDTO user : nonAdminUsers) {
            // 如果StaffProfile表中不存在该用户，则创建
            if (staffProfileRepository.findByUsername(user.getUsername()) == null) {
                StaffCreateDTO createDTO = new StaffCreateDTO();
                createDTO.setUsername(user.getUsername());
                createDTO.setRealName(user.getRealName());
                createDTO.setRole(user.getRole());
                this.createStaff(createDTO);
            }
        }

        // 3. 返回StaffProfile表中所有员工信息
        return staffProfileRepository.findAll().stream()
                .map(staff -> {
                    StaffResponseDTO dto = new StaffResponseDTO();
                    dto.setUsername(staff.getUsername());
                    dto.setRealName(staff.getRealName());
                    dto.setRole(staff.getRole());
                    dto.setStaffInformation(staff.getStaffInformation());
                    return dto;
                })
                .collect(Collectors.toList());
    }
    @Override
    public StaffProfile getStaffEntity(Long id) {
        return staffProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("员工不存在"));
    }

    @Override
    public StaffResponseDTO convertToResponseDTO(StaffProfile staff) {
        StaffResponseDTO dto = new StaffResponseDTO();
        dto.setUsername(staff.getUsername());
        dto.setRealName(staff.getRealName());
        dto.setRole(staff.getRole());
        dto.setStaffInformation(staff.getStaffInformation());
        return dto;
    }
    @Override
    @Transactional
    public StaffProfile updateStaff(String username, StaffUpdateDTO dto) {
        StaffProfile staffProfile = staffProfileRepository.findByUsername(username);
        if (staffProfile == null) {
            throw new RuntimeException("员工档案不存在");
        }

        // 更新所有可修改字段
        if (dto.getRole() != null) {
            staffProfile.setRole(dto.getRole());
        }
        if (dto.getRealName() != null) {
            staffProfile.setRealName(dto.getRealName());
        }
        if (dto.getStaffInformation() != null) {
            staffProfile.setStaffInformation(dto.getStaffInformation());
        }
        if (dto.getUsername() != null && !dto.getUsername().equals(username)) {
            // 检查新用户名是否已存在
            if (staffProfileRepository.findByUsername(dto.getUsername()) != null) {
                throw new RuntimeException("用户名已存在");
            }
            staffProfile.setUsername(dto.getUsername());
        }
        return staffProfileRepository.save(staffProfile);
    }
}
