package com.example.project.ratings.entities;

import com.example.project.products.entities.Product;
import com.example.project.users.entities.User;
import com.example.project.util.entities.Auditor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ratings")
@EqualsAndHashCode(callSuper = false)
public class Rating extends Auditor{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    Long id;

    @Column
    @Min(value = 1)
    @Max(value = 5)
    @EqualsAndHashCode.Exclude
    Integer scores;

    @Column
    @EqualsAndHashCode.Exclude
    String comment;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id"),
        @JoinColumn(name = "username", nullable = false, referencedColumnName = "username")
    })
    User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false, referencedColumnName = "id")
    Product product;
}