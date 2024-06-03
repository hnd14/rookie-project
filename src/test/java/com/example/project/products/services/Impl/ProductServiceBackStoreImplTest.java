package com.example.project.products.services.Impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.example.project.products.dto.Requests.PostNewProductDto;
import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.dto.Requests.UpdateProductDto;
import com.example.project.products.dto.Responses.ProductAdminDto;
import com.example.project.products.entities.Product;
import com.example.project.products.exceptions.ProductNotFoundException;
import com.example.project.products.mapper.CategoryMapperImpl;
import com.example.project.products.mapper.ProductMapperImpl;
import com.example.project.products.repositories.CategoryRepository;
import com.example.project.products.repositories.ProductCategoryRepository;
import com.example.project.products.repositories.ProductRepository;
import com.example.project.products.services.ProductService;
import com.example.project.ratings.repositories.RatingRepository;
import com.example.project.util.dto.response.PagedDto;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { ProductMapperImpl.class, ProductServiceBackStoreImpl.class, CategoryMapperImpl.class })
public class ProductServiceBackStoreImplTest {
    @MockBean
    private ProductRepository mockProductRepository;
    @MockBean
    private ProductService mockProductService;
    @MockBean
    private RatingRepository mockRatingRepository;
    @MockBean
    private ProductCategoryRepository productCategoryRepository;
    @MockBean
    private CategoryRepository categoryRepository;
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
        lenient().when(mockProductRepository.save(any())).thenAnswer((invocation) -> {
            Product newProd = (Product) invocation.getArgument(0);
            if (newProd.getName() == null || newProd.getName().equals("")) {
                throw new MethodArgumentNotValidException(null, null);
            }
            if (newProd.getId() == null) {
                newProd.setId((long) 1);
            }

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
        assertThat(result).usingRecursiveComparison().ignoringFields("id")
                .isEqualTo(mapper.toStaffDto(newProduct));
        assertThat(result).extracting("id").isNotNull();
    }

    @Test
    void testCreateProduct_whenNullName_ShouldThrow() {
        // set up
        var newPostDto = new PostNewProductDto(null, "", (long) 0, 500.0, List.of(), true);

        // run assert
        assertThrows(IllegalArgumentException.class, () -> {
            service.createNewProduct(newPostDto);
        });
    }

    @Test
    void testCreateProduct_whenEmptyName_ShouldThrow() {
        // set up
        var newPostDto = new PostNewProductDto("", "", (long) 0, 500.0, List.of(), true);
        // run assert
        assertThrows(IllegalArgumentException.class, () -> {
            service.createNewProduct(newPostDto);
        });
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
                .hasFieldOrPropertyWithValue("isFeatured", productInDB.getIsFeatured())
                .hasFieldOrProperty("createdTime").hasFieldOrProperty("createdBy")
                .hasFieldOrProperty("updatedTime")
                .hasFieldOrProperty("updatedBy");
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

    private PagedDto<ProductAdminDto> convertToPagedDto(Page<Product> result) {
        var content = result.getContent().stream().map(
                mapper::toStaffDto).collect(Collectors.toList());
        return new PagedDto<>(content, result.getTotalPages(), result.getNumber());
    }

    @Test
    void testDelete_whenPassId_shouldDeleteItemFromRepository() {
        // run
        service.deleteProduct((long) 20);
        // assert
        verify(mockProductRepository, times(1)).deleteById((long) 20);
    }

    @Test
    void testUpdate() {
        // set up
        var updatedDesc = "desc";
        var updatedStock = Long.valueOf(15);
        var updatedPrice = 100.0;
        var updatedIsFeatured = true;
        var updateCategoriesId = new ArrayList<Long>();
        var updateDto = new UpdateProductDto(updatedDesc, updatedStock, updatedPrice, updateCategoriesId,
                updatedIsFeatured);
        // run
        var result = service.updateProduct((long) 20, updateDto);

        // assert
        assertThat(result).hasFieldOrPropertyWithValue("id", Long.valueOf(20))
                .hasFieldOrPropertyWithValue("name", productInDB.getName())
                .hasFieldOrPropertyWithValue("desc", updatedDesc)
                .hasFieldOrPropertyWithValue("salePrice", updatedPrice)
                .hasFieldOrPropertyWithValue("isFeatured", updatedIsFeatured);
        verify(mockProductRepository, times(1)).deleteById((long) 20);
    }
}
