package com.web.jkjk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.jkjk.dto.BoardDTO;
import com.web.jkjk.dto.ReviewDTO;
import com.web.jkjk.service.BoardService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	//컨트롤러에서는 서비스(Service) 이외에 엔티티나 레포지토리에 접근하여 로직을 구성하지 않도록 모두 수정해보기
	//즉, BoardService만 사용해서 구현
	@Autowired
	private BoardService boardService;
		
	// 모든 게시글을 조회하고, 모델에 추가하여 화면에 보여주기(완료)
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("boards", boardService.findAll());
        return "list"; // 게시글 리스트 뷰 반환
    }
    
    // 글작성 클릭하면 create 뷰로 이동하면서 해당 객체 유효성 검사를 위해 DTO 가져가기(완료)
    @GetMapping("/create")
    public String create(BoardDTO boardDTO) {
        return "create"; // 게시글 생성 폼을 반환
    }
    
    // 저장하기 버튼 클릭하면 새로운 글 저장하기, 유효성검사 추가, DTO를 이용하여 서비스를 통해 바로 저장하기(완료)
    @PostMapping("/create")
    public String create(@Valid BoardDTO boardDTO, BindingResult bindingResult) {
    	if( bindingResult.hasErrors() ) {
			return "create";
		}
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
    
    // 글 상세보기 화면에서 글 수정하기 버튼을 클릭하면 글 작성 뷰로 이동, 기존 글에 담겨있는 정보와 함께 이동(완료)
    // 기존 정보는 Model에 담아 전달해 보기(완료)
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id, Model model) {
    	model.addAttribute("boardDTO", boardService.findById(id));
    	//키값으로 "boardDTO"를 설정하니 DTO폼 전송과 같이 작동함 오류없음 확인
    	return "create";
    }
    
    // 수정하는 화면에서 수정하고 저장하기 버튼 클릭, 유효성검사, 서비스에 해당 로직 구현하여 modify함수 호출(완료)
    @PostMapping("/modify/{id}")
    public String modify(@Valid BoardDTO boardDTO, BindingResult bindingResult,
    					 @PathVariable("id") Long id) {
    	if( bindingResult.hasErrors() ) {
			return "create";
		}
        boardService.motify(boardDTO);
    	return "redirect:/board/" + id;
    }

    // 리뷰DTO에 BoardId 바인딩 후 서비스의 댓글 저장 메소드(저장된 보드아이디로 보드찾아 리뷰 엔티티중 board로 저장) 호출(완료)
    @PostMapping("/{boardId}/comment")
    public String createComment(@PathVariable("boardId") Long boardId, ReviewDTO reviewDTO) {
        reviewDTO.setBoardId(boardId);
        boardService.saveComment(reviewDTO);
        return "redirect:/board/" + boardId; // 댓글이 달린 게시글 상세 페이지로 리다이렉트
    }
    
    // 게시글에 좋아요를 추가하는 메서드 구현해보기(완료)
    @PostMapping("/{id}/like")
    public String like(@PathVariable("id") Long id) {
        boardService.increaseLikes(id);
        return "redirect:/board/" + id; // 게시글 상세 페이지로 리다이렉트
    }
    
    // 게시글에 싫어요를 추가하는 메서드 구현해보기(완료)
    @PostMapping("/{id}/dislike")
    public String dislike(@PathVariable("id") Long id) {
        boardService.increaseDislikes(id);
        return "redirect:/board/" + id; // 게시글 상세 페이지로 리다이렉트
    }

    // 게시글을 삭제하는 메서드 만들기(완료)
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        boardService.deleteById(id);
        return "redirect:/board/list";
    }
    
    // 댓글을 삭제하는 메서드 만들기, 삭제후 보드아이디를 리턴 받이 해당 아이디를 통해 리다이랙트 처리해보기(완료)
    @GetMapping("/deletereview/{id}")
    public String deleteReview(@PathVariable("id") Long id) {
        Long boardId = boardService.reviewDeleteById(id);
        return "redirect:/board/" + boardId; // 게시글 상세 페이지로 리다이렉트
    }
    
    // 댓글 수정하는 메서드, 리뷰dto를 키값으로 리뷰dto 전달하여 기존 값 유지하게 설정해보기(완료)
    @GetMapping("/modifyreview/{id}")
    public String modifyReview(@PathVariable("id") Long id, Model model) {
    	model.addAttribute("reviewDTO", boardService.findReviewById(id));
        return "review_form";
    }
    
    // 댓글 수정하는 메서드, 유효성 검사, 댓글 수정후 해당 댓글의 boardID 리턴받아 기존 게시글로 리다이렉트 시키기(완료)
    @PostMapping("/modifyreview/{id}")
    public String modifyReview(@Valid ReviewDTO reviewDTO, BindingResult bindingResult,
    					 @PathVariable("id") Long id) {
    	if( bindingResult.hasErrors() ) {
			return "review_form";
		}
        Long boardId = boardService.modifyReview(reviewDTO);
    	return "redirect:/board/" + boardId; // 게시글 상세 페이지로 리다이렉트
    }

}
