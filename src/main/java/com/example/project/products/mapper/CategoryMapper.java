package com.example.project.products.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import com.example.project.products.dto.Requests.PostNewCategoryDto;
import com.example.project.products.dto.Responses.CategoryAdminDto;
import com.example.project.products.dto.Responses.CategoryDto;
import com.example.project.products.dto.Responses.SimpleCategoryDto;
import com.example.project.products.entities.Category;
import com.example.project.products.entities.ProductCategory;

@Mapper(componentModel = "spring")
@Component
public interface CategoryMapper {
    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "productsInCategory")
    Category toNewCategory(PostNewCategoryDto dto);

    CategoryDto toDto(Category category);

    CategoryAdminDto toAdminDto(Category category);

    @Mapping(target = "name", source = "dto.category.name")
    @Mapping(target = "id", source = "dto.category.id")
    SimpleCategoryDto toSimpleCategoryDto(ProductCategory dto);
}
