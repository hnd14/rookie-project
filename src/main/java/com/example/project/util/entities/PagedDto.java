package com.example.project.util.entities;

import java.util.Optional;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public record PagedDto(Optional<String> sortBy,
  Optional<String> direction, 
  Optional<Integer> pageSize,
  Optional<Integer> pageNumber) {

}
