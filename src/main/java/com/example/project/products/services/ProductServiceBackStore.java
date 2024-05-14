package com.example.project.products.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.project.products.dto.Requests.PostNewProductDto;
import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.dto.Requests.UpdateProductDto;
import com.example.project.products.dto.Responses.ProductAdminDto;

@Service
public interface ProductServiceBackStore {
    ProductAdminDto createNewProduct(PostNewProductDto dto);
    ProductAdminDto getProductById(Long id);
    ProductAdminDto updateProduct(Long id, UpdateProductDto dto);
    List<ProductAdminDto> findProductWithFilter(ProductSearchDto dto);
}

