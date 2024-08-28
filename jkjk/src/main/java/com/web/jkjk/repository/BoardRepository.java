package com.web.jkjk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.jkjk.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{
	

}
