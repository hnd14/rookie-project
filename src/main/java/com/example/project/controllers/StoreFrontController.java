package com.example.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.products.dto.Requests.ProductSearchDto;
import com.example.project.products.dto.Responses.CategoryDto;
import com.example.project.products.dto.Responses.ProductCustomerDto;
import com.example.project.products.dto.Responses.ProductThumnailDto;
import com.example.project.products.services.CategoryServiceStore;
import com.example.project.products.services.ProductServiceStore;
import com.example.project.ratings.dto.requests.EditRatingDto;
import com.example.project.ratings.dto.requests.PostNewRatingDto;
import com.example.project.ratings.dto.responses.AverageRatingDto;
import com.example.project.ratings.dto.responses.NewRatingPostedDto;
import com.example.project.ratings.dto.responses.RatingDetailsDto;
import com.example.project.ratings.services.RatingService;
import com.example.project.users.dto.requests.CustomerSignUpDto;
import com.example.project.users.dto.responses.UserReturnDto;
import com.example.project.users.services.UserService;
import com.example.project.util.dto.requests.PagingDto;
import com.example.project.util.dto.response.PagedDto;

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
    ProductCustomerDto getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/products")
    @ResponseBody
    PagedDto<ProductThumnailDto> findProductsWithFilter(ProductSearchDto dto) {
        return productService.findProductWithFilter(dto);
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public UserReturnDto signUp(@RequestBody CustomerSignUpDto dto) {
        return userService.signUp(dto);
    }

    @GetMapping("/categories")
    public PagedDto<CategoryDto> getCategory(PagingDto dto) {
        return categoryService.findAllCategories(dto);
    }

    @PostMapping("/products/{id}/ratings")
    @ResponseStatus(HttpStatus.CREATED)
    public NewRatingPostedDto postRating(@PathVariable Long id, @RequestBody PostNewRatingDto dto) {
        return ratingService.postNewRating(id, dto);
    }

    @GetMapping("/products/{id}/ratings/avg")
    public AverageRatingDto getAvgRating(@PathVariable Long id) {
        return ratingService.getAvgRating(id);
    }

    @GetMapping("/products/{id}/ratings")
    @ResponseBody
    public PagedDto<RatingDetailsDto> getAllRating(@PathVariable Long id, PagingDto pagingDto) {
        return ratingService.getAllRatingsFor(id, pagingDto);
    }

    @GetMapping("/products/{id}/ratings/me")
    public RatingDetailsDto getMyRating(@PathVariable String id) {
        return ratingService.getMyRating(id);
    }

    @PutMapping("/products/{id}/ratings")
    public RatingDetailsDto editRating(@PathVariable(name = "id") String id, @RequestBody EditRatingDto dto) {
        return ratingService.editRating(id, dto);
    }

    @DeleteMapping("/products/{id}/ratings")
    public void deleteRating(@PathVariable(name = "id") String id) {
        ratingService.deleteRating(id);
    }

}
