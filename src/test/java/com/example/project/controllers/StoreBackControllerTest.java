package com.example.project.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.project.products.dto.Requests.PostNewCategoryDto;
import com.example.project.products.dto.Requests.PostNewProductDto;
import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.dto.Requests.UpdateCategoryDto;
import com.example.project.products.dto.Requests.UpdateProductDto;
import com.example.project.products.services.CategoryServiceBackStore;
import com.example.project.products.services.ProductServiceBackStore;
import com.example.project.users.dto.requests.CreateNewAdminDto;
import com.example.project.users.services.UserService;
import com.example.project.util.dto.requests.PagingDto;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { StoreBackController.class })
public class StoreBackControllerTest {
    @MockBean
    private ProductServiceBackStore mockProductService;
    @MockBean
    private CategoryServiceBackStore mockCategoryService;
    @MockBean
    private UserService mockUserService;
    @Autowired
    private StoreBackController controller;

    @Test
    void testCreateAdmin() {
        // set up
        var dto = new CreateNewAdminDto("admin", "admin");
        // run
        controller.createAdmin(dto);
        // assert
        verify(mockUserService, times(1)).createNewAdminUser(dto);
    }

    @Test
    void testCreateNewCategory() {
        // set up
        var dto = new PostNewCategoryDto("new category", "new");
        // run
        controller.createNewCategory(dto);
        // assert
        verify(mockCategoryService, times(1)).createNew(dto);
    }

    @Test
    void testCreateNewProduct() {
        // set up
        var dto = new PostNewProductDto("new product", null, Long.valueOf(10), 50.0, null, true);
        // run
        controller.createNewProduct(dto);
        // assert
        verify(mockProductService, times(1)).createNewProduct(dto);
    }

    @Test
    void testDeleteCategory() {
        // run
        controller.deleteCategory((long) 1);
        // assert
        verify(mockCategoryService, times(1)).deleteCategory((long) 1);
    }

    @Test
    void testDeleteProduct() {
        // run
        controller.deleteProduct((long) 1);
        // assert
        verify(mockProductService, times(1)).deleteProduct((long) 1);
    }

    @Test
    void testDeleteUser() {
        // run
        controller.deleteUser((long) 1);
        // assert
        verify(mockUserService, times(1)).deleteUser((long) 1);
    }

    @Test
    void testFindAllCategories() {
        // set up
        var dto = new PagingDto(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        // run
        controller.findAllCategories(dto);
        // assert
        verify(mockCategoryService, times(1)).findAllCategories(dto);
    }

    @Test
    void testGetProductById() {
        // run
        controller.getProductById((long) 1);
        // assert
        verify(mockProductService, times(1)).getProductById((long) 1);
    }

    @Test
    void testGetProducts() {
        // set up
        var searchDto = new ProductSearchDto(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty());
        // run
        controller.getProducts(searchDto);
        // assert
        verify(mockProductService, times(1)).findProductWithFilter(searchDto);
    }

    @Test
    void testGetUserById() {
        // run
        controller.getUserById((long) 1);
        // assert
        verify(mockUserService, times(1)).getUserById((long) 1);
    }

    @Test
    void testUpdateCategory() {
        // set up
        var dto = new UpdateCategoryDto("new desc");
        // run
        controller.updateCategory((long) 1, dto);
        // assert
        verify(mockCategoryService, times(1)).update((long) 1, dto);
    }

    @Test
    void testUpdateProduct() {
        // set up
        var dto = new UpdateProductDto("new desc", Long.valueOf(1), 50.0, null, true);
        // run
        controller.updateProduct((long) 1, dto);
        // assert
        verify(mockProductService, times(1)).updateProduct((long) 1, dto);
    }
}
