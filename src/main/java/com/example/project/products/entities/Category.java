package com.example.project.products.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories")
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue
    @Column
    private Long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "category_id")
    private List<ProductCategory> productsInCategory;
}
