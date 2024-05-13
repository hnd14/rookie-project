package com.example.project.products.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.project.products.dto.Requests.PostNewProductDto;
import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.dto.Responses.ProductSellerDto;

@Service
public interface ProductServiceBackStore {
    ProductSellerDto createNewProduct(PostNewProductDto dto);
    ProductSellerDto getProductById(Long id);
    List<ProductSellerDto> findProductWithFilter(ProductSearchDto dto);
}

