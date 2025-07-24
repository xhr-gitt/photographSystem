package com.example.photographmanger.repository;

import com.example.photographmanger.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepositoryImplementation<User,Long> {
    Object findByUsername(String admin);
    Object findUserIdByUsername(String username);
    boolean existsByUsername(String username);
    List<User> findByRole(String role);

    @Query("SELECT u FROM User u WHERE u.role NOT IN :roles")
    List<User> findByRoleNotIn(List<String> roles);



}