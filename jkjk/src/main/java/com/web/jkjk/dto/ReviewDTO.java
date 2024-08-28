package com.web.jkjk.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {
	
	private Long id; // 댓글 ID
    private String content; // 댓글 내용
    private String writer; // 댓글 작성자
    private Long boardId; // 해당 댓글이 속한 게시글의 ID
    private LocalDateTime createdDate; // 댓글 작성 날짜

}
