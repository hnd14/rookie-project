package com.example.project.products.services;

import org.springframework.stereotype.Service;

import com.example.project.products.dto.Requests.PostNewProductDto;
import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.dto.Requests.UpdateProductDto;
import com.example.project.products.dto.Responses.PagedDto;
import com.example.project.products.dto.Responses.ProductAdminDto;

@Service
public interface ProductServiceBackStore {
    ProductAdminDto createNewProduct(PostNewProductDto dto);
    ProductAdminDto getProductById(Long id);
    ProductAdminDto updateProduct(Long id, UpdateProductDto dto);
    PagedDto<ProductAdminDto> findProductWithFilter(ProductSearchDto dto);
    void deleteProduct(Long id);
}

