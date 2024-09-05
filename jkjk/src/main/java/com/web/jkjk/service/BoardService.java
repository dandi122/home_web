package com.web.jkjk.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.web.jkjk.dto.BoardDTO;
import com.web.jkjk.dto.ReviewDTO;
import com.web.jkjk.entity.Board;
import com.web.jkjk.entity.Review;
import com.web.jkjk.entity.SnsUser;
import com.web.jkjk.entity.UserLikeDislike;
import com.web.jkjk.repository.BoardRepository;
import com.web.jkjk.repository.ReviewRepository;
import com.web.jkjk.repository.UserLikeDislikeRepository;
import com.web.jkjk.repository.UserRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class BoardService {
	
	//레포지토리 의존성 주입하기
	@Autowired
	private BoardRepository boardRepository;
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private UserLikeDislikeRepository userLikeDislikeRepository;
	@Autowired
	private UserRepository userRepository;
	
	
	private final int PAGE_SIZE = 10;
	
	// TODO 2024.09.05 #5 : 공지사항 게시판 구현해보기
	public Page<BoardDTO> findByWriter(int page) {
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("id").descending());
		return boardRepository.findByWriter("admin", pageable).map(board -> new BoardDTO().fromEntity(board));
	}
	
	// TODO 2024.09.03 #2 : 페이징기능 구현을 위한 서비스 추가
	public Page<BoardDTO> findAll(int page) {
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("id").descending());
		return boardRepository.findAll(pageable).map(board -> new BoardDTO().fromEntity(board));
	}
	
	// TODO 2024-09.03 #6 : 검색 로직 구현
	public Page<BoardDTO> findAll(int page, String keyword) {
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("id").descending());
		Specification<Board> sf = complexSearch(keyword);
		return boardRepository.findAll(sf, pageable).map(board -> new BoardDTO().fromEntity(board));
	}
	
	private Specification<Board> complexSearch(String keyword) {
		return new Specification<>() {
			
			@Override
			public Predicate toPredicate(Root<Board> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
//				return criteriaBuilder.like(root.get("title"), "%"+keyword+"%");
				return criteriaBuilder.or(
						criteriaBuilder.like(root.get("title"), "%"+keyword+"%"),
						criteriaBuilder.like(root.get("content"), "%"+keyword+"%")
						);
			}
		};
	}
	
	// 모든 게시글을 조회하고, BoardDTO 리스트로 변환하여 반환
    @Transactional
    public List<BoardDTO> findAll() {
        return boardRepository.findAll().stream()
                .map(board -> new BoardDTO().fromEntity(board))
                .collect(Collectors.toList());
    }
    
	// 게시글을 저장하고 저장된 게시글의 ID 반환하는 메소드 만들기
	@Transactional
	public Long save(BoardDTO boardDTO) {
		Board board = new Board();
		board.setTitle(boardDTO.getTitle());
		board.setContent(boardDTO.getContent());
		board.setWriter(boardDTO.getWriter());
        return boardRepository.save(board).getId();
	}
	
	// 게시글 수정 메소드
	@Transactional
	public void motify(BoardDTO boardDTO) {
		Board board = boardRepository.findById(boardDTO.getId()).orElseThrow(() -> new IllegalArgumentException("해당 id에 해당하는 게시글이 없습니다."));
		board.setTitle(boardDTO.getTitle());
		board.setWriter(boardDTO.getWriter());
		board.setContent(boardDTO.getContent());
		board.setModifiedDate(LocalDateTime.now());
		boardRepository.save(board);
	}
        
    
    // 특정 아이디에 해당하는 게시글을 조회하고, BoardDTO로 변환하여 반환하게하기
    @Transactional
    public BoardDTO findById(Long id) {
    	
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 id에 해당하는 게시글이 없습니다."));
        
        return new BoardDTO().fromEntity(board);
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
    
    /**
     * TODO 2024.09.05 #6 : 좋아요 싫어요를 유저당 한번씩만 가산할 수 있도록 구현해보기
     */
    @Transactional
    public void increaseLikes(String username, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        
        Optional<SnsUser> osnsUser = userRepository.findByUsername(username);
        SnsUser snsUser = osnsUser.get();
        
        Optional<UserLikeDislike> existing = userLikeDislikeRepository.findBySnsUserAndBoard(snsUser, board);
       
        if(existing.isPresent()) {
        	// 기존 저장된 값이 있다면,
        	UserLikeDislike userLikeDislike = existing.get();
        	
        	if(userLikeDislike.isLiked()) {
        		// 기존 저장된 값이 "좋아요(true)" 이면,
        		board.decreaseLikes();
        		userLikeDislike.setLiked(false);
        		
        	} else {
        		// 기존 저장된 값이 "좋아요가 아님(false)" 이면,
        		board.increaseLikes();
        		userLikeDislike.setLiked(true);
        	}
        	
        	userLikeDislikeRepository.save(userLikeDislike);
        } else {
        	// 저장된 값이 없다면,
        	board.increaseLikes();
        	
        	UserLikeDislike userLikeDislike = new UserLikeDislike();
        	userLikeDislike.setSnsUser(snsUser);
        	userLikeDislike.setBoard(board);
        	userLikeDislike.setLiked(true);
        	userLikeDislike.setDisliked(false);
        	userLikeDislikeRepository.save(userLikeDislike);
        }
        
        boardRepository.save(board);
        
    }
    
    
    // 게시글에 좋아요를 추가하는 메서드
//    @Transactional
//    public void increaseLikes(Long boardId) {
//        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
//        board.increaseLikes();
//        boardRepository.save(board);
//    }
    
    
    /**
     * TODO 2024.09.05 #6 : 좋아요 싫어요를 유저당 한번씩만 가산할 수 있도록 구현해보기
     */
    @Transactional
    public void increaseDislikes(String username, Long boardId) {
    	Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        
        Optional<SnsUser> osnsUser = userRepository.findByUsername(username);
        SnsUser snsUser = osnsUser.get();
        
        Optional<UserLikeDislike> existing = userLikeDislikeRepository.findBySnsUserAndBoard(snsUser, board);
       
        if(existing.isPresent()) {
        	// 기존 저장된 값이 있다면,
        	UserLikeDislike userLikeDislike = existing.get();
        	
        	if(userLikeDislike.isDisliked()) {
        		// 기존 저장된 값이 "싫어요(true)" 이면,
        		board.decreaseDislikes();
        		userLikeDislike.setDisliked(false);
        		
        	} else {
        		// 기존 저장된 값이 "좋아요가 아님(false)" 이면,
        		board.increaseDislikes();
        		userLikeDislike.setDisliked(true);
        	}
        	
        	userLikeDislikeRepository.save(userLikeDislike);
        } else {
        	// 저장된 값이 없다면,
        	board.increaseDislikes();
        	
        	UserLikeDislike userLikeDislike = new UserLikeDislike();
        	userLikeDislike.setSnsUser(snsUser);
        	userLikeDislike.setBoard(board);
        	userLikeDislike.setLiked(false);
        	userLikeDislike.setDisliked(true);
        	userLikeDislikeRepository.save(userLikeDislike);
        }
        
        boardRepository.save(board);
    }
    
    
    // 게시글에 싫어요를 추가하는 메서드
//    @Transactional
//    public void increaseDislikes(Long boardId) {
//        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
//        board.increaseDislikes();
//        boardRepository.save(board);
//    }
    
    // 게시글 삭제하는 메서드
    @Transactional
    public void deleteById(Long id) {
    	boardRepository.deleteById(id);
    }
    
    //리뷰 삭제하는 메서드
    @Transactional
    public Long reviewDeleteById(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        reviewRepository.deleteById(id);
        return review.getBoard().getId();
        
    }
    
    // 리뷰id로 리뷰 찾는 메서드 
    @Transactional
    public ReviewDTO findReviewById(Long id) {
    	
        Review review = reviewRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 id에 해당하는 댓글이 없습니다."));
        
        return new ReviewDTO().fromEntity(review);
    }
    
    // 리뷰 수정하는 메서드
    @Transactional
	public Long modifyReview(ReviewDTO reviewDTO) {
		// TODO Auto-generated method stub
		Review review = reviewRepository.findById(reviewDTO.getId()).orElseThrow(() -> new IllegalArgumentException("해당 id에 해당하는 댓글이 없습니다."));
		review.setContent(reviewDTO.getContent());
		reviewRepository.save(review);
		return review.getBoard().getId();
		
	}


	
		

}
