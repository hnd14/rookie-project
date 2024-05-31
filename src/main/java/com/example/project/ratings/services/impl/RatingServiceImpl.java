package com.example.project.ratings.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.products.exceptions.ProductNotFoundException;
import com.example.project.products.repositories.ProductRepository;
import com.example.project.ratings.dto.requests.EditRatingDto;
import com.example.project.ratings.dto.requests.PostNewRatingDto;
import com.example.project.ratings.dto.responses.AverageRatingDto;
import com.example.project.ratings.dto.responses.NewRatingPostedDto;
import com.example.project.ratings.dto.responses.RatingDetailsDto;
import com.example.project.ratings.entities.Rating;
import com.example.project.ratings.repositories.RatingRepository;
import com.example.project.ratings.services.RatingService;
import com.example.project.users.entities.User;
import com.example.project.users.repositories.UserRepository;
import com.example.project.util.dto.requests.PagingDto;
import com.example.project.util.exceptions.NotFoundException;

@Service
@Transactional(readOnly = true)
public class RatingServiceImpl implements RatingService {
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    
    private final Integer DEFAULT_PAGE_SIZE = 10;
    private final String DEFAULT_SORT_BY = "name";

    @Override
    @Transactional
	public NewRatingPostedDto postNewRating(Long productId, PostNewRatingDto dto){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null 
            || !authentication.isAuthenticated()
            || authentication instanceof AnonymousAuthenticationToken){
            throw new SecurityException("You need to log in to leave a rating");
        }
        var username = authentication.getPrincipal().toString();
        Rating newRating = new Rating();
        newRating.setScores(dto.scores());
        newRating.setProduct(productRepository.findById(productId).orElseThrow(ProductNotFoundException::new));
        User user =userRepository.findOneByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Username not found"));
        newRating.setUser(user);
        ratingRepository.save(newRating);
        return new NewRatingPostedDto(newRating.getId());
    }

    @Override
	public AverageRatingDto getAvgRating(Long productId){
        return new AverageRatingDto(ratingRepository.findAverageRatingForProduct(productId));
    }

	@Override
	public List<RatingDetailsDto> getAllRatingsFor(Long productId, PagingDto dto) {
        var sortBy = dto.sortBy().orElse(DEFAULT_SORT_BY);
        String sortDir =dto.direction().orElse("ASC");
        Sort.Direction direction = sortDir.equals("DESC")?Direction.DESC:Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);

        Integer pageSize = dto.pageSize().orElse(DEFAULT_PAGE_SIZE);
        Integer pageNumber = dto.pageNumber().orElse(1);
        var page = PageRequest.of(pageNumber-1, pageSize, sort);

        return ratingRepository.findByProduct(productId, page).stream()
        .map(rating -> new RatingDetailsDto(rating.getId(),rating.getUser().getUsername(), rating.getScores(), rating.getComment())
        ).collect(Collectors.toList());
	}

    @Transactional
    @Override
    public RatingDetailsDto editRating(Long ratingId, EditRatingDto dto){
        Rating rating = ratingRepository.findById(ratingId).orElseThrow(NotFoundException::new);
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null 
            || !authentication.isAuthenticated()
            || authentication instanceof AnonymousAuthenticationToken){
            throw new SecurityException("You need to log in to edit a rating");
        }
        var username = authentication.getPrincipal().toString();
        if (!username.equals(rating.getUser().getUsername())){
            throw new SecurityException("You are not allowed to edit this rating");
        }
        rating.setScores(dto.scores().orElse(rating.getScores())); 
        rating.setComment(dto.comment().orElse(rating.getComment()));;
        ratingRepository.save(rating);
        return new RatingDetailsDto(rating.getId(), rating.getUser().getUsername(), rating.getScores(), rating.getComment());
    }

    @Override
    public void deleteRating(Long ratingId) {
        Rating rating = ratingRepository.findById(ratingId).orElseThrow(NotFoundException::new);
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null 
            || !authentication.isAuthenticated()
            || authentication instanceof AnonymousAuthenticationToken){
            throw new SecurityException("You need to log in to delete a rating");
        }
        var username = authentication.getPrincipal().toString();
        if (!username.equals(rating.getUser().getUsername())){
            throw new SecurityException("You are not allowed to delete this rating");
        }
        ratingRepository.delete(rating);
    }

    
}
