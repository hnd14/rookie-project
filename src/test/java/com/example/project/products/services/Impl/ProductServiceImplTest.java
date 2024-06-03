package com.example.project.products.services.Impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.products.entities.Product;
import com.example.project.products.exceptions.ProductNotFoundException;
import com.example.project.products.repositories.ProductRepository;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@Transactional
public class ProductServiceImplTest {
    @Autowired
    private ProductRepository repository;
    @Autowired
    private ProductServiceImpl service;
    private Product productInDB;
    
    @BeforeAll
    void setUp(){
        repository.deleteAll();
    }

    @BeforeEach
    void setUpDB(){
        productInDB = new Product();
        productInDB.setName("product");
        productInDB.setSalePrice(5000.0);
        productInDB.setIsFeatured(true);
        repository.save(productInDB);
    }

    @AfterEach
    void tearDownDB(){
        repository.deleteAll();
    }

    @Test
    void testFindProductWithFilter() {
        
    }

    @Test
    void testGetProductById_WhenPassedInIncorrectId_ShouldThrow() {
        // set up
        var expected = repository.findAll().getFirst();
        // run
        var exceptions = assertThrows(ProductNotFoundException.class, ()->{service.getProductById(expected.getId() - 1);}) ;
        // assert
        assertThat(exceptions.getMessage()).isEqualTo("Product not found!");
    }

    @Test
    void testGetProductById_WhenPassedInCorrectId_ShouldRetrunTheProduct() {
        // set up
        var expected = productInDB;
        // run
        var result = service.getProductById(expected.getId());
        // assert
        assertThat(result).isEqualTo(expected);
    }
}
