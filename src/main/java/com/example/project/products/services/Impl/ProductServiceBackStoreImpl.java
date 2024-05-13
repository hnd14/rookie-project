package com.example.project.products.services.Impl;

import com.example.project.products.dto.Requests.PostNewProductDto;
import com.example.project.products.dto.Responses.ProductSellerDto;
import com.example.project.products.entities.Product;
import com.example.project.products.mapper.ProductMapper;
import com.example.project.products.repositories.ProductRepository;
import com.example.project.products.services.ProductServiceBackStore;

public class ProductServiceBackStoreImpl implements ProductServiceBackStore {
    ProductRepository repo;
    ProductMapper mapper;
    @Override
    public ProductSellerDto createNewProduct(PostNewProductDto dto) {
        Product newProduct = mapper.toNewProduct(dto);
        repo.save(newProduct);
        return mapper.toStaffDto(newProduct);
    }

}
