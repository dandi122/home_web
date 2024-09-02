package com.web.jkjk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
	
	//홈페이지 이동
	@RequestMapping("/")
	public String index() {
		return "index";
	}
}
