package com.example.project.util.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.example.project.util.dto.response.ErrorDto;
import com.example.project.util.exceptions.DuplicatedResourceException;
import com.example.project.util.exceptions.NotFoundException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final String ERROR_LOG_FORMAT = "Error: URI: {}, ErrorCode: {}, Message: {}";

    private String getRequestPath(WebRequest req) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) req;
        return servletWebRequest.getRequest().getServletPath();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFoundException(NotFoundException ex, WebRequest req) {
        String message = ex.getMessage();
        log.warn(ERROR_LOG_FORMAT, getRequestPath(req), 404, message);
        ErrorDto dto = new ErrorDto("404", HttpStatus.NOT_FOUND.toString(), message, null);
        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicatedResourceException.class)
    public ResponseEntity<ErrorDto> handleDuplicatedResourceException(DuplicatedResourceException ex, WebRequest req) {
        String message = "Resource you are trying to create already existed.";
        log.warn(ERROR_LOG_FORMAT, getRequestPath(req), 400, message);
        ErrorDto dto = new ErrorDto("400", HttpStatus.BAD_REQUEST.toString(), message, null);
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }
}
