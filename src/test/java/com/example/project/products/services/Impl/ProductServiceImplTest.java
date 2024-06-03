package com.example.project.products.services.Impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

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

import com.example.project.products.dto.Requests.ProductSearchDto;
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
    private final ProductSearchDto defaultSerch = new ProductSearchDto(Optional.of(""),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.of("name"),
            Optional.of("ASC"),
            Optional.of(10),
            Optional.of(1),
            Optional.empty());

    @BeforeAll
    void setUp() {
        repository.deleteAll();
    }

    @BeforeEach
    void setUpDB() {
        productInDB = new Product();
        productInDB.setName("product");
        productInDB.setSalePrice(5000.0);
        productInDB.setIsFeatured(false);
        repository.save(productInDB);
    }

    @AfterEach
    void tearDownDB() {
        repository.deleteAll();
    }

    @Test
    void testFindProductWithFilter_whenGivenEmpty_shouldUseDefault() {
        // set up
        var emptySearchDto = new ProductSearchDto(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty());

        var expected = service.findProductWithFilter(defaultSerch);
        // run
        var result = service.findProductWithFilter(emptySearchDto);
        // assert
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void testFindAllCategories_whenGivenPageIndex0_shouldThrow() {
        // set up
        var dto = new ProductSearchDto(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(0),
                Optional.empty());
        // run
        assertThrows(IllegalArgumentException.class, () -> {
            service.findProductWithFilter(dto);
        });
    }

    @Test
    void testFindAllCategories_whenGivenPageIndexNegative_shouldThrow() {
        // set up
        var dto = new ProductSearchDto(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(-1),
                Optional.empty());
        // run
        assertThrows(IllegalArgumentException.class, () -> {
            service.findProductWithFilter(dto);
        });
    }

    @Test
    void testFindAllCategories_whenGivenPageSize0_shouldThrow() {
        // set up
        var dto = new ProductSearchDto(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(0), Optional.empty(),
                Optional.empty());
        // run
        assertThrows(IllegalArgumentException.class, () -> {
            service.findProductWithFilter(dto);
        });
    }

    @Test
    void testFindAllCategories_whenGivenFilterName_shouldFindOnlyProductsWhoseNameContainsSearchString() {
        // set up
        var searchSequence = "pro";
        var dto = new ProductSearchDto(Optional.of(searchSequence), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty());
        // run
        var result = service.findProductWithFilter(dto);
        // assert
        assertThat(result.getContent()).allMatch((product) -> {
            return product.getName().contains(searchSequence);
        });

    }

    @Test
    void testFindAllCategories_whenGivenFilterFeatured_shouldFindOnlyFeaturedProducts() {
        // set up
        var dto = new ProductSearchDto(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.of(true));
        // run
        var result = service.findProductWithFilter(dto);
        // assert
        assertThat(result.getContent()).allMatch((product) -> {
            return product.getIsFeatured();
        });

    }

    @Test
    void testFindAllCategories_whenGivenFilterPrice_shouldFindOnlyProductsWhosePriceInRange() {
        // set up
        var minPrice = 500.0;
        var maxPrice = 5000.0;
        var dto = new ProductSearchDto(Optional.empty(), Optional.empty(), Optional.of(minPrice),
                Optional.of(maxPrice), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty());
        // run
        var result = service.findProductWithFilter(dto);
        // assert
        assertThat(result.getContent()).allMatch((product) -> {
            return product.getSalePrice() >= minPrice && product.getSalePrice() <= maxPrice;
        });

    }

    @Test
    void testFindAllCategories_whenGivenPageSizeNegative_shouldThrow() {
        // set up
        var dto = new ProductSearchDto(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(-1), Optional.empty(),
                Optional.empty());
        // run
        assertThrows(IllegalArgumentException.class, () -> {
            service.findProductWithFilter(dto);
        });
    }

    @Test
    void testGetProductById_WhenPassedInIncorrectId_ShouldThrow() {
        // set up
        var expected = repository.findAll().getFirst();
        // run
        var exceptions = assertThrows(ProductNotFoundException.class, () -> {
            service.getProductById(expected.getId() - 1);
        });
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
