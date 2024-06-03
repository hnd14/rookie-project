package com.example.project.products.services.Impl;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.dto.Responses.ProductCustomerDto;
import com.example.project.products.dto.Responses.ProductThumnailDto;
import com.example.project.products.exceptions.ProductNotFoundException;
import com.example.project.products.mapper.ProductMapper;
import com.example.project.products.repositories.ProductRepository;
import com.example.project.products.services.ProductService;
import com.example.project.products.services.ProductServiceStore;
import com.example.project.ratings.repositories.RatingRepository;
import com.example.project.util.dto.response.PagedDto;

@Service
@Transactional(readOnly = true)
public class ProductServiceStoreImpl implements ProductServiceStore {
    @Autowired
    private ProductRepository repo;
    @Autowired
    private ProductMapper mapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private RatingRepository ratingRepository;

    public ProductCustomerDto getProductById(Long id) {
        return repo.findById(id).map((product) -> {
            var res = mapper.toCustomerDto(product);
            res.setCategories(product.getCategories()
                    .stream()
                    .map((category) -> category.getCategory().getName())
                    .collect(Collectors.toList()));
            res.setImagesUrl(product.getImages().stream().map((image) -> image.getUrl()).collect(Collectors.toList()));
            res.setAverageScore(ratingRepository.findAverageRatingForProduct(id));
            return res;
        }).orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public PagedDto<ProductThumnailDto> findProductWithFilter(ProductSearchDto dto) {
        var content = productService.findProductWithFilter(dto);
        return new PagedDto<>(content.getContent().stream().map(mapper::toThumnailDto).collect(Collectors.toList()),
                content.getTotalPages(),
                content.getNumber());
    }

}
