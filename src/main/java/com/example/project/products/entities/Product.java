package com.example.project.products.entities;

import java.util.ArrayList;
import java.util.List;

import com.example.project.image.entities.Image;
import com.example.project.ratings.entities.Rating;
import com.example.project.util.entities.Auditor;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.SecondaryTable;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@SecondaryTable(name = "scores", pkJoinColumns = @PrimaryKeyJoinColumn(name = "product_id"))
public class Product extends Auditor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "description")
    private String desc;

    @Column
    private Long stock;

    @Column
    private Double salePrice;

    @Column
    private Boolean isFeatured;

    @Column
    private String thumbnailUrl;

    @Embedded
    private AverageProductRating avgRating = new AverageProductRating();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductCategory> categories = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Image> images;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Rating> ratings;
}
