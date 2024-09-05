package com.web.jkjk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.jkjk.entity.Board;
import com.web.jkjk.entity.SnsUser;
import com.web.jkjk.entity.UserLikeDislike;

public interface UserLikeDislikeRepository extends JpaRepository<UserLikeDislike, Long>{

	Optional<UserLikeDislike> findBySnsUserAndBoard(SnsUser snsUser, Board board);
	
}
