package com.example.project.util.dto.requests;

import java.util.Optional;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public record PagingDto(Optional<String> sortBy,
  Optional<String> direction, 
  Optional<Integer> pageSize,
  Optional<Integer> pageNumber) {

}
