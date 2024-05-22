package com.example.project.products.services.Impl;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.entities.Product;
import com.example.project.products.exceptions.ProductNotFoundException;
import com.example.project.products.mapper.ProductMapper;
import com.example.project.products.repositories.ProductRepository;
import com.example.project.products.services.ProductService;

import jakarta.persistence.criteria.JoinType;

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService{
    @Autowired
    ProductRepository repo;
    @Autowired
    ProductMapper mapper;
    private final Integer DEFAULT_PAGE_SIZE = 10;
    private final String DEFAULT_SORT_BY = "name";


    @Override
    public Product getProductById(Long id) {
        return repo.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    private class ProductSpecifications {
        static Specification<Product> nameLike(String productName){
            return (root,query,cb)->cb.like(cb.lower(root.get("name")), "%"+productName.toLowerCase()+"%");
        }

        static Specification<Product> priceBetween(Double minPrice, Double maxPrice){
            return (root,query,cb) -> {
                var minPricePredicate = minPrice == null?cb.conjunction():cb.greaterThan(root.get("salePrice"), minPrice);
                var maxPricePredicate = maxPrice == null?cb.conjunction():cb.lessThan(root.get("salePrice"), maxPrice);
                return cb.and(maxPricePredicate,minPricePredicate);
            };
        }

        static Specification<Product> hasCategory(Long categoryId){
            return (root, query, cb) ->{
                var productCategory = root.join("categories",JoinType.LEFT);
                return categoryId == null?cb.conjunction()
                :cb.equal(productCategory.get("category").get("id"), categoryId);
            };
        }

        static Specification<Product> isFeatured(Boolean isFeatured){
            return (root,query,cb) -> isFeatured?cb.isTrue(root.get("isFeatured")):cb.conjunction();
        }
        
    }

    @Override
    public List<Product> findProductWithFilter(ProductSearchDto dto) {
        String productName = dto.name() == null?"":dto.name();
        Double minPrice = dto.minPrice();
        Double maxPrice = dto.maxPrice();   
        Long categoryId = dto.categoriesId();
        Boolean isFeatured = dto.isFeatured().orElse(false);
        var sortBy = dto.sortBy().orElse(DEFAULT_SORT_BY);
        String sortDir =dto.direction().orElse("ASC");
        Sort.Direction direction = sortDir.equals("DESC")?Direction.DESC:Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);

        Integer pageSize = dto.pageSize().orElse(DEFAULT_PAGE_SIZE);
        Integer pageNumber = dto.pageNumber().orElse(1);
        var page = PageRequest.of(pageNumber-1, pageSize, sort);
        
        return repo.findAll(Specification.where(ProductSpecifications.nameLike(productName))
                                            .and(ProductSpecifications.priceBetween(minPrice, maxPrice))
                                            .and(ProductSpecifications.isFeatured(isFeatured))
                                            .and(ProductSpecifications.hasCategory(categoryId))
                                            , page).toList();   
    }

}
