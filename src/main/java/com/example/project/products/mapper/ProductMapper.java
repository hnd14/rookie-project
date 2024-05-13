package com.example.project.products.mapper;

import org.mapstruct.Mapper;

import com.example.project.products.dto.Requests.PostNewProductDto;
import com.example.project.products.dto.Responses.ProductCustomerDto;
import com.example.project.products.dto.Responses.ProductSellerDto;
import com.example.project.products.entities.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductCustomerDto toCustomerDto(Product product);
    Product toNewProduct(PostNewProductDto dto);
    ProductSellerDto toStaffDto(Product product);
}
