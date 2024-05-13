package com.example.project.products.mapper;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import com.example.project.products.dto.Requests.PostNewCategoryDto;
import com.example.project.products.dto.Responses.CategoryDto;
import com.example.project.products.entities.Category;


@Mapper(componentModel = "spring")
@Component
public interface CategoryMapper {
    Category toNewCategory(PostNewCategoryDto dto);
    CategoryDto toDto(Category category);
}
