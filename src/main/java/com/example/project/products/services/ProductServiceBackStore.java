package com.example.project.products.services;

import org.springframework.stereotype.Service;

import com.example.project.products.dto.Requests.PostNewProductDto;
import com.example.project.products.dto.Responses.ProductSellerDto;

@Service
public interface ProductServiceBackStore {
    ProductSellerDto createNewProduct(PostNewProductDto dto);
}
