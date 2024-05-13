package com.example.project.products.services.Impl;


import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;


import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.dto.Responses.ProductCustomerDto;
import com.example.project.products.entities.Product;
import com.example.project.products.entities.ProductCategory;
import com.example.project.products.exceptions.ProductNotFoundException;
import com.example.project.products.mapper.ProductMapper;
import com.example.project.products.repositories.ProductRepository;
import com.example.project.products.services.ProductServiceStore;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

@Service
public class ProductServiceStoreImpl implements ProductServiceStore{
    private ProductRepository repo;
    private ProductMapper mapper;

    public ProductCustomerDto getProductById(Long id){
        return repo.findById(id).map(mapper::toCustomerDto).orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public List<ProductCustomerDto> findProductWithFilter(ProductSearchDto dto) {
        // TODO Auto-generated method stub
        String productName = dto.name() == null?"":dto.name();
        List<Long> productCategories = List.of(dto.categoriesId());
        Double minPrice = dto.minPrice();
        Double maxPrice = dto.maxPrice();
        Specification<Product> specification = (root, query, cb) ->{
            var namePredicate = cb.like(root.get("name"), productName);
            var minPricePredicate = minPrice == null?null:cb.greaterThan(root.get("price"), minPrice);
            var maxPricePredicate = maxPrice == null?null:cb.lessThan(root.get("price"), maxPrice);
            return cb.and(namePredicate, maxPricePredicate, minPricePredicate);
        };
        // Specification<Product> categoriesSpecification = (root, query, cb) ->{
        //     Join<Product, ProductCategory> productCategory = root.join("categories");
        //     var result = productCategories.stream().map((id)->(cb.)})
        //     return cb.exists(null);
        // };
        return repo.findAll(specification).stream().map(mapper::toCustomerDto).collect(Collectors.toList());
    }


}
