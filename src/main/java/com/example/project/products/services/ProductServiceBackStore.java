package com.example.project.products.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.project.products.dto.Requests.PostNewProductDto;
import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.dto.Responses.ProductAdminDto;

@Service
public interface ProductServiceBackStore {
    ProductAdminDto createNewProduct(PostNewProductDto dto);
    ProductAdminDto getProductById(Long id);
    List<ProductAdminDto> findProductWithFilter(ProductSearchDto dto);
}

