package com.example.project.image.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.project.image.dto.response.ImageUploadResponse;
import com.example.project.image.services.ImageService;

@RestController
@RequestMapping("/store-back/images")
public class ImageController {
    @Autowired
    private ImageService imageService;

    // @PostMapping("/upload")
    ResponseEntity<List<ImageUploadResponse>> uploadFile(
            @RequestParam("file") MultipartFile[] multipartFiles, @RequestParam("productId") Long productId)
            throws IOException {
        var response = imageService.uploadImages(multipartFiles, productId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // @PostMapping("/thumbnails/upload")
    ResponseEntity<ImageUploadResponse> uploadThumnail(
            @RequestParam("file") MultipartFile multipartFile, @RequestParam("productId") Long productId)
            throws IOException {
        var response = imageService.uploadThumbnail(multipartFile, productId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/upload")
    ResponseEntity<List<ImageUploadResponse>> uploadThumbnailAndImages(
            @RequestParam("file") MultipartFile[] multipartFiles,
            @RequestParam("thumbnail") MultipartFile thumbnail,
            @RequestParam("productId") Long productId,
            @RequestParam("deleteImages") String deleteImage)
            throws IOException {
        var response = imageService.updateImagesData(multipartFiles, thumbnail, productId, deleteImage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
