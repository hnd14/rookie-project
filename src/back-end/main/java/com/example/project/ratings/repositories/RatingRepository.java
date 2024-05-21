package com.example.project.ratings.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.project.ratings.entities.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Long>,JpaSpecificationExecutor<Rating>{

}
