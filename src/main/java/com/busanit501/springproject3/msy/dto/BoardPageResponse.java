package com.busanit501.springproject3.msy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BoardPageResponse {
    private List<BoardDto> boards;
    private int totalPages;
    private long totalElements;
}
