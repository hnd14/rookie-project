package com.example.project.ratings.dto.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record PostNewRatingDto(@NotBlank Long userId,
                                @NotBlank Long productId,
                                @NotBlank @Min(value = 1) @Max(value=5) Long scores) {

}
