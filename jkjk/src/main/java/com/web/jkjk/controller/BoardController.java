package com.web.jkjk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.jkjk.dto.BoardDTO;
import com.web.jkjk.dto.ReviewDTO;
import com.web.jkjk.service.BoardService;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	
	// 모든 게시글을 조회하고, 모델에 추가하여 화면에 보여주기
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("boards", boardService.findAll());
        return "list"; // 게시글 리스트 뷰 반환
    }
       

    @GetMapping("/create")
    public String createForm() {
        return "create"; // 게시글 생성 폼을 반환
    }

    @PostMapping("/create")
    public String create(BoardDTO boardDTO) {
    	System.out.println(boardDTO.toString());
        boardService.save(boardDTO);
        return "redirect:/board/list"; // 게시글 리스트 페이지로 리다이렉트
    }
    
    // 특정 게시글을 조회하여 상세 페이지로 이동
    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        model.addAttribute("board", boardService.findById(id));
        model.addAttribute("reviews", boardService.findReviewsByBoardId(id));
        return "detail"; // 게시글 상세 뷰 반환
    }
    
    
    // 댓글을 저장하는 메서드
    @PostMapping("/{boardId}/comment")
    public String createComment(@PathVariable("boardId") Long boardId, ReviewDTO reviewDTO) {
        reviewDTO.setBoardId(boardId);
        boardService.saveComment(reviewDTO);
        return "redirect:/board/" + boardId; // 댓글이 달린 게시글 상세 페이지로 리다이렉트
    }
    
    // 게시글에 좋아요를 추가하는 메서드
    @PostMapping("/{id}/like")
    public String like(@PathVariable("id") Long id) {
        boardService.increaseLikes(id);
        return "redirect:/board/" + id; // 게시글 상세 페이지로 리다이렉트
    }
    
    // 게시글에 싫어요를 추가하는 메서드
    @PostMapping("/{id}/dislike")
    public String dislike(@PathVariable("id") Long id) {
        boardService.increaseDislikes(id);
        return "redirect:/board/" + id; // 게시글 상세 페이지로 리다이렉트
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        boardService.deleteById(id);
        return "redirect:list";
    }

}
