package com.example.project.products.services;

import org.springframework.stereotype.Service;

import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.entities.Product;

@Service
public interface ProductService {
    Product getProductById(Long id);
    Product findProductWithFilter(ProductSearchDto dto);
}
