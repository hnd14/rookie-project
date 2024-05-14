package com.example.project.products.services.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.products.dto.Requests.PostNewProductDto;
import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.dto.Requests.UpdateProductDto;
import com.example.project.products.dto.Responses.ProductAdminDto;
import com.example.project.products.entities.Product;
import com.example.project.products.exceptions.ProductNotFoundException;
import com.example.project.products.mapper.ProductMapper;
import com.example.project.products.repositories.ProductRepository;
import com.example.project.products.services.ProductService;
import com.example.project.products.services.ProductServiceBackStore;


@Service
@Transactional(readOnly = true)
public class ProductServiceBackStoreImpl implements ProductServiceBackStore {
    @Autowired
    ProductRepository repo;
    @Autowired
    ProductMapper mapper;
    @Autowired
    ProductService productService;
    
    @Override
    @Transactional
    public ProductAdminDto createNewProduct(PostNewProductDto dto) {
        Product newProduct = mapper.toNewProduct(dto);
        repo.save(newProduct);
        return mapper.toStaffDto(newProduct);
    }
    @Override
    public ProductAdminDto getProductById(Long id) {
        return repo.findById(id).map(mapper::toStaffDto).orElseThrow(ProductNotFoundException::new);
    }
    @Override
    public List<ProductAdminDto> findProductWithFilter(ProductSearchDto dto) {
        return productService.findProductWithFilter(dto).stream().map(mapper::toStaffDto).collect(Collectors.toList());
    }
    @Override
    public ProductAdminDto updateProduct(Long id, UpdateProductDto dto) {
        Product productToUpdate = repo.findById(id).orElseThrow(ProductNotFoundException::new);
        productToUpdate.setDesc(dto.desc() == null?productToUpdate.getDesc():dto.desc());
        productToUpdate.setSalePrice(dto.salePrice() == null?productToUpdate.getSalePrice():dto.salePrice());
        productToUpdate.setStock(dto.stock() == null?productToUpdate.getStock():dto.stock());
        return mapper.toStaffDto(productToUpdate);
    }

}
