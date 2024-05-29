package com.example.project.products.services.Impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.project.products.dto.Requests.PostNewProductDto;
import com.example.project.products.dto.Responses.ProductAdminDto;
import com.example.project.products.dto.Responses.ProductDetailsAdminDto;


@SpringBootTest
@ActiveProfiles("test")
public class ProductServiceBackStoreImplTestIntegration {
    @Autowired
    private ProductServiceBackStoreImpl service;
    private PostNewProductDto postDto = new PostNewProductDto("test", null, null, null, null,null);
    @Test
    private void createNewProductTest_whenCreatingSuccessful(){
        String expectedProductName = "test";
        //do 
        ProductAdminDto result = service.createNewProduct(postDto);
        ProductDetailsAdminDto postedProduct = service.getProductById(result.id());

        //assert
        assertEquals(expectedProductName, postedProduct.getName());
    }
}
