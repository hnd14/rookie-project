package com.example.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.example.project.products.dto.Responses.ProductCustomerDto;
import com.example.project.products.services.ProductServiceStore;
import com.example.project.users.dto.requests.CustomerSignUpDto;
import com.example.project.users.dto.requests.UpdateUserInfoDto;
import com.example.project.users.dto.responses.UserReturnDto;
import com.example.project.users.services.UserService;

@RestController
@RequestMapping("/store")
public class StoreFrontController {
    @Autowired
    ProductServiceStore productService;
    @Autowired
    UserService userService;
    
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
        Long id=Long.valueOf(1);
        return userService.updateUserInfo(id, dto);
    }
}
