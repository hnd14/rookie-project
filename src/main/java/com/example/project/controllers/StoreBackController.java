package com.example.project.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.products.dto.Requests.PostNewCategoryDto;
import com.example.project.products.dto.Requests.PostNewProductDto;
import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.dto.Requests.UpdateProductDto;
import com.example.project.products.dto.Responses.CategoryDto;
import com.example.project.products.dto.Responses.ProductAdminDto;
import com.example.project.products.services.CategoryServiceBackStore;
import com.example.project.products.services.ProductServiceBackStore;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/store-back")
@AllArgsConstructor
public class StoreBackController {
    @Autowired
    private final ProductServiceBackStore productService; 
    @Autowired
    private final CategoryServiceBackStore categoryService;

    @PostMapping("/products")
    public ProductAdminDto createNewProduct(@RequestBody PostNewProductDto dto){
        return productService.createNewProduct(dto);
    }

    @GetMapping("/products/{id}")
    public ProductAdminDto getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @GetMapping("/products")
    @ResponseBody
    public List<ProductAdminDto> getProducts(ProductSearchDto dto){
        return productService.findProductWithFilter(dto);
    }

    @PostMapping("/categories")
    public CategoryDto createNewCategory(@RequestBody PostNewCategoryDto dto){
        return categoryService.createNew(dto);
    }

    @GetMapping("/categories")
    public List<CategoryDto> findCategoriesWithPartialName(@RequestParam(value = "name", required = false, defaultValue = "") String name){
        return categoryService.findCategoryByName(name);
    }

    @PutMapping("/products/{id}")
    public ProductAdminDto updateProduct(@PathVariable Long id, @RequestBody UpdateProductDto dto){
        return productService.updateProduct(id, dto);
    }
}
