package com.example.project.products.services.Impl;

import com.example.project.products.dto.Responses.ProductCustomerDto;
import com.example.project.products.exceptions.ProductNotFoundException;
import com.example.project.products.mapper.ProductMapper;
import com.example.project.products.repositories.ProductRepository;
import com.example.project.products.services.ProductServiceStore;

public class ProductServiceStoreImpl implements ProductServiceStore{
    private ProductRepository repo;
    private ProductMapper mapper;

    public ProductCustomerDto getProductById(Long id){
        return repo.findById(id).map(mapper::toCustomerDto).orElseThrow(ProductNotFoundException::new);
    }
}
