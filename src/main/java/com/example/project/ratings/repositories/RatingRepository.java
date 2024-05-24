package com.example.project.ratings.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.project.ratings.entities.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long>{
    @Query(value = "SELECT AVG(r.scores) FROM Rating r WHERE r.product.id = ?1")
    Double findAverageRatingForProduct(Long productId);
    @Query(value = "SELECT r FROM Rating r WHERE r.product.id = ?1")
    List<Rating> findByProduct(Long id);
}
