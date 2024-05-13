package com.example.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.dto.Responses.ProductCustomerDto;
import com.example.project.products.services.ProductServiceStore;

@RestController
@RequestMapping("/store")
public class StoreFrontController {
    @Autowired
    ProductServiceStore productService;
    
    @GetMapping("/products/{id}")
    ProductCustomerDto getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @GetMapping("/products")
    @ResponseBody
    List<ProductCustomerDto> findProductsWithFilter(ProductSearchDto dto){
        return productService.findProductWithFilter(dto);
    }
}
