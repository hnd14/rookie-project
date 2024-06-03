package com.example.project.products.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Setter
@Getter
@NoArgsConstructor
public class AverageProductRating {
    @Column(name = "avg_rating", table = "scores")
    private Double avgRating;
}
