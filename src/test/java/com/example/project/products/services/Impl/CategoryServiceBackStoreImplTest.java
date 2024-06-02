package com.example.project.products.services.Impl;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

import com.example.project.products.dto.Requests.PostNewCategoryDto;
import com.example.project.products.dto.Requests.UpdateCategoryDto;
import com.example.project.products.mapper.CategoryMapper;
import com.example.project.products.repositories.CategoryRepository;
import com.example.project.util.dto.requests.PagingDto;
import com.example.project.util.exceptions.DuplicatedResourceException;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;

import com.example.project.products.entities.Category;
import com.example.project.products.entities.ProductCategory;
import com.example.project.products.exceptions.CategoryNotFoundException;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@Transactional
public class CategoryServiceBackStoreImplTest {
    @Autowired
    CategoryServiceBackStoreImpl service;
    @Autowired
    CategoryMapper mapper;
    @Autowired
    CategoryRepository repo;
    PostNewCategoryDto postDto;
    private final String CATEGORY_IN_DB_NAME = "new category";
    private Category newCat;
    private PagingDto defaultPage = new PagingDto(Optional.of("name"), Optional.of("ASC"), Optional.of(10),
            Optional.of(1));

    @BeforeAll
    void setUpData() {
        repo.deleteAll();
        newCat = new Category(Long.valueOf(1), CATEGORY_IN_DB_NAME, "", new ArrayList<ProductCategory>());

    }

    @BeforeEach
    void setUp() {
        postDto = new PostNewCategoryDto(null, null);
        repo.save(newCat);
    }

    @AfterEach
    void tearDown() {
        repo.delete(newCat);
    }

    @Test
    void testCreateNew_successfully_shouldReturnDtoForTheCreatedCategory() {
        // set up
        postDto = new PostNewCategoryDto("test", "test description");
        // Run
        var result = service.createNew(postDto);
        // assert
        assertThat(result).hasFieldOrPropertyWithValue("name", "test");
        assertThat(result).hasFieldOrPropertyWithValue("desc", "test description");
        assertThat(result.id()).isNotNull();
    }

    @Test
    void testCreateNew_nullName_shouldThrowIllegalArgumentsException() {
        assertThrows(ConstraintViolationException.class, () -> service.createNew(postDto));
    }

    @Test
    void testCreateNew_emptyName_shouldThrowIllegalArgumentsException() {
        // set up
        postDto = new PostNewCategoryDto("", "test description");
        // run and assert
        assertThrows(ValidationException.class, () -> service.createNew(postDto));
    }

    @Test
    void testCreateNew_existedName_shouldThrowIllegalArgumentsException() {
        // set up
        postDto = new PostNewCategoryDto(CATEGORY_IN_DB_NAME, "test description");
        // run and assert
        assertThrows(DuplicatedResourceException.class, () -> service.createNew(postDto));
    }

    @Test
    void testDeleteCategory_whenGivenId_shouldDeleteCategory() {
        // run
        service.deleteCategory(newCat.getId());
        // assert
        assertThat(repo.findAll()).doesNotContain(newCat);
    }

    @Test
    void testFindAllCategories_whenGivenEmpty_shouldUseDefault() {
        // set up
        var emptyDto = new PagingDto(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        var expected = service.findAllCategories(defaultPage);
        // run
        var result = service.findAllCategories(emptyDto);
        // assert
        assertThat(result).hasFieldOrPropertyWithValue("content", expected.getContent())
                .hasFieldOrPropertyWithValue("currentPage", expected.getCurrentPage())
                .hasFieldOrPropertyWithValue("pageCount", expected.getPageCount());
    }

    @Test
    void testFindAllCategories_whenGivenPageIndex0_shouldThrow() {
        // set up
        var dto = new PagingDto(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(0));
        // run
        assertThrows(IllegalArgumentException.class, () -> {
            service.findAllCategories(dto);
        });
    }

    @Test
    void testFindAllCategories_whenGivenPageIndexNegative_shouldThrow() {
        // set up
        var dto = new PagingDto(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(-1));
        // run
        assertThrows(IllegalArgumentException.class, () -> {
            service.findAllCategories(dto);
        });
    }

    @Test
    void testFindAllCategories_whenGivenPageSize0_shouldThrow() {
        // set up
        var dto = new PagingDto(Optional.empty(), Optional.empty(), Optional.of(0), Optional.empty());
        // run
        assertThrows(IllegalArgumentException.class, () -> {
            service.findAllCategories(dto);
        });
    }

    @Test
    void testFindAllCategories_whenGivenPageSizeNegative_shouldThrow() {
        // set up
        var dto = new PagingDto(Optional.empty(), Optional.empty(), Optional.of(-1), Optional.empty());
        // run
        assertThrows(IllegalArgumentException.class, () -> {
            service.findAllCategories(dto);
        });
    }

    @Test
    void testFindAllCategories_whenGivenBadSortBy_shouldThrow() {
        // set up
        var dto = new PagingDto(Optional.of("user"), Optional.empty(), Optional.empty(), Optional.empty());
        // run
        assertThrows(PropertyReferenceException.class, () -> {
            service.findAllCategories(dto);
        });
    }

    @Test
    void testFindCategoryById_whenGivenCorrectId_shouldFindTheCategoryAndTurnToDto() {
        // set up
        var cate = repo.findAll().getFirst();
        var expected = mapper.toAdminDto(cate);
        // run
        var res = service.findCategoryById(cate.getId());
        // assert
        assertThat(res).isEqualTo(expected);
    }

    @Test
    void testFindCategoryById_whenGivenWrongId_shouldThrow() {
        // set up
        var id = newCat.getId() - 1;
        // run
        assertThrows(CategoryNotFoundException.class, () -> {
            service.findCategoryById(id);
        });
    }

    @Test
    void testUpdate_success() {
        // set up
        var cate = repo.findAll().getFirst();
        var cateId = cate.getId();
        var newDesc = "Descriptions...";
        var updateDto = new UpdateCategoryDto(newDesc);
        // run
        var ret = service.update(cateId, updateDto);
        var res = repo.findById(cateId).orElseThrow();
        // assert
        assertThat(res).hasFieldOrPropertyWithValue("desc", newDesc);
        assertThat(ret).isEqualTo(mapper.toAdminDto(res));
    }

    @Test
    void testUpdate_whenGivenWrongId_shouldThrow() {
        // set up
        var cate = repo.findAll().getFirst();
        var cateId = cate.getId() - 5;
        var newDesc = "Descriptions...";
        var updateDto = new UpdateCategoryDto(newDesc);
        // run
        assertThrows(CategoryNotFoundException.class, ()->{
            service.update(cateId, updateDto);
        });
    }
}
