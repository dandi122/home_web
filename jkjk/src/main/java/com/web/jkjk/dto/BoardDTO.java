package com.web.jkjk.dto;

import java.time.LocalDateTime;

import com.web.jkjk.entity.Board;

import lombok.Getter;
import lombok.Setter;

// 모두 dto를 거쳐가게 만들어보자
@Getter
@Setter
public class BoardDTO {

	private Long id;
	private String title;
	private String content;
	private String writer;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;
	
	
	// DTO 객체를 Entity 객체로 변환
    // 이 메서드를 사용하여 데이터를 데이터베이스에 저장할 준비
	public BoardDTO toEntity(Board entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.writer = entity.getWriter();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
        return this;
    }
	
	
}
