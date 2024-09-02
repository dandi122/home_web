package com.web.jkjk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 보안정책 설정
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// 모든 페이지 접근 가능하게 해보기
		http.authorizeHttpRequests (( a ) -> a.requestMatchers(new AntPathRequestMatcher("/user/**")).permitAll() )
		.authorizeHttpRequests (( a ) -> a.requestMatchers(new AntPathRequestMatcher("/dist/**")).permitAll() )
		.authorizeHttpRequests (( a ) -> a.requestMatchers(new AntPathRequestMatcher("/plugins/**")).permitAll() )
		.authorizeHttpRequests (( a ) -> a.requestMatchers(new AntPathRequestMatcher("/**")).authenticated() )
		
		//h2 콘솔 이하 url은 csrf에 대한 보안 체크 무시(테스트로 해보기)
		.csrf( (b) -> b.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
		
		// X-frame-options 헤더값을 SAMEORIGIN으로 대체 -> iframe 미로딩 문제 해결 
		.headers( (c) -> c.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
		// h2 사용 안하니까 위에 두가지 옵션은 삭제해도 될거 같음
		
		// 스프링시큐리티 로그인 URL 등록
		.formLogin((formLogin) -> formLogin.loginPage("/user/login").defaultSuccessUrl("/"))
		//로그아웃
		.logout( (logout) -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
									.logoutSuccessUrl("/user/login")
									.invalidateHttpSession(true));
		
		return http.build();
	}
	
	// 비밀번호 암호화 모듈
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//인증 매니저 빈 생성
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	
}
