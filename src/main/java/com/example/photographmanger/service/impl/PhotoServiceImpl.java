package com.example.photographmanger.service.impl;

import com.example.photographmanger.entity.Photo;
import com.example.photographmanger.repository.PhotoRepository;
import com.example.photographmanger.service.PhotoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotoServiceImpl implements PhotoService {
    private final PhotoRepository photoRepository;

    public PhotoServiceImpl(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Override
    public Photo savePhoto(String category, String fileUrl) {
        Photo photo = new Photo();
        photo.setCategory(category);
        photo.setFileUrl(fileUrl);
        return photoRepository.save(photo);
    }

    @Override
    public List<Photo> getPhotosByCategory(String category) {
        return photoRepository.findByCategory(category);
    }
}