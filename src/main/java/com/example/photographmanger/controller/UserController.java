// UserController.java
package com.example.photographmanger.controller;

import com.example.photographmanger.dto.PageResultDTO;
import com.example.photographmanger.dto.UserCreateDTO;
import com.example.photographmanger.dto.UserResponseDTO;
import com.example.photographmanger.dto.UserUpdateDTO;
import com.example.photographmanger.entity.User;
import com.example.photographmanger.repository.UserRepository;
import com.example.photographmanger.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import cn.hutool.core.bean.BeanUtil;

import javax.annotation.security.PermitAll;
import java.util.List;
import java.util.Optional;

@Tag(name = "用户管理")
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String getProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        return currentUser.getUsername();
    }

    @GetMapping("/text")
    public String testtext() {
        return "text test";
    }

    @PostMapping("users")
    @Operation(summary = "用户添加")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> dataCreate(@Validated @RequestBody UserCreateDTO dto) {
        User user = new User();
        BeanUtil.copyProperties(dto, user);
        userService.createOne(user);
        return ResponseEntity.ok("新增成功");
    }

    // 1. 用户新增
    @PostMapping("zhuceusers")
    @PermitAll
    @Operation(summary = "用户注册")
    public ResponseEntity<Object> zhuceCreate(@Validated @RequestBody UserCreateDTO dto) {
        User user = new User();
        BeanUtil.copyProperties(dto, user);
        userService.createOne(user);
        return ResponseEntity.ok("新增成功");
    }

    // 2. 查询所有用户
    @GetMapping("users")
    @Operation(summary = "获取所有用户")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    // 3. 根据ID查询用户
    @GetMapping("users/{id}")
    @Operation(summary = "根据ID获取用户")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 4. 更新用户信息
    @PutMapping("users/{id}")
    @Operation(summary = "更新用户信息")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @Validated @RequestBody UserUpdateDTO dto) {
        User user = new User();
        BeanUtil.copyProperties(dto, user);
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    // 5. 删除用户
    @DeleteMapping("users/{id}")
    @Operation(summary = "删除用户")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("用户删除成功");
    }
    // 6. 批量删除用户
    @DeleteMapping("users/batch")
    @Operation(summary = "批量删除用户")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUsers(@RequestBody List<Long> ids) {
        userService.deleteUsers(ids);
        return ResponseEntity.ok("批量删除用户成功");
    }
    //7分页
    @GetMapping("/users/page")
    @Operation(summary = "分页查询用户")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResultDTO<User>> getUsersByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order) {

        // 构建分页参数
        Pageable pageable;
        if (sortBy != null && order != null) {
            Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(direction, sortBy));
        } else {
            pageable = PageRequest.of(pageNum - 1, pageSize);
        }

        // 调用服务层获取分页数据
        Page<User> userPage = userService.getList(Specification.where(null), pageable);

        // 转换为自定义的分页结果DTO
        PageResultDTO<User> result = new PageResultDTO<>();
        result.setNumber(pageNum);
        result.setSize(pageSize);
        result.setTotalElements(userPage.getTotalElements());
        result.setTotalPages(userPage.getTotalPages());
        result.setContent(userPage.getContent());

        return ResponseEntity.ok(result);
    }
    @GetMapping("/by-role/{role}")
    @Operation(summary = "根据角色查询用户")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getUsersByRole(@PathVariable String role) {
        return ResponseEntity.ok(userService.findUsersByRole(role));
    }

    @GetMapping("/non-admin")
    @Operation(summary = "查询非ADMIN/USER用户")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getNonAdminUsers() {
        return ResponseEntity.ok(userService.findNonAdminUsers());
    }
}