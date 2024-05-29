package com.example.project.products.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import com.example.project.products.dto.Requests.PostNewProductDto;
import com.example.project.products.dto.Responses.ProductCustomerDto;
import com.example.project.products.dto.Responses.ProductDetailsAdminDto;
import com.example.project.products.dto.Responses.ProductAdminDto;
import com.example.project.products.entities.Product;

@Mapper(componentModel = "spring")
@Component
public interface ProductMapper {
    ProductCustomerDto toCustomerDto(Product product);
    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "categories")
    Product toNewProduct(PostNewProductDto dto);
    ProductAdminDto toStaffDto(Product product);
    ProductDetailsAdminDto toDetailsAdminDto(Product product);
}
