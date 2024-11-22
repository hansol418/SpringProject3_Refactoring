package com.busanit501.springproject3.lhs.controller.api;

import com.busanit501.springproject3.msy.dto.BoardDto;
import com.busanit501.springproject3.msy.dto.BoardPageResponse;
import com.busanit501.springproject3.msy.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardRestController {

    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<BoardPageResponse> getAllBoards(
            @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        Page<BoardDto> boards = (searchKeyword != null && !searchKeyword.isEmpty())
                ? boardService.findAllItemsByKeyword(searchKeyword, pageable)
                : boardService.getAllBoards(pageable);

        BoardPageResponse response = new BoardPageResponse(
                boards.getContent(),
                boards.getTotalPages(),
                boards.getTotalElements()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDto> getBoardById(@PathVariable("id") Long id) {
        BoardDto board = boardService.getBoardById(id);
        return ResponseEntity.ok(board);
    }

    @PostMapping
    public ResponseEntity<String> createBoard(@RequestBody BoardDto boardDto) {
        boardService.createBoard(boardDto);
        return ResponseEntity.ok("Board created successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable("id") Long id) {
        boardService.deleteBoard(id);
        return ResponseEntity.ok("Board deleted successfully");
    }
}
