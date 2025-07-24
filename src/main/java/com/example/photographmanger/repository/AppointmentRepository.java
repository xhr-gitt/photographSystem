// AppointmentRepository.java
package com.example.photographmanger.repository;

import com.example.photographmanger.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepositoryImplementation<Appointment, Long> {
    List<Appointment> findByUserId(Long userId); // 新增按userId查询方法
}