package com.example.photographmanger.repository;


import com.example.photographmanger.dto.StaffUpdateDTO;
import com.example.photographmanger.entity.StaffProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffProfileRepository extends JpaRepository<StaffProfile, Long> {
    StaffProfile findByUserId(Long userId);
    StaffProfile findByUsername(String username);
    boolean existsByUserId(Long userId);

    //StaffProfile updateStaff(String username, StaffUpdateDTO dto);
}