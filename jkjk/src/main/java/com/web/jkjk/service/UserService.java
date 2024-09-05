package com.web.jkjk.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.web.jkjk.dto.UserDTO;
import com.web.jkjk.entity.SnsUser;
import com.web.jkjk.repository.UserRepository;

import jakarta.validation.Valid;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	// 암호화모듈 의존성 주입
	@Autowired
	private PasswordEncoder passwordEncoder;

	public void save(UserDTO userDTO) {
		SnsUser snsUser = new SnsUser();
		snsUser.setUsername(userDTO.getUsername());
		snsUser.setPassword(passwordEncoder.encode(userDTO.getPassword1()));
//		snsUser.setPassword(userDTO.getPassword1());
		snsUser.setEmail(userDTO.getEmail());
		userRepository.save(snsUser);
	}
	
}
