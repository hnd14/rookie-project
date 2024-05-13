package com.example.project.products.services.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.products.dto.Requests.PostNewProductDto;
import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.dto.Responses.ProductSellerDto;
import com.example.project.products.entities.Product;
import com.example.project.products.exceptions.ProductNotFoundException;
import com.example.project.products.mapper.ProductMapper;
import com.example.project.products.repositories.ProductRepository;
import com.example.project.products.services.ProductServiceBackStore;

@Service
public class ProductServiceBackStoreImpl implements ProductServiceBackStore {
    @Autowired
    ProductRepository repo;
    @Autowired
    ProductMapper mapper;
    
    @Override
    public ProductSellerDto createNewProduct(PostNewProductDto dto) {
        Product newProduct = mapper.toNewProduct(dto);
        repo.save(newProduct);
        return mapper.toStaffDto(newProduct);
    }
    @Override
    public ProductSellerDto getProductById(Long id) {
        return repo.findById(id).map(mapper::toStaffDto).orElseThrow(ProductNotFoundException::new);
    }
    @Override
    public List<ProductSellerDto> findProductWithFilter(ProductSearchDto dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findProductWithFilter'");
    }

}
