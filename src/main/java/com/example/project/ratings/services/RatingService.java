package com.example.project.ratings.services;

import java.util.List;

import com.example.project.products.dto.Requests.EditRatingDto;
import com.example.project.ratings.dto.requests.PostNewRatingDto;
import com.example.project.ratings.dto.responses.AverageRatingDto;
import com.example.project.ratings.dto.responses.NewRatingPostedDto;
import com.example.project.ratings.dto.responses.RatingDetailsDto;

public interface RatingService {
    NewRatingPostedDto postNewRating(Long productId, PostNewRatingDto dto);
    AverageRatingDto getAvgRating(Long productId);
    List<RatingDetailsDto> getAllRatingsFor(Long productId);
    RatingDetailsDto editRating(EditRatingDto dto);
    void deleteRating(Long ratingId);
}