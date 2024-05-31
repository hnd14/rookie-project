package com.example.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.products.dto.Requests.PostNewCategoryDto;
import com.example.project.products.dto.Requests.PostNewProductDto;
import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.dto.Requests.UpdateCategoryDto;
import com.example.project.products.dto.Requests.UpdateProductDto;
import com.example.project.products.dto.Responses.CategoryAdminDto;
import com.example.project.products.dto.Responses.ProductAdminDto;
import com.example.project.products.dto.Responses.ProductDetailsAdminDto;
import com.example.project.products.services.CategoryServiceBackStore;
import com.example.project.products.services.ProductServiceBackStore;
import com.example.project.users.dto.requests.CreateNewAdminDto;
import com.example.project.users.dto.responses.UserReturnDto;
import com.example.project.users.services.UserService;
import com.example.project.util.dto.requests.PagingDto;
import com.example.project.util.dto.response.PagedDto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/store-back")
@AllArgsConstructor
public class StoreBackController extends V1Rest{
    @Autowired
    private final ProductServiceBackStore productService; 
    @Autowired
    private final CategoryServiceBackStore categoryService;
    @Autowired
    private final UserService userService;

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductAdminDto createNewProduct(@RequestBody @Valid PostNewProductDto dto){
        return productService.createNewProduct(dto);
    }

    @GetMapping("/products/{id}")
    public ProductDetailsAdminDto getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @GetMapping("/products")
    @ResponseBody
    public PagedDto<ProductAdminDto> getProducts(ProductSearchDto dto){
        return productService.findProductWithFilter(dto);
    }

    @PutMapping("/products/{id}")
    public ProductAdminDto updateProduct(@PathVariable Long id, @RequestBody UpdateProductDto dto){
        return productService.updateProduct(id, dto);
    }

    @DeleteMapping("/products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryAdminDto createNewCategory(@Valid @RequestBody PostNewCategoryDto dto){
        return categoryService.createNew(dto);
    }

    @GetMapping("/categories")
    @ResponseBody
    public PagedDto<CategoryAdminDto> findAllCategories(PagingDto dto){
        return categoryService.findAllCategories(dto);
    }

    @DeleteMapping("/categories/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
    }

    @PutMapping("/categories/{id}")
    public CategoryAdminDto updateCategory(@PathVariable Long id, @RequestBody UpdateCategoryDto dto){
        return categoryService.update(id, dto);
    }

    @PostMapping("/admins")
    @ResponseStatus(HttpStatus.CREATED)
    public UserReturnDto createAdmin(@RequestBody CreateNewAdminDto dto){
        return userService.createNewAdminUser(dto);
    }

    @GetMapping("/users/{id}")
    public UserReturnDto getUserById (@PathVariable Long id){
        return userService.getUserById(id);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }
}
