package com.web.jkjk.dto;

import java.time.LocalDateTime;

import com.web.jkjk.entity.Board;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 모두 dto를 거쳐가게 만들어보자
@Getter
@Setter
@ToString
public class BoardDTO {

	private Long id;
	
	@NotEmpty(message="제목은 반드시 입력해야합니다.")
	private String title;
	
	@NotEmpty(message="본문 내용은 반드시 입력해야 합니다.")
	private String content;
	private String writer;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;
	private int likes;
	private int dislikes;
	private int reviewCount;
	
	
	// Entity 객체를 DTO 객체로 변환
	public BoardDTO fromEntity(Board entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.writer = entity.getWriter();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
        this.likes = entity.getLikes();
        this.dislikes = entity.getDislikes();
        this.reviewCount = entity.getReviewtList().size();
        return this;
    }
	
	
}
