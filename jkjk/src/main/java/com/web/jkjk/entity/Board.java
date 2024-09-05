package com.web.jkjk.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Board {
	
	// 게시글 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 
    
    // 게시글의 제목
    //@Column(nullable = false)
    private String title; 
    
    // 게시글의 내용
    //@Column(nullable = false)
    private String content; 
    
    // 게시글 작성자
    //@Column(nullable = false)
    private String writer; 
    
    // 게시글 생성 날짜
    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now(); 
    
    // 게시글 수정 날짜
    @Column(nullable = true)
    private LocalDateTime modifiedDate;
    
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
	private List<Review> reviewtList;
    
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
	private List<UserLikeDislike> userLikeDislikeList;
    
 // 좋아요와 싫어요 카운트를 위한 필드 추가
    @Column(nullable = false)
    private int likes = 0;  // 좋아요 카운트

    @Column(nullable = false)
    private int dislikes = 0;  // 싫어요 카운트

    // 좋아요 증가 메서드
    public void increaseLikes() {
        this.likes++;
    }

    // 싫어요 증가 메서드
    public void increaseDislikes() {
        this.dislikes++;
    }
    
    // 좋아요 감소 메소드
    public void decreaseLikes() {
        this.likes--;
    }

    // 싫어요 감소 메서드
    public void decreaseDislikes() {
        this.dislikes--;
    }
    
    
    
}
