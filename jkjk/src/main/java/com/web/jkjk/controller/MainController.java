package com.web.jkjk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
	
	//TODO 2024.08.29 #1 : 게시판 작업을 위해 리다이렉트 설정
	@RequestMapping("/")
	public String index() {
		return "index";
	}
}
