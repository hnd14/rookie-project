package com.example.project.util.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DuplicatedResourceException extends RuntimeException {
    public DuplicatedResourceException(String message) {
        super(message);
    }
}
