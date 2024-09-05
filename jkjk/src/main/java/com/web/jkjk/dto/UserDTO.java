package com.web.jkjk.dto;

import java.time.LocalDateTime;

import com.web.jkjk.entity.SnsUser;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
	
	
	private Long id;
	@NotEmpty(message="사용자 ID는 반드시 입력하세요")
    private String username; 

	@NotEmpty(message="비밀번호 필수 입력 항목")
    private String password1; 
	
	@NotEmpty(message="비밀번호 확인 필수 입력 항목")
    private String password2; 
	
	@NotEmpty(message="이메일 필수 입력 항목")
    private String email;
	
//    private LocalDateTime regDate; 
    
    public UserDTO fromEntity(SnsUser entity) {
		this.id = entity.getId();
		this.username = entity.getUsername();
//		this.password1 = entity.getPassword();
		this.email = entity.getEmail();
//		this.regDate = entity.getRegDate();
		return this;
    }
    
}
