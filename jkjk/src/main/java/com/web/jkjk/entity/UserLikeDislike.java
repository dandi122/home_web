/**
 * TODO 2024.09.05 #6 : 좋아요 싫어요를 유저당 한번씩만 가산할 수 있도록 구현해보기
 */
package com.web.jkjk.entity;

import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserLikeDislike {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
    @JoinColumn(name = "snsUser_id", nullable = false)
    private SnsUser snsUser;
	
	@ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;
	
	@Column(nullable = false)
	private boolean liked;
	
	@Column(nullable = false)
	private boolean disliked;
	

}
