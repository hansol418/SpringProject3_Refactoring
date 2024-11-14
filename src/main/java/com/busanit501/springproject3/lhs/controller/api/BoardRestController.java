package com.busanit501.springproject3.lhs.controller.api;

import com.busanit501.springproject3.msy.dto.BoardDto;
import com.busanit501.springproject3.msy.entity.Comment;
import com.busanit501.springproject3.msy.service.BoardService;
import com.busanit501.springproject3.msy.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardRestController {

    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<Page<BoardDto>> getAllBoards(
            @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Pageable pageable) {

        Pageable sortedPageable = PageRequest.of(page, pageable.getPageSize(), Sort.by("createDate").descending());
        Page<BoardDto> boards = (searchKeyword != null && !searchKeyword.isEmpty())
                ? boardService.findAllItemsByKeyword(searchKeyword, sortedPageable)
                : boardService.getAllBoards(sortedPageable);

        return ResponseEntity.ok(boards);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDto> getBoardById(@PathVariable("id") Long id) {
        BoardDto board = boardService.getBoardById(id);
        List<Comment> comments = commentService.getCommentsByBoardId(id);
        board.setAnswerList(comments);
        return ResponseEntity.ok(board);
    }

    @PostMapping
    public ResponseEntity<String> createBoard(@RequestBody BoardDto boardDto) {
        boardService.createBoard(boardDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Board created successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateBoard(@RequestBody BoardDto boardDto, @RequestParam("file") MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                boardService.saveOrUpdateItemWithFile(boardDto, file);
            } else {
                boardService.saveOrUpdateItem(boardDto);
            }
            return ResponseEntity.ok("Board updated successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable("id") Long id) {
        boardService.deleteBoard(id);
        return ResponseEntity.ok("Board deleted successfully");
    }
}
