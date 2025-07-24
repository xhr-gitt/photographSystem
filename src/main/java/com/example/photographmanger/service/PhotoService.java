package com.example.photographmanger.service;

import com.example.photographmanger.entity.Photo;
import java.util.List;

public interface PhotoService {
    Photo savePhoto(String category, String fileUrl);
    List<Photo> getPhotosByCategory(String category);
}