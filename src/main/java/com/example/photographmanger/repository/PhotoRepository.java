package com.example.photographmanger.repository;

import com.example.photographmanger.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByCategory(String category);
}