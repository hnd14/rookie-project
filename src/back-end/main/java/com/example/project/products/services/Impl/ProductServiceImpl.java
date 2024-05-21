package com.example.project.products.services.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.entities.Product;
import com.example.project.products.exceptions.ProductNotFoundException;
import com.example.project.products.mapper.ProductMapper;
import com.example.project.products.repositories.ProductRepository;
import com.example.project.products.services.ProductService;

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService{
    @Autowired
    ProductRepository repo;
    @Autowired
    ProductMapper mapper;


    @Override
    public Product getProductById(Long id) {
        return repo.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public List<Product> findProductWithFilter(ProductSearchDto dto) {
        String productName = dto.name() == null?"":dto.name();
        Double minPrice = dto.minPrice();
        Double maxPrice = dto.maxPrice();
        Long categoryId = dto.categoriesId();
        Specification<Product> specification = (root, query, cb) ->{
            var namePredicate = cb.like(cb.lower(root.get("name")), "%"+productName.toLowerCase()+"%");
            var minPricePredicate = minPrice == null?cb.conjunction():cb.greaterThan(root.get("salePrice"), minPrice);
            var maxPricePredicate = maxPrice == null?cb.conjunction():cb.lessThan(root.get("salePrice"), maxPrice);
            var productCategory = root.join("categories");
            var categoryPredicate = categoryId == null?cb.conjunction():cb.equal(productCategory.get("category").get("id"), categoryId);
            return cb.and(namePredicate, maxPricePredicate, minPricePredicate, categoryPredicate);
        };
        return repo.findAll(specification);
    }

}
