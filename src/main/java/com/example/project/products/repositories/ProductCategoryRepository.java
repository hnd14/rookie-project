package com.example.project.products.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.products.entities.Category;
import com.example.project.products.entities.Product;
import com.example.project.products.entities.ProductCategory;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long>{
    Optional<ProductCategory> findOneByProductAndCategory(Product product, Category category);
}
