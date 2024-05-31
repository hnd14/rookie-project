package com.example.project.image.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.image.entities.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {

}
