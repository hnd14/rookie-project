package com.example.project.ratings.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.project.ratings.entities.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long>{
    @Query(value = "SELECT AVG() FROM Rating r WHERE r.product.id = ?1")
    Double findAverageRatingForProduct(Long productId);
}
