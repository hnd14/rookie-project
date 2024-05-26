package com.example.project.util.dto;

import java.util.List;

public record ErrorDto(String statusCode, String title, String detail, List<String> fieldErrors) {

}
