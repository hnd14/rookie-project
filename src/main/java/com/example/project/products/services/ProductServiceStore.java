package com.example.project.products.services;

import org.springframework.stereotype.Service;

import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.dto.Responses.ProductCustomerDto;
import com.example.project.products.dto.Responses.ProductThumnailDto;
import com.example.project.util.dto.response.PagedDto;

@Service
public interface ProductServiceStore {
    ProductCustomerDto getProductById(Long id);

    PagedDto<ProductThumnailDto> findProductWithFilter(ProductSearchDto dto);
}
