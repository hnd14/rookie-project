package com.example.project.products.services.Impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.example.project.products.dto.Requests.PostNewProductDto;
import com.example.project.products.entities.Product;
import com.example.project.products.mapper.ProductMapperImpl;
import com.example.project.products.repositories.ProductRepository;
import com.example.project.products.services.ProductService;
import com.example.project.ratings.repositories.RatingRepository;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { ProductMapperImpl.class, ProductServiceBackStoreImpl.class })
public class ProductServiceBackStoreImplTest {
    @MockBean
    private ProductRepository mockProductRepository;
    @MockBean
    private ProductService mockProductService;
    @MockBean
    private RatingRepository mockRatingRepository;
    @Autowired
    private ProductServiceBackStoreImpl service;
    @Autowired
    private ProductMapperImpl mapper;

    private Product productInDB;

    private Page<Product> returnPage;

    @BeforeEach
    void setUpMock() {
        productInDB = new Product();
        productInDB.setId(Long.valueOf(20));
        productInDB.setName("Product");
        productInDB.setStock(Long.valueOf(0));
        productInDB.setSalePrice(500.0);
        productInDB.setIsFeatured(false);
        productInDB.setThumbnailUrl("thumbnail");
        productInDB.setCategories(new ArrayList<>());
        productInDB.setImages(new ArrayList<>());
        returnPage = new PageImpl<>(List.of(productInDB));

        lenient().when(mockProductRepository.findById(Long.valueOf(20))).thenReturn(Optional.of(productInDB));
        lenient().when(mockProductRepository.findById(Long.valueOf(15))).thenReturn(Optional.empty());
        lenient().when(mockRatingRepository.findAverageRatingForProduct(Long.valueOf(20))).thenReturn(5.0);
        lenient().when(mockProductService.findProductWithFilter(any())).thenReturn(returnPage);
        when(mockProductRepository.save(any())).thenAnswer((invocation) -> {
            Product newProd = (Product) invocation.getArgument(0);
            if (newProd.getName() == null || newProd.getName().equals("")) {
                throw new MethodArgumentNotValidException(null, null);
            }
            newProd.setId((long) 1);
            return newProd;
        });
    }

    @Test
    void testCreateProduct_whenGivenCorrectInformation_ShouldReturnCorrectDto() {
        // set up
        var newPostDto = new PostNewProductDto("new product", "", (long) 0, 500.0, List.of(), true);
        var newProduct = mapper.toNewProduct(newPostDto);

        // run
        var result = service.createNewProduct(newPostDto);
        // assert
        assertThat(result).isEqualTo(mapper.toStaffDto(newProduct));
    }

    @Test
    void testCreateProduct_whenNullName_ShouldThrow() {
        // set up
        var newPostDto = new PostNewProductDto(null, "", (long) 0, 500.0, List.of(), true);

        // run assert
        assertThrows(MethodArgumentNotValidException.class, () -> {
            service.createNewProduct(newPostDto);
        });
    }

    @Test
    void testCreateProduct_whenEmptyName_ShouldThrow() {
        // set up
        var newPostDto = new PostNewProductDto("", "", (long) 0, 500.0, List.of(), true);

        // run assert
        assertThrows(MethodArgumentNotValidException.class, () -> {
            service.createNewProduct(newPostDto);
        });
    }
}
