package com.example.project.util.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PagedDto<T> {
    List<T> content;
    Integer pageCount;
    Integer currentPage;
}
