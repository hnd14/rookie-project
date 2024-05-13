package com.example.project.products.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.entities.Product;

@Service
public interface ProductService {
    Product getProductById(Long id);
    List<Product> findProductWithFilter(ProductSearchDto dto);
}
