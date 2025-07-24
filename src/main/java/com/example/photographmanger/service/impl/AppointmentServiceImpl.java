// AppointmentServiceImpl.java
package com.example.photographmanger.service.impl;

import com.example.photographmanger.entity.Appointment;
import com.example.photographmanger.repository.AppointmentRepository;
import com.example.photographmanger.service.AppointmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository dao;

    public AppointmentServiceImpl(AppointmentRepository dao) {
        this.dao = dao;
    }

    @Override
    public Appointment createOne(Appointment appointment) {
        return dao.save(appointment);
    }

    @Override
    public List<Appointment> findAll() {
        return dao.findAll();
    }

    @Override
    public Optional<Appointment> findById(Long id) {
        return dao.findById(id);
    }

    @Override
    public Appointment updateAppointment(Long id, Appointment appointment) {
        return dao.findById(id)
                .map(existingAppointment -> {
                    if (appointment.getStaffId() != null) {
                        existingAppointment.setStaffId(appointment.getStaffId());
                    }
                    if (appointment.getUserId() != null) {
                        existingAppointment.setUserId(appointment.getUserId());
                    }
                    if (appointment.getAppointmentTime() != null) {
                        existingAppointment.setAppointmentTime(appointment.getAppointmentTime());
                    }
                    return dao.save(existingAppointment);
                })
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
    }

    @Override
    public void deleteAppointment(Long id) {
        dao.deleteById(id);
    }

    @Override
    public void deleteAppointments(List<Long> ids) {
        dao.deleteAllById(ids);
    }

    @Override
    public Page<Appointment> getList(Specification<Appointment> spec, Pageable pageable) {
        return dao.findAll(spec, pageable);
    }
    @Override
    public List<Appointment> findByUserId(Long userId) {
        return dao.findByUserId(userId); // 使用新增的Repository方法
    }

}