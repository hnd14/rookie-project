package com.example.project.products.services;


import org.springframework.stereotype.Service;

import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.dto.Responses.PagedDto;
import com.example.project.products.dto.Responses.ProductCustomerDto;

@Service
public interface ProductServiceStore {
    ProductCustomerDto getProductById(Long id);
    PagedDto<ProductCustomerDto> findProductWithFilter(ProductSearchDto dto);
}
