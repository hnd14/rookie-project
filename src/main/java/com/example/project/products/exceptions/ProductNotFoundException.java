package com.example.project.products.exceptions;

import com.example.project.util.exceptions.NotFoundException;

public class ProductNotFoundException extends NotFoundException {
    public ProductNotFoundException() {
        super("Product not found!");
    }
}
