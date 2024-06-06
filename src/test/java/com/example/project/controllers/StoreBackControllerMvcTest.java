package com.example.project.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import com.example.project.products.dto.Requests.PostNewCategoryDto;
import com.example.project.products.dto.Requests.PostNewProductDto;
import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.dto.Requests.UpdateCategoryDto;
import com.example.project.products.dto.Requests.UpdateProductDto;
import com.example.project.products.exceptions.CategoryNotFoundException;
import com.example.project.products.exceptions.ProductNotFoundException;
import com.example.project.products.services.CategoryServiceBackStore;
import com.example.project.products.services.ProductServiceBackStore;
import com.example.project.users.dto.requests.CreateNewAdminDto;
import com.example.project.users.services.UserService;
import com.example.project.util.exceptions.DuplicatedResourceException;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(StoreBackController.class)
public class StoreBackControllerMvcTest {
    @MockBean
    private UserService mockUserService;

    @MockBean
    private CategoryServiceBackStore mockCategoryService;

    @MockBean
    private ProductServiceBackStore mockProductService;

    @Autowired
    private MockMvc mvc;

    @Captor
    ArgumentCaptor<ProductSearchDto> searchDtoCaptor;

    @BeforeEach
    void setUp() {

    }

    @Test
    void testGetProductById_whenReturnProduct_shouldShowCorrectInfo() throws Exception {
        mvc.perform(get("/store-back/products/1"))
                .andExpect(status().isOk());

    }

    @Test
    void testGetProductById_whenProductNotFound_shouldReturnStatus404() throws Exception {
        when(mockProductService.getProductById(2L)).thenThrow(new ProductNotFoundException());
        mvc.perform(get("/store-back/products/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProduct_whenOK_shouldReturnStatus201() throws Exception {
        var inputDto = new PostNewProductDto("products", "", 10L, 10.0, List.of(), true);
        mvc.perform(
                post("/store-back/products").contentType(MediaType.APPLICATION_JSON).content(asJsonString(inputDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateProduct_whenBlankName_shouldReturnStatusCode400() throws Exception {
        var inputDto = new PostNewProductDto("", "", 10L, 10.0, List.of(), true);
        mvc.perform(
                post("/store-back/products").contentType(MediaType.APPLICATION_JSON).content(asJsonString(inputDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateProduct_whenNullName_shouldReturnStatusCode400() throws Exception {
        var inputDto = new PostNewProductDto("", "", 10L, 10.0, List.of(), true);
        mvc.perform(
                post("/store-back/products").contentType(MediaType.APPLICATION_JSON).content(asJsonString(inputDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateProduct_whenSalePriceNegative_shouldReturnStatusCode400() throws Exception {
        var inputDto = new PostNewProductDto("", "", 10L, -10.0, List.of(), true);
        mvc.perform(
                post("/store-back/products").contentType(MediaType.APPLICATION_JSON).content(asJsonString(inputDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateProduct_whenStockNegative_shouldReturnStatusCode400() throws Exception {
        var inputDto = new PostNewProductDto("", "", -10L, 10.0, List.of(), true);
        mvc.perform(
                post("/store-back/products").contentType(MediaType.APPLICATION_JSON).content(asJsonString(inputDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAdmin_whenOK_shouldReturn201() throws Exception {
        var inputDto = new CreateNewAdminDto("admin", "admin");
        mvc.perform(post("/store-back/admins").contentType(MediaType.APPLICATION_JSON).content(asJsonString(inputDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createAdmin_whenEmptyUsername_shouldReturn400() throws Exception {
        var inputDto = new CreateNewAdminDto("admin", "admin");
        mvc.perform(post("/store-back/admins").contentType(MediaType.APPLICATION_JSON).content(asJsonString(inputDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createAdmin_whenEmptyPassword_shouldReturn400() throws Exception {
        var inputDto = new CreateNewAdminDto("admin", "");
        mvc.perform(post("/store-back/admins").contentType(MediaType.APPLICATION_JSON).content(asJsonString(inputDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createAdmin_whenUsernameAlreadyExisted_shouldReturn400() throws Exception {
        var inputDto = new CreateNewAdminDto("admin", "");
        when(mockUserService.createNewAdminUser(inputDto))
                .thenThrow(new DuplicatedResourceException("This username has been used"));
        mvc.perform(post("/store-back/admins").contentType(MediaType.APPLICATION_JSON).content(asJsonString(inputDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateCategories_whenOK_shouldReturn200() throws Exception {
        var inputDto = new PostNewCategoryDto("category", "desc");
        mvc.perform(
                post("/store-back/categories").contentType(MediaType.APPLICATION_JSON).content(asJsonString(inputDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateCategories_whenEmptyName_shouldReturn400() throws Exception {
        var inputDto = new PostNewCategoryDto("", "desc");
        mvc.perform(
                post("/store-back/categories").contentType(MediaType.APPLICATION_JSON).content(asJsonString(inputDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateCategories_whenNullName_shouldReturn400() throws Exception {
        var inputDto = new PostNewCategoryDto(null, "desc");
        mvc.perform(
                post("/store-back/categories").contentType(MediaType.APPLICATION_JSON).content(asJsonString(inputDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateCategories_whenDuplicateName_shouldReturn400() throws Exception {
        var inputDto = new PostNewCategoryDto("category", "desc");
        when(mockCategoryService.createNew(any(PostNewCategoryDto.class))).thenThrow(new DuplicatedResourceException());
        mvc.perform(
                post("/store-back/categories").contentType(MediaType.APPLICATION_JSON).content(asJsonString(inputDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteCategory_whenGivenId_shouldReturn204() throws Exception {
        mvc.perform(delete("/store-back/categories/1")).andExpect(status().isNoContent());
    }

    @Test
    void testDeleteCategory_whenGivenIdNotNumber_shouldReturn400() throws Exception {
        mvc.perform(delete("/store-back/categories/abc")).andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteUser_whenGivenId_shouldReturn204() throws Exception {
        mvc.perform(delete("/store-back/users/1")).andExpect(status().isNoContent());
    }

    @Test
    void testDeleteUser_whenGivenIdNotNumber_shouldReturn400() throws Exception {
        mvc.perform(delete("/store-back/users/abc")).andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteProducts_whenGivenId_shouldReturn204() throws Exception {
        mvc.perform(delete("/store-back/products/1")).andExpect(status().isNoContent());
    }

    @Test
    void testDeleteProducts_whenGivenIdNotNumber_shouldReturn400() throws Exception {
        mvc.perform(delete("/store-back/products/abc")).andExpect(status().isBadRequest());
    }

    @Test
    void testFindAllProducts_whenNotGivenAnyParams_shouldReturn200() throws Exception {
        mvc.perform(get("/store-back/products")).andExpect(status().isOk());
        verify(mockProductService).findProductWithFilter(searchDtoCaptor.capture());

        assertThat(searchDtoCaptor.getValue()).hasFieldOrPropertyWithValue("name", Optional.empty())
                .hasFieldOrPropertyWithValue("categoriesId", Optional.empty())
                .hasFieldOrPropertyWithValue("minPrice", Optional.empty())
                .hasFieldOrPropertyWithValue("maxPrice", Optional.empty())
                .hasFieldOrPropertyWithValue("isFeatured", Optional.empty());
    }

    @Test
    void testFindAllProducts_whenGivenCorrectParams_shouldReturn200() throws Exception {
        mvc.perform(get("/store-back/products?name=abc&&categoriesId=1&&minPrice=10.0&&maxPrice=20.0&&isFeatured=true"))
                .andExpect(status().isOk());
        verify(mockProductService).findProductWithFilter(searchDtoCaptor.capture());

        assertThat(searchDtoCaptor.getValue()).hasFieldOrPropertyWithValue("name", Optional.of("abc"))
                .hasFieldOrPropertyWithValue("categoriesId", Optional.of(1L))
                .hasFieldOrPropertyWithValue("minPrice", Optional.of(10.0))
                .hasFieldOrPropertyWithValue("maxPrice", Optional.of(20.0))
                .hasFieldOrPropertyWithValue("isFeatured", Optional.of(true));
    }

    @Test
    void testFindAllProducts_whenGivenIllegalParams_shouldReturn400() throws Exception {
        when(mockProductService.findProductWithFilter(any())).thenThrow(new IllegalArgumentException());
        mvc.perform(get(
                "/store-back/products?name=abc&&categoriesId=1&&minPrice=10.0&&maxPrice=20.0&&isFeatured=true&&pageSize=0"))
                .andExpect(status().isBadRequest());
        verify(mockProductService).findProductWithFilter(searchDtoCaptor.capture());

        assertThat(searchDtoCaptor.getValue()).hasFieldOrPropertyWithValue("name", Optional.of("abc"))
                .hasFieldOrPropertyWithValue("categoriesId", Optional.of(1L))
                .hasFieldOrPropertyWithValue("minPrice", Optional.of(10.0))
                .hasFieldOrPropertyWithValue("maxPrice", Optional.of(20.0))
                .hasFieldOrPropertyWithValue("isFeatured", Optional.of(true))
                .hasFieldOrPropertyWithValue("pageSize", Optional.of(0));
    }

    @Test
    void testFindAllCategories_whenServiceResponse_shouldReturn200() throws Exception {
        mvc.perform(get("/store-back/categories")).andExpect(status().isOk());
    }

    @Test
    void testFindAllCategories_whenServiceThrowsIllegalArgumentExceptions_shouldReturn400() throws Exception {
        when(mockCategoryService.findAllCategories(any())).thenThrow(new IllegalArgumentException());
        mvc.perform(get("/store-back/categories")).andExpect(status().isBadRequest());
    }

    @Test
    void testGetProductsById_whenGivenNumberId_shouldReturn200() throws Exception {
        mvc.perform(get("/store-back/products/1")).andExpect(status().isOk());
    }

    @Test
    void testGetProductsById_whenGivenNotNumberId_shouldReturn400() throws Exception {
        mvc.perform(get("/store-back/products/abc")).andExpect(status().isBadRequest());
    }

    @Test
    void testGetProductsById_whenGivenServiceThrowProductNotFoundException_shouldReturn404() throws Exception {
        when(mockProductService.getProductById(1L)).thenThrow(new ProductNotFoundException());
        mvc.perform(get("/store-back/products/1")).andExpect(status().isNotFound());
    }

    @Test
    void testUpdateCategory_whenServiceReturn_shouldReturn200() throws Exception {
        var inputDto = new UpdateCategoryDto("abc");
        mvc.perform(
                put("/store-back/categories/1").contentType(MediaType.APPLICATION_JSON).content(asJsonString(inputDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateCategory_whenServiceThrowNotFound_shouldReturn404() throws Exception {
        var inputDto = new UpdateCategoryDto("abc");
        when(mockCategoryService.update(anyLong(), any())).thenThrow(new CategoryNotFoundException());
        mvc.perform(
                put("/store-back/categories/1").contentType(MediaType.APPLICATION_JSON).content(asJsonString(inputDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateCategory_whenNotGivenData_shouldReturn400() throws Exception {
        mvc.perform(
                put("/store-back/categories/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProduct_whenServiceReturn_shouldReturn200() throws Exception {
        var inputDto = new UpdateProductDto("abc", 1L, 10.0, List.of(), null);
        mvc.perform(
                put("/store-back/products/1").contentType(MediaType.APPLICATION_JSON).content(asJsonString(inputDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateProduct_whenServiceThrowNotFound_shouldReturn404() throws Exception {
        var inputDto = new UpdateProductDto("abc", 1L, 10.0, List.of(), null);
        when(mockProductService.updateProduct(anyLong(), any())).thenThrow(new ProductNotFoundException());
        mvc.perform(
                put("/store-back/products/1").contentType(MediaType.APPLICATION_JSON).content(asJsonString(inputDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateProducts_whenNotGivenData_shouldReturn400() throws Exception {
        mvc.perform(
                put("/store-back/products/1"))
                .andExpect(status().isBadRequest());
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
