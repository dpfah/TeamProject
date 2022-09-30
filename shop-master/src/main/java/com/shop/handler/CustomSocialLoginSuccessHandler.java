package com.shop.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.shop.dto.MemberSecurityDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler{
	
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		
		log.info("----------------------------------------------------------");
		log.info("CustomLoginSuccessHandler onAuthenticationSuccess ..........");
		log.info(authentication.getPrincipal());
		
		MemberSecurityDto memberSecurityDto = (MemberSecurityDto) authentication.getPrincipal();
		
		String encodedPw =memberSecurityDto.getPassword();
		
		// 소셜 로그인이고 회원의 패스워드가 1111
		
		if (memberSecurityDto.isSocial() && (memberSecurityDto.getPassword().equals("1111") || passwordEncoder.matches("1111", memberSecurityDto.getPassword()))) {
			log.info("Shoild Change Password");
			
			log.info("Redirect to Member Modify ");
			response.sendRedirect("/member/modify");
			
			return;
		} else {
			
			response.sendRedirect("/board/list");
		}
	}
	
}
