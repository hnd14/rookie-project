package com.example.project.products.entities;

import java.util.ArrayList;
import java.util.List;

import com.example.project.util.entities.Auditor;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categories")
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class Category extends Auditor{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(unique = true)
    @NotBlank
    private String name;

    @Column(name = "description")
    private String desc;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<ProductCategory> productsInCategory = new ArrayList<>();
}
