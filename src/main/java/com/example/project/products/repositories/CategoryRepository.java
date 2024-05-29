package com.example.project.products.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.products.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Page<Category> findByNameContains(String name, Pageable page);
    Optional<Category> findOneByName(String name);
}
