package com.example.project.ratings.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.project.products.exceptions.ProductNotFoundException;
import com.example.project.products.repositories.ProductRepository;
import com.example.project.ratings.dto.requests.GetAverageRatingDto;
import com.example.project.ratings.dto.requests.PostNewRatingDto;
import com.example.project.ratings.dto.responses.AverageRatingDto;
import com.example.project.ratings.dto.responses.NewRatingPostedDto;
import com.example.project.ratings.entities.Rating;
import com.example.project.ratings.repositories.RatingRepository;
import com.example.project.ratings.services.RatingService;
import com.example.project.users.entities.User;
import com.example.project.users.repositories.UserRepository;

public class RatingServiceImpl implements RatingService {
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
	public NewRatingPostedDto postNewRating(PostNewRatingDto dto){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null 
            || !authentication.isAuthenticated()
            || authentication instanceof AnonymousAuthenticationToken){
            throw new SecurityException("You need to log in to leave a rating");
        }
        var username = authentication.getPrincipal().toString();
        Rating newRating = new Rating();
        newRating.setScores(dto.scores());
        newRating.setProduct(productRepository.findById(dto.productId()).orElseThrow(ProductNotFoundException::new));
        User user =userRepository.findOneByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Username not found"));
        newRating.setUser(user);
        ratingRepository.save(newRating);
        return new NewRatingPostedDto(newRating.getId());
    }

    @Override
	public AverageRatingDto getAvgRating(GetAverageRatingDto dto){
        return new AverageRatingDto(ratingRepository.findAverageRatingForProduct(dto.productId()));
    }
}
