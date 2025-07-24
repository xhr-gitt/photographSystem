// AppointmentService.java
package com.example.photographmanger.service;

import com.example.photographmanger.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    Appointment createOne(Appointment appointment);
    List<Appointment> findAll();
    Optional<Appointment> findById(Long id);
    Appointment updateAppointment(Long id, Appointment appointment);
    void deleteAppointment(Long id);
    void deleteAppointments(List<Long> ids);
    Page<Appointment> getList(Specification<Appointment> spec, Pageable pageable);
    List<Appointment> findByUserId(Long userId); // 修改返回类型为List
}