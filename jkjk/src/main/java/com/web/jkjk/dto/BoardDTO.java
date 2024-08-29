package com.web.jkjk.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.web.jkjk.entity.Board;
import com.web.jkjk.entity.Review;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 모두 dto를 거쳐가게 만들어보자
@Getter
@Setter
@ToString
public class BoardDTO {

	private Long id;
	private String title;
	private String content;
	private String writer;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;
	private int likes;
	private int dislikes;
	private int reviewCount;
	
	
	// Entity 객체를 DTO 객체로 변환
    // 이 메서드를 사용하여 데이터를 데이터베이스에 저장할 준비
	public BoardDTO toEntity(Board entity) {
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
