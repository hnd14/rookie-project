package com.example.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.dto.Responses.CategoryDto;
import com.example.project.products.dto.Responses.PagedDto;
import com.example.project.products.dto.Responses.ProductCustomerDto;
import com.example.project.products.services.CategoryServiceStore;
import com.example.project.products.services.ProductServiceStore;
import com.example.project.ratings.dto.requests.EditRatingDto;
import com.example.project.ratings.dto.requests.PostNewRatingDto;
import com.example.project.ratings.dto.responses.AverageRatingDto;
import com.example.project.ratings.dto.responses.NewRatingPostedDto;
import com.example.project.ratings.dto.responses.RatingDetailsDto;
import com.example.project.ratings.services.RatingService;
import com.example.project.users.dto.requests.CustomerSignUpDto;
import com.example.project.users.dto.requests.UpdateUserInfoDto;
import com.example.project.users.dto.responses.UserReturnDto;
import com.example.project.users.services.UserService;
import com.example.project.util.entities.PagingDto;


@RestController
@RequestMapping("/store")
public class StoreFrontController extends V1Rest {
    @Autowired
    ProductServiceStore productService;
    @Autowired
    UserService userService;
    @Autowired
    RatingService ratingService;
    @Autowired
    CategoryServiceStore categoryService;
    
    @GetMapping("/products/{id}")
    ProductCustomerDto getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @GetMapping("/products")
    @ResponseBody
    List<ProductCustomerDto> findProductsWithFilter(ProductSearchDto dto){
        return productService.findProductWithFilter(dto);
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public UserReturnDto signUp(@RequestBody CustomerSignUpDto dto){
        return userService.signUp(dto);
    }

    @PutMapping("/me")
    public UserReturnDto updateUserInfo(@RequestBody UpdateUserInfoDto dto){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication==null || authentication.isAuthenticated()){
            throw new SecurityException("You need to log in to use this function.");
        }
        var username = authentication.getPrincipal().toString();
        return userService.updateUserInfo(username, dto);
    }

    @PostMapping("/products/{id}/ratings")
    @ResponseStatus(HttpStatus.CREATED)
    public NewRatingPostedDto postRating(@PathVariable Long id,@RequestBody PostNewRatingDto dto){
        return ratingService.postNewRating(id, dto);
    }

    @GetMapping("/products/{id}/ratings/avg")
    public AverageRatingDto getAvgRating(@PathVariable Long id){
        return ratingService.getAvgRating(id);
    }

    @GetMapping("/products/{id}/ratings")
    public List<RatingDetailsDto> getAllRating(@PathVariable Long id,@RequestParam PagingDto pagingDto){
        return ratingService.getAllRatingsFor(id, pagingDto);
    }

    @PutMapping("/ratings/{id}")
    public RatingDetailsDto editRating(@PathVariable Long id, @RequestBody EditRatingDto dto){
        return ratingService.editRating(id,dto);
    }

    @DeleteMapping("/ratings/{id}")
    public void deleteRating(@PathVariable Long id){
        ratingService.deleteRating(id);
    }

    @GetMapping("/categories")
    public PagedDto<CategoryDto> getCategory(PagingDto dto){
        return categoryService.findAllCategories(dto);
    }
}   
