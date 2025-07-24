package com.example.photographmanger.service.impl;
import com.example.photographmanger.dto.UserResponseDTO;
import com.example.photographmanger.entity.User;
import com.example.photographmanger.repository.UserRepository;
import com.example.photographmanger.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository dao;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository dao, PasswordEncoder passwordEncoder) {
        this.dao = dao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createOne(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return dao.save(user);
    }

    @Override
    public List<User> findAll() {
        return dao.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return dao.findById(id);
    }

    @Override
    public User updateUser(Long id, User user) {
        return dao.findById(id)
                .map(existingUser -> {
                    if (user.getUsername() != null) {
                        existingUser.setUsername(user.getUsername());
                    }
                    if (user.getPassword() != null) {
                        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
                    }
                    if (user.getRealName() != null) {
                        existingUser.setRealName(user.getRealName());
                    }
                    if (user.getPhone() != null) {
                        existingUser.setPhone(user.getPhone());
                    }
                    if (user.getRole() != null) {
                        existingUser.setRole(user.getRole());
                    }
                    existingUser.setEnabled(user.isEnabled());
                    return dao.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public void deleteUser(Long id) {
        dao.deleteById(id);
    }
    @Override
    public void deleteUsers(List<Long> ids) {
        // 批量删除用户
        dao.deleteAllById(ids);
    }
    @Override
    public Page<User> getList(Specification<User> spec, Pageable pageable) {
        return dao.findAll(spec, pageable);
    }
    @Override
    public List<UserResponseDTO> findUsersByRole(String role) {
        return dao.findByRole(role).stream()
                .map(user -> {
                    UserResponseDTO dto = new UserResponseDTO();
                    dto.setUsername(user.getUsername());
                    dto.setRealName(user.getRealName());
                    dto.setRole(user.getRole());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDTO> findNonAdminUsers() {
        return dao.findByRoleNotIn(Arrays.asList("ADMIN", "USER")).stream()
                .map(user -> {
                    UserResponseDTO dto = new UserResponseDTO();
                    dto.setUsername(user.getUsername());
                    dto.setRealName(user.getRealName());
                    dto.setRole(user.getRole());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}