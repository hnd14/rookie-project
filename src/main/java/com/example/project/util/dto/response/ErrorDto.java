package com.example.project.util.dto.response;

import java.util.List;

public record ErrorDto(String statusCode, String title, String detail, List<String> fieldErrors) {

}
