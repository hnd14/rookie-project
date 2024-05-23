package com.example.project.ratings.dto.requests;

import jakarta.validation.constraints.NotNull;

public record GetAverageRatingDto(@NotNull Long productId) {

}
