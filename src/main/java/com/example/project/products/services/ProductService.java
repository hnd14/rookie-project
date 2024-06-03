package com.example.project.products.services;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.entities.Product;

@Service
public interface ProductService {
    Product getProductById(Long id);

    Page<Product> findProductWithFilter(ProductSearchDto dto);
}
