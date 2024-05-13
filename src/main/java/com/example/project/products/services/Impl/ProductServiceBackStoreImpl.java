package com.example.project.products.services.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.project.products.dto.Requests.PostNewProductDto;
import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.dto.Responses.ProductSellerDto;
import com.example.project.products.entities.Product;
import com.example.project.products.exceptions.ProductNotFoundException;
import com.example.project.products.mapper.ProductMapper;
import com.example.project.products.repositories.ProductRepository;
import com.example.project.products.services.ProductServiceBackStore;

@Service
public class ProductServiceBackStoreImpl implements ProductServiceBackStore {
    @Autowired
    ProductRepository repo;
    @Autowired
    ProductMapper mapper;
    
    @Override
    public ProductSellerDto createNewProduct(PostNewProductDto dto) {
        Product newProduct = mapper.toNewProduct(dto);
        repo.save(newProduct);
        return mapper.toStaffDto(newProduct);
    }
    @Override
    public ProductSellerDto getProductById(Long id) {
        return repo.findById(id).map(mapper::toStaffDto).orElseThrow(ProductNotFoundException::new);
    }
    @Override
    public List<ProductSellerDto> findProductWithFilter(ProductSearchDto dto) {
        String productName = dto.name() == null?"":dto.name();
        // List<Long> productCategories = List.of(dto.categoriesId());
        Double minPrice = dto.minPrice();
        Double maxPrice = dto.maxPrice();
        Specification<Product> specification = (root, query, cb) ->{
            var namePredicate = cb.like(root.get("name"), "%"+productName+"%");
            var minPricePredicate = minPrice == null?cb.conjunction():cb.greaterThan(root.get("salePrice"), minPrice);
            var maxPricePredicate = maxPrice == null?cb.conjunction():cb.lessThan(root.get("salePrice"), maxPrice);
            return cb.and(namePredicate, maxPricePredicate, minPricePredicate);
        };
        return repo.findAll(specification).stream().map(mapper::toStaffDto).collect(Collectors.toList());
    }

}
