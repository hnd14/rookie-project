package com.example.project.ratings.services;

import java.util.List;

import com.example.project.ratings.dto.requests.EditRatingDto;
import com.example.project.ratings.dto.requests.PostNewRatingDto;
import com.example.project.ratings.dto.responses.AverageRatingDto;
import com.example.project.ratings.dto.responses.NewRatingPostedDto;
import com.example.project.ratings.dto.responses.RatingDetailsDto;
import com.example.project.util.entities.PagedDto;

public interface RatingService {
    NewRatingPostedDto postNewRating(Long productId, PostNewRatingDto dto);
    AverageRatingDto getAvgRating(Long productId);
    List<RatingDetailsDto> getAllRatingsFor(Long productId, PagedDto dto);
    RatingDetailsDto editRating(Long ratingId, EditRatingDto dto);
    void deleteRating(Long ratingId);
}