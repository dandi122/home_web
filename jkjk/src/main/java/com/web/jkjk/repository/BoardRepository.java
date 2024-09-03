package com.web.jkjk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.jkjk.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{
	
	//TODO 2024.09.03 #1: 페이징 기능 추가
	Page<Board> findAll(Pageable pageable);
	
	//TODO 2024.09.03 #6: 검색기능 구현
	Page<Board> findAll(Specification<Board> sf, Pageable pageable);
	
}
