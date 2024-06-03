package com.example.project.ratings.dto.requests;

import java.util.Optional;

public record EditRatingDto(Optional<Integer> scores, Optional<String> comment) {

}
