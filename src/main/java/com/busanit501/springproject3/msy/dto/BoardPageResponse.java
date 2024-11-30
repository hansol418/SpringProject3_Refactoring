package com.busanit501.springproject3.msy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BoardPageResponse {
    @JsonProperty("content") // JSON 응답의 필드 이름을 "content"로 설정
    private List<BoardDto> boards;
    private int totalPages;
    private long totalElements;
}
