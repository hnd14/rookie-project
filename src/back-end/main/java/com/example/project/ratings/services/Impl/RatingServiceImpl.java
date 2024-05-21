package com.example.project.ratings.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.products.repositories.ProductRepository;
import com.example.project.ratings.repositories.RatingRepository;
import com.example.project.users.repositories.UserRepository;



@Service
@Transactional(readOnly=true)
public class RatingServiceImpl {
    @Autowired
    RatingRepository ratingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;

}
