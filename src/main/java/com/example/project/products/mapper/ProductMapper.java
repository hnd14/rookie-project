package com.example.project.products.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import com.example.project.products.dto.Requests.PostNewProductDto;
import com.example.project.products.dto.Responses.ProductCustomerDto;
import com.example.project.products.dto.Responses.ProductDetailsAdminDto;
import com.example.project.products.dto.Responses.ProductThumnailDto;
import com.example.project.products.dto.Responses.ProductAdminDto;
import com.example.project.products.entities.Product;

@Mapper(componentModel = "spring")
@Component
public interface ProductMapper {
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "imagesUrl", ignore = true)
    @Mapping(target = "averageScore", ignore = true)
    ProductCustomerDto toCustomerDto(Product product);

    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "categories")
    Product toNewProduct(PostNewProductDto dto);

    ProductAdminDto toStaffDto(Product product);

    @Mapping(target = "imagesUrl", ignore = true)
    @Mapping(target = "categoriesInfo", ignore = true)
    ProductDetailsAdminDto toDetailsAdminDto(Product product);

    ProductThumnailDto toThumnailDto(Product product);
}
