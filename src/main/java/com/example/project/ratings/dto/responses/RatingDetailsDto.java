package com.example.project.ratings.dto.responses;

import java.time.LocalDateTime;

public record RatingDetailsDto(String id, String username, String productId, Integer scores, String comment,
        LocalDateTime updatedTime) {

}
