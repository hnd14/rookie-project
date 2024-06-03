package com.example.project.products.services.Impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.dto.Responses.ProductThumnailDto;
import com.example.project.products.entities.Product;
import com.example.project.products.exceptions.ProductNotFoundException;
import com.example.project.products.mapper.ProductMapperImpl;
import com.example.project.products.repositories.ProductRepository;
import com.example.project.products.services.ProductService;
import com.example.project.ratings.repositories.RatingRepository;
import com.example.project.util.dto.response.PagedDto;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { ProductMapperImpl.class, ProductServiceStoreImpl.class })
public class ProductServiceStoreImplTest {
    @MockBean
    private ProductRepository mockProductRepository;
    @MockBean
    private ProductService mockProductService;
    @MockBean
    private RatingRepository mockRatingRepository;
    @Autowired
    private ProductServiceStoreImpl service;
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
    }

    @Test
    void testFindById_whenGivenCorrectId_shouldReturnCorrectDto() {
        // set up

        // run
        var result = service.getProductById(Long.valueOf(20));
        // assert
        assertThat(result).hasFieldOrPropertyWithValue("id", Long.valueOf(20)).hasFieldOrPropertyWithValue("name",
                productInDB.getName()).hasFieldOrPropertyWithValue("desc", productInDB.getDesc())
                .hasFieldOrPropertyWithValue("salePrice", productInDB.getSalePrice())
                .hasFieldOrPropertyWithValue("thumbnailUrl", productInDB.getThumbnailUrl())
                .hasFieldOrPropertyWithValue("averageScore", 5.0);
    }

    @Test
    void testFindById_whenWrongId_shouldThrow() {
        // run and assert
        assertThrows(ProductNotFoundException.class, () -> {
            service.getProductById(Long.valueOf(15));
        });
    }

    @Test
    void testFindProductWithFilter_ShouldReturnCorrectDto() {
        // set up
        var expected = convertToPagedDto(returnPage);
        var searchDto = new ProductSearchDto(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty());
        // run
        var result = service.findProductWithFilter(searchDto);
        // assert
        assertThat(result).hasFieldOrPropertyWithValue("content", expected.getContent())
                .hasFieldOrPropertyWithValue("pageCount", expected.getPageCount())
                .hasFieldOrPropertyWithValue("currentPage", expected.getCurrentPage());
    }

    private PagedDto<ProductThumnailDto> convertToPagedDto(Page<Product> result) {
        var content = result.getContent().stream().map(
                mapper::toThumnailDto).collect(Collectors.toList());
        return new PagedDto<>(content, result.getTotalPages(), result.getNumber());
    }

}
