package com.example.project.products.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.dto.Responses.ProductCustomerDto;

@Service
public interface ProductServiceStore {
    ProductCustomerDto getProductById(Long id);
    List<ProductCustomerDto> findProductWithFilter(ProductSearchDto dto);
}
