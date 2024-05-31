package com.example.project.image.services.Impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.project.image.dto.response.ImageUploadResponse;
import com.example.project.image.entities.Image;
import com.example.project.image.repositories.ImageRepository;
import com.example.project.image.services.ImageService;
import com.example.project.products.exceptions.ProductNotFoundException;
import com.example.project.products.repositories.ProductRepository;
import com.example.project.util.functions.UtilFunctions;

@Service
@Transactional(readOnly = true)
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageRepository repository;
    @Autowired
    private ProductRepository productRepository;
    private String imagePath ="C:/Users/dnhoa/Image/";

    @Transactional
    @Override
    public List<ImageUploadResponse> uploadImages(MultipartFile[] images, Long productId){
        var product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        var response = Arrays.asList(images).stream().map((multipartFile)->{
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            try {
                String filecode = UtilFunctions.saveFile(imagePath, fileName, multipartFile);
                String fileUrl = filecode + "-" + fileName;
                Image thisImage = new Image(fileUrl, product);
                repository.save(thisImage);    
                return new ImageUploadResponse(fileName, true, fileUrl);
            } catch (Exception e) {
                return new ImageUploadResponse(fileName, false, null);
            }              
        }).collect(Collectors.toList());
        return response;
    }

    @Transactional
    @Override
    public ImageUploadResponse uploadThumbnail(MultipartFile image, Long productId){
        var product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        String fileName = StringUtils.cleanPath(image.getOriginalFilename());
        try {
            String filecode = UtilFunctions.saveFile(imagePath, fileName, image);
            String fileUrl = filecode + "-" + fileName;
            product.setThumbnailUrl(fileUrl);
            productRepository.save(product);
            return new ImageUploadResponse(fileName, true, fileUrl);
        } catch (Exception e) {
            return new ImageUploadResponse(fileName, false, null);
        }
           
        }
}
