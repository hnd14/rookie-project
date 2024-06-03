package com.example.project.ratings.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.project.ratings.entities.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, String> {
    @Query(value = "SELECT AVG(r.scores) FROM Rating r WHERE r.product.id = ?1")
    Double findAverageRatingForProduct(Long productId);

    @Query(value = "SELECT r FROM Rating r WHERE r.product.id = :id")
    Page<Rating> findByProduct(@Param("id") Long id, Pageable page);
}
