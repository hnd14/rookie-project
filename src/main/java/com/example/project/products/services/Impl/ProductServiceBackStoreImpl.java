package com.example.project.products.services.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.products.dto.Requests.PostNewProductDto;
import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.dto.Requests.UpdateProductDto;
import com.example.project.products.dto.Responses.ProductAdminDto;
import com.example.project.products.dto.Responses.ProductDetailsAdminDto;
import com.example.project.products.entities.Category;
import com.example.project.products.entities.Product;
import com.example.project.products.entities.ProductCategory;
import com.example.project.products.exceptions.CategoryNotFoundException;
import com.example.project.products.exceptions.ProductNotFoundException;
import com.example.project.products.mapper.CategoryMapper;
import com.example.project.products.mapper.ProductMapper;
import com.example.project.products.repositories.CategoryRepository;
import com.example.project.products.repositories.ProductCategoryRepository;
import com.example.project.products.repositories.ProductRepository;
import com.example.project.products.services.ProductService;
import com.example.project.products.services.ProductServiceBackStore;
import com.example.project.util.dto.response.PagedDto;
import com.example.project.util.functions.UtilFunctions;

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
    @Autowired
    CategoryMapper categoryMapper;
    @Value("${app.image.folder}")
    private String imagePath = "C:/Users/dnhoa/Image/";

    @Override
    @Transactional
    public ProductAdminDto createNewProduct(PostNewProductDto dto) {
        Product newProduct = mapper.toNewProduct(dto);
        newProduct.getAvgRating().setAvgRating(0.0);
        repo.save(newProduct);
        addCategoriesToProduct(newProduct, dto.categoriesId());
        return mapper.toStaffDto(newProduct);
    }

    @Override
    public ProductDetailsAdminDto getProductById(Long id) {
        Product product = repo.findById(id).orElseThrow(ProductNotFoundException::new);
        ProductDetailsAdminDto result = mapper.toDetailsAdminDto(product);
        result.setCategoriesInfo(
                product.getCategories().stream().map(categoryMapper::toSimpleCategoryDto).collect(Collectors.toList()));
        result.setImagesUrl(product.getImages().stream().map((image) -> image.getUrl()).collect(Collectors.toList()));
        return result;
    }

    @Override
    public PagedDto<ProductAdminDto> findProductWithFilter(ProductSearchDto dto) {
        var content = productService.findProductWithFilter(dto);
        return new PagedDto<>(content.getContent().stream().map(mapper::toStaffDto).collect(Collectors.toList()),
                content.getTotalPages(),
                content.getNumber());
    }

    @Override
    @Transactional
    public ProductAdminDto updateProduct(Long id, UpdateProductDto dto) {
        Product productToUpdate = repo.findById(id).orElseThrow(ProductNotFoundException::new);
        productToUpdate.setDesc(dto.desc() == null ? productToUpdate.getDesc() : dto.desc());
        productToUpdate.setSalePrice(dto.salePrice() == null ? productToUpdate.getSalePrice() : dto.salePrice());
        productToUpdate.setStock(dto.stock() == null ? productToUpdate.getStock() : dto.stock());
        productToUpdate.setIsFeatured(dto.isFeatured() == null ? productToUpdate.getIsFeatured() : dto.isFeatured());
        productToUpdate.getCategories().stream().forEach((categories) -> {
            productCategoryRepository.delete(categories);
        });
        addCategoriesToProduct(productToUpdate, dto.categoriesId());
        repo.save(productToUpdate);
        return mapper.toStaffDto(productToUpdate);
    }

    @Transactional
    private void addCategoriesToProduct(Product product, List<Long> categoriesId) {
        if (categoriesId == null)
            return;
        categoriesId.stream().forEach(id -> {
            Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
            if (productCategoryRepository.findOneByProductAndCategory(product, category).isPresent()) {
                return;
            }
            ProductCategory productCategory = new ProductCategory(null, product, category);
            productCategoryRepository.saveAndFlush(productCategory);
        });
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = repo.findById(id).orElse(new Product());
        var images = product.getImages();
        repo.deleteById(id);
        images.stream().forEach((image) -> {
            try {
                UtilFunctions.deleteFile(imagePath, image.getUrl());
            } catch (Exception e) {
                // TODO: handle exception
            }
        });
    }

}
