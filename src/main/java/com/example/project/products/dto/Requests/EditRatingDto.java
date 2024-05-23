package com.example.project.products.dto.Requests;

import java.util.Optional;

import jakarta.validation.constraints.NotNull;

public record EditRatingDto(@NotNull Long ratingId, Optional<Integer> scores, Optional<String> comment) {

}
