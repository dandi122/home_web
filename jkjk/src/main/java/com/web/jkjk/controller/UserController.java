/**
 * TODO #2024.09.02 1. 회원가입 절차 구현
 */
package com.web.jkjk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.jkjk.dto.UserDTO;
import com.web.jkjk.service.UserService;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
		
	// 회원가입 화면으로 이동하는 메소드
	@GetMapping("/signup")
	public String signup(UserDTO userDTO) {
		
		return "signup_form";
	}
	
	// 회원가입 버튼 누르면 회원정보 저장하는 메소드
	@PostMapping("/signup")
	public String signup(@Valid UserDTO userDTO, BindingResult bindingResult) {
		
		//유효성 검사 오류 체크
		if( bindingResult.hasErrors() ) {
			return "signup_form";
		}
		
		//비번 확인 체크
		if( !userDTO.getPassword1().equals(userDTO.getPassword2()) ) {
			bindingResult.rejectValue("password2", "passwordInCorrect", "패스워드가 서로 일치하지 않습니다.");
			return "signup_form";
		}
		
		// db저장
		try {
			userService.save(userDTO);
		} catch (DataIntegrityViolationException e) {
			bindingResult.reject("signupError", "이미 사용중인 아이디입니다.");
			return "signup_form";
		} catch (Exception e) {
			bindingResult.reject("signupError", e.getMessage());
			return "signup_form";
		}	
		
		return "redirect:/";		
	}
	
	//로그인화면 매핑
	@GetMapping("/login")
	public String login() {
		
		return "login_form";
	}
	
	
	
}
