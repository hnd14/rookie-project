package com.example.project.products.services.Impl;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.dto.Responses.ProductCustomerDto;
import com.example.project.products.exceptions.ProductNotFoundException;
import com.example.project.products.mapper.ProductMapper;
import com.example.project.products.repositories.ProductRepository;
import com.example.project.products.services.ProductService;
import com.example.project.products.services.ProductServiceStore;

@Service
@Transactional(readOnly = true)
public class ProductServiceStoreImpl implements ProductServiceStore{
    @Autowired
    private ProductRepository repo;
    @Autowired
    private ProductMapper mapper;
    @Autowired
    private ProductService productService;

    public ProductCustomerDto getProductById(Long id){
        return repo.findById(id).map((product)->{
            var res = mapper.toCustomerDto(product);
            res.setCategories(product.getCategories()
                                    .stream()
                                    .map((category)->category.getCategory().getName()) 
                                    .collect(Collectors.toList()));
            return res;
        }).orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public List<ProductCustomerDto> findProductWithFilter(ProductSearchDto dto) {
        return productService.findProductWithFilter(dto).stream().map(mapper::toCustomerDto).collect(Collectors.toList());
    }


}
