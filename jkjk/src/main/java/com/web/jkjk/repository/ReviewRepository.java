package com.web.jkjk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.jkjk.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{
	
	// 특정 게시글에 속한 모든 댓글을 조회
    List<Review> findByBoardId(Long boardId);
	
}
