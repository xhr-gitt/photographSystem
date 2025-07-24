// AppointmentController.java
package com.example.photographmanger.controller;

import com.example.photographmanger.dto.AppointmentCreateDTO;
import com.example.photographmanger.dto.AppointmentUpdateDTO;
import com.example.photographmanger.dto.PageResultDTO;
import com.example.photographmanger.entity.Appointment;
import com.example.photographmanger.service.AppointmentService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import cn.hutool.core.bean.BeanUtil;
import java.util.List;
import java.util.Optional;

@Tag(name = "预约管理")
@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    @Operation(summary = "创建预约")
    public ResponseEntity<Object> createAppointment(@Validated @RequestBody AppointmentCreateDTO dto) {
        Appointment appointment = new Appointment();
        BeanUtil.copyProperties(dto, appointment);
        appointmentService.createOne(appointment);
        return ResponseEntity.ok("预约创建成功");
    }

    @GetMapping
    @Operation(summary = "获取所有预约")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.findAll();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取预约")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        Optional<Appointment> appointment = appointmentService.findById(id);
        return appointment.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新预约信息")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Appointment> updateAppointment(
            @PathVariable Long id,
            @Validated @RequestBody AppointmentUpdateDTO dto) {
        Appointment appointment = new Appointment();
        BeanUtil.copyProperties(dto, appointment);
        Appointment updatedAppointment = appointmentService.updateAppointment(id, appointment);
        return ResponseEntity.ok(updatedAppointment);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除预约")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok("预约删除成功");
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除预约")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAppointments(@RequestBody List<Long> ids) {
        appointmentService.deleteAppointments(ids);
        return ResponseEntity.ok("批量删除预约成功");
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询预约")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResultDTO<Appointment>> getAppointmentsByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order) {

        Pageable pageable;
        if (sortBy != null && order != null) {
            Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(direction, sortBy));
        } else {
            pageable = PageRequest.of(pageNum - 1, pageSize);
        }

        Page<Appointment> appointmentPage = appointmentService.getList(Specification.where(null), pageable);

        PageResultDTO<Appointment> result = new PageResultDTO<>();
        result.setNumber(pageNum);
        result.setSize(pageSize);
        result.setTotalElements(appointmentPage.getTotalElements());
        result.setTotalPages(appointmentPage.getTotalPages());
        result.setContent(appointmentPage.getContent());

        return ResponseEntity.ok(result);
    }
    // AppointmentController.java
    @GetMapping("/user/{userId}")
    @Operation(summary = "根据用户ID获取所有预约")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Appointment>> getAppointmentsByUserId(@PathVariable Long userId) {
        List<Appointment> appointments = appointmentService.findByUserId(userId);
        if (appointments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(appointments);
    }
}