package com.example.project.products.exceptions;

import com.example.project.util.exceptions.NotFoundException;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException() {
        super("Category not found!");
    }
}
