package com.example.project.products.services;

import org.springframework.stereotype.Service;

import com.example.project.products.dto.Requests.PostNewProductDto;
import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.dto.Requests.UpdateProductDto;
import com.example.project.products.dto.Responses.ProductAdminDto;
import com.example.project.products.dto.Responses.ProductDetailsAdminDto;
import com.example.project.util.dto.response.PagedDto;

@Service
public interface ProductServiceBackStore {
    ProductAdminDto createNewProduct(PostNewProductDto dto);

    ProductDetailsAdminDto getProductById(Long id);

    ProductAdminDto updateProduct(Long id, UpdateProductDto dto);

    PagedDto<ProductAdminDto> findProductWithFilter(ProductSearchDto dto);

    void deleteProduct(Long id);
}
