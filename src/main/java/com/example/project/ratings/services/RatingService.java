package com.example.project.ratings.services;

import java.util.List;

import com.example.project.ratings.dto.requests.EditRatingDto;
import com.example.project.ratings.dto.requests.PostNewRatingDto;
import com.example.project.ratings.dto.responses.AverageRatingDto;
import com.example.project.ratings.dto.responses.NewRatingPostedDto;
import com.example.project.ratings.dto.responses.RatingDetailsDto;
import com.example.project.util.dto.requests.PagingDto;

public interface RatingService {
    NewRatingPostedDto postNewRating(Long productId, PostNewRatingDto dto);
    AverageRatingDto getAvgRating(Long productId);
    List<RatingDetailsDto> getAllRatingsFor(Long productId, PagingDto dto);
    RatingDetailsDto editRating(String productId, EditRatingDto dto);
    void deleteRating(String productId);
}