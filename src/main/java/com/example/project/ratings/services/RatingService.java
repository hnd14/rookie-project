package com.example.project.ratings.services;

import com.example.project.ratings.dto.requests.GetAverageRatingDto;
import com.example.project.ratings.dto.requests.PostNewRatingDto;
import com.example.project.ratings.dto.responses.AverageRatingDto;
import com.example.project.ratings.dto.responses.NewRatingPostedDto;

public interface RatingService {
    NewRatingPostedDto postNewRating(PostNewRatingDto dto);
    AverageRatingDto getAvgRating(GetAverageRatingDto dto);
}