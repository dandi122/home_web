package com.web.jkjk.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.jkjk.dto.BoardDTO;
import com.web.jkjk.dto.ReviewDTO;
import com.web.jkjk.entity.Board;
import com.web.jkjk.entity.Review;
import com.web.jkjk.repository.BoardRepository;
import com.web.jkjk.repository.ReviewRepository;

import jakarta.transaction.Transactional;

@Service
public class BoardService {
	
	//레포지토리 의존성 주입하기
	@Autowired
	private BoardRepository boardRepository;
	@Autowired
	private ReviewRepository reviewRepository;
	
	// 게시글을 저장하고 저장된 게시글의 ID 반환하는 메소드 만들기
	@Transactional
	public Long save(BoardDTO boardDTO) {
		Board board = new Board();
		board.setTitle(boardDTO.getTitle());
		board.setContent(boardDTO.getContent());
		board.setWriter(boardDTO.getWriter());
        return boardRepository.save(board).getId();
	}
        
    
    // 특정 아이디에 해당하는 게시글을 조회하고, BoardDTO로 변환하여 반환하게하기
    @Transactional
    public BoardDTO findById(Long id) {
    	
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 id에 해당하는 게시글이 없습니다."));
        
        return new BoardDTO().toEntity(board);
    }
    
    // 모든 게시글을 조회하고, BoardDTO 리스트로 변환하여 반환
    @Transactional
    public List<BoardDTO> findAll() {
        return boardRepository.findAll().stream()
                .map(board -> new BoardDTO().toEntity(board))
                .collect(Collectors.toList());
    }
    
    // 댓글 저장 메서드
    @Transactional
    public Long saveComment(ReviewDTO reviewDTO) {
        Board board = boardRepository.findById(reviewDTO.getBoardId()).orElseThrow(() -> new IllegalArgumentException("해당 id에 해당하는 게시글이 없습니다."));
        Review review = new Review();
        review.setContent(reviewDTO.getContent());
        review.setWriter(reviewDTO.getWriter());
        review.setBoard(board);
        return reviewRepository.save(review).getId();
    }
    
    // 특정 게시글의 모든 댓글을 조회하는 메서드
    @Transactional
    public List<ReviewDTO> findReviewsByBoardId(Long boardId) {
        return reviewRepository.findByBoardId(boardId).stream()
                .map(review -> {
                    ReviewDTO dto = new ReviewDTO();
                    dto.setId(review.getId());
                    dto.setContent(review.getContent());
                    dto.setWriter(review.getWriter());
                    dto.setBoardId(review.getBoard().getId());
                    dto.setCreatedDate(review.getCreatedDate());
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    // 게시글에 좋아요를 추가하는 메서드
    @Transactional
    public void increaseLikes(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        board.increaseLikes();
        boardRepository.save(board);
    }
    
    // 게시글에 싫어요를 추가하는 메서드
    @Transactional
    public void increaseDislikes(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        board.increaseDislikes();
        boardRepository.save(board);
    }
    
    @Transactional
    public void deleteById(Long id) {
        boardRepository.deleteById(id);
    }
		

}
