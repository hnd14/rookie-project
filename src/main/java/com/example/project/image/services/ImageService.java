package com.example.project.image.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.project.image.dto.response.ImageUploadResponse;

public interface ImageService {

    List<ImageUploadResponse> uploadImages(MultipartFile[] images, Long productId);
    ImageUploadResponse uploadThumbnail(MultipartFile image, Long productId);
    List<ImageUploadResponse> uploadThumbnailAndImages(MultipartFile[] images, MultipartFile thumbnail, Long productId);

}