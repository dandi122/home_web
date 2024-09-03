/**
 * TODO #2024.09.02 : 스피링시큐리티에서 UserDetailsService 객체를 이용, 반드시 구현해야할 함수 통해 처리
 */

package com.web.jkjk.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.web.jkjk.config.UserRole;
import com.web.jkjk.entity.SnsUser;
import com.web.jkjk.repository.UserRepository;

@Service
public class UserSecurityService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	// 사용자 로그인 하면 인증에 대한 처리 진행하는 코드
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO : 유저네임을 이용하여 사용자 획득
		// 권한 부여
		// 권한 : admin, 일반, username을 기반으로 부여
		
//		Optional<SnsUser> _user = this.userRepository.findByUsername(username);
//        if (_user.isEmpty()) {
//            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
//        }
//        SnsUser user = _user.get();
		// 요렇게도 해보기
		SnsUser user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("해당 username에 해당하는 사용자가 없습니다."));
		
        
		
		List<GrantedAuthority> authorities = new ArrayList<>();
        // 사용자명이 ‘admin’인 경우에는 ADMIN 권한(ROLE_ADMIN)을 부여
        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }
        System.out.println(username + " 회원임");
        
		return new User(user.getUsername(), user.getPassword(), authorities);
	}

	
}
