package com.example.project.ratings.services.impl;

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
import com.example.project.util.dto.response.PagedDto;
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
    private final String DEFAULT_SORT_BY = "updatedTime";

    @Override
    @Transactional
    public NewRatingPostedDto postNewRating(Long productId, PostNewRatingDto dto) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new SecurityException("You need to log in to leave a rating");
        }
        var username = authentication.getPrincipal().toString();
        Rating newRating = new Rating();
        newRating.setScores(dto.scores());
        newRating.setProduct(productRepository.findById(productId).orElseThrow(ProductNotFoundException::new));
        User user = userRepository.findOneByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        newRating.setUser(user);
        newRating.setId(productId.toString() + "_" + user.getUsername().toString());
        ratingRepository.save(newRating);
        return new NewRatingPostedDto(newRating.getId());
    }

    @Override
    public AverageRatingDto getAvgRating(Long productId) {
        return new AverageRatingDto(ratingRepository.findAverageRatingForProduct(productId));
    }

    @Override
    public PagedDto<RatingDetailsDto> getAllRatingsFor(Long productId, PagingDto dto) {
        var sortBy = dto.sortBy().orElse(DEFAULT_SORT_BY);
        String sortDir = dto.direction().orElse("DESC");
        Sort.Direction direction = sortDir.equals("ASC") ? Direction.ASC : Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        Integer pageSize = dto.pageSize().orElse(DEFAULT_PAGE_SIZE);
        Integer pageNumber = dto.pageNumber().orElse(1);
        var page = PageRequest.of(pageNumber - 1, pageSize, sort);

        var content = ratingRepository.findByProduct(productId, page);
        return new PagedDto<>(
                content.getContent().stream()
                        .map(rating -> new RatingDetailsDto(rating.getId(), rating.getUser().getUsername(),
                                productId.toString(),
                                rating.getScores(), rating.getComment()))
                        .collect(Collectors.toList()),
                content.getTotalPages(),
                content.getNumber());
    }

    @Transactional
    @Override
    public RatingDetailsDto editRating(String productId, EditRatingDto dto) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new SecurityException("You need to log in to edit a rating");
        }
        var username = authentication.getPrincipal().toString();
        Rating rating = ratingRepository.findById(productId + "_" + username).orElse(new Rating());
        rating.setScores(dto.scores().orElse(rating.getScores()));
        rating.setComment(dto.comment().orElse(rating.getComment()));
        rating.setProduct(
                productRepository.findById(Long.valueOf(productId)).orElseThrow(ProductNotFoundException::new));
        User user = userRepository.findOneByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        rating.setUser(user);
        rating.setId(productId + "_" + username);
        ratingRepository.save(rating);
        return new RatingDetailsDto(rating.getId(), rating.getUser().getUsername(), productId, rating.getScores(),
                rating.getComment());
    }

    @Override
    public void deleteRating(String productId) {

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new SecurityException("You need to log in to delete a rating");
        }
        var username = authentication.getPrincipal().toString();
        Rating rating = ratingRepository.findById(productId + "_" + username).orElseThrow(NotFoundException::new);
        ratingRepository.delete(rating);
    }

    @Override
    public RatingDetailsDto getMyRating(String productId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new SecurityException("You need to log in to see your rating");
        }
        var username = authentication.getPrincipal().toString();
        var ratingId = productId + "_" + username;
        Rating myRating = ratingRepository.findById(ratingId).orElse(new Rating(ratingId, 0, "", null, null));
        return new RatingDetailsDto(ratingId, username, productId, myRating.getScores(), myRating.getComment());
    }

}
