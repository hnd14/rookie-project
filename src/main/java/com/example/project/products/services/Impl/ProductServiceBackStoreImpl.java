package com.example.project.products.services.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.products.dto.Requests.PostNewProductDto;
import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.dto.Requests.UpdateProductDto;
import com.example.project.products.dto.Responses.ProductAdminDto;
import com.example.project.products.entities.Category;
import com.example.project.products.entities.Product;
import com.example.project.products.entities.ProductCategory;
import com.example.project.products.exceptions.CategoryNotFoundException;
import com.example.project.products.exceptions.ProductNotFoundException;
import com.example.project.products.mapper.ProductMapper;
import com.example.project.products.repositories.CategoryRepository;
import com.example.project.products.repositories.ProductCategoryRepository;
import com.example.project.products.repositories.ProductRepository;
import com.example.project.products.services.ProductService;
import com.example.project.products.services.ProductServiceBackStore;


@Service
@Transactional(readOnly = true)
public class ProductServiceBackStoreImpl implements ProductServiceBackStore {
    @Autowired
    ProductRepository repo;
    @Autowired
    ProductMapper mapper;
    @Autowired
    ProductService productService;
    @Autowired
    ProductCategoryRepository productCategoryRepository;
    @Autowired
    CategoryRepository categoryRepository;
    
    @Override
    @Transactional
    public ProductAdminDto createNewProduct(PostNewProductDto dto) {
        Product newProduct = mapper.toNewProduct(dto);
        addCategoriesToProduct(newProduct, dto.categoriesId());
        repo.save(newProduct);
        return mapper.toStaffDto(newProduct);
    }
    @Override
    public ProductAdminDto getProductById(Long id) {
        return repo.findById(id).map(mapper::toStaffDto).orElseThrow(ProductNotFoundException::new);
    }
    @Override
    public List<ProductAdminDto> findProductWithFilter(ProductSearchDto dto) {
        return productService.findProductWithFilter(dto).stream().map(mapper::toStaffDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductAdminDto updateProduct(Long id, UpdateProductDto dto) {
        Product productToUpdate = repo.findById(id).orElseThrow(ProductNotFoundException::new);
        productToUpdate.setDesc(dto.desc() == null?productToUpdate.getDesc():dto.desc());
        productToUpdate.setSalePrice(dto.salePrice() == null?productToUpdate.getSalePrice():dto.salePrice());
        productToUpdate.setStock(dto.stock() == null?productToUpdate.getStock():dto.stock());
        addCategoriesToProduct(productToUpdate, dto.categoriesId());
        return mapper.toStaffDto(productToUpdate);
    }

    @Transactional
    private void addCategoriesToProduct(Product product, List<Long> categoriesId){
        categoriesId.stream().forEach(id -> {
            Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
            ProductCategory productCategory = new ProductCategory(null,product, category);
            productCategoryRepository.saveAndFlush(productCategory);
        });
    }

}
