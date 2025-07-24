// UserService.java
package com.example.photographmanger.service;

import com.example.photographmanger.dto.UserResponseDTO;
import com.example.photographmanger.entity.Appointment;
import com.example.photographmanger.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createOne(User user);
    List<User> findAll();
    Optional<User> findById(Long id);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    void deleteUsers(List<Long> ids); // 新增批量删除方法
    Page<User> getList(Specification<User> spec, Pageable pageable);
    List<UserResponseDTO> findUsersByRole(String role);
    List<UserResponseDTO> findNonAdminUsers();

}