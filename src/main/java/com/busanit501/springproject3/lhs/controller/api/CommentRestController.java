package com.busanit501.springproject3.lhs.controller.api;

import com.busanit501.springproject3.msy.dto.CommentDto;
import com.busanit501.springproject3.msy.entity.Comment;
import com.busanit501.springproject3.msy.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentRestController {

    private final CommentService commentService;



    @PostMapping("/create/{boardId}")
    public ResponseEntity<String> createComment(@PathVariable("boardId") Long boardId, @RequestBody CommentDto commentDto) {
        commentDto.setBoardId(boardId);
        commentService.saveComment(commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Comment created successfully");
    }

    @PutMapping("/update/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable("commentId") Long commentId, @RequestBody CommentDto commentDto) {
        commentService.updateComment(commentId, commentDto);
        return ResponseEntity.ok("Comment updated successfully");
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("commentId") Long commentId) {
        CommentDto commentDto = commentService.getCommentById(commentId);
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("Comment deleted successfully");
    }

    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<CommentDto>> getCommentsByBoardId(@PathVariable("boardId") Long boardId) {
        List<Comment> comments = commentService.getCommentsByBoardId(boardId);
        List<CommentDto> commentDtos = comments.stream()
                .map(CommentDto::fromEntity)
                .toList();
        return ResponseEntity.ok(commentDtos);
    }


}
