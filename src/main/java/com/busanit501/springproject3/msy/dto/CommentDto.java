package com.busanit501.springproject3.msy.dto;

import com.busanit501.springproject3.msy.entity.Board;
import com.busanit501.springproject3.msy.entity.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private Long boardId;
    private String content2;
    private String writer;

    // LocalDateTime 필드에 JSON 직렬화 형식 적용
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyDate;

    public Comment toEntity(Board board) {
        Comment comment = new Comment();
        comment.setId(this.id);
        comment.setBoard(board);
        comment.setContent2(this.content2);
        comment.setCreateDate(this.createDate);
        comment.setModifyDate(this.modifyDate);
        return comment;
    }

    public static CommentDto fromEntity(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setBoardId(comment.getBoard().getId());
        dto.setContent2(comment.getContent2());
        dto.setCreateDate(comment.getCreateDate());
        dto.setModifyDate(comment.getModifyDate());
        return dto;
    }
}
