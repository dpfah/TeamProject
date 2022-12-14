package com.shop.service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.shop.constant.Role;
import com.shop.dto.MemberSecurityDto;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService{
	
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
		log.info("userRequest....");
		log.info("userRequest");
		
		log.info("oauth2 user.....................................");
		
		ClientRegistration clientRegistration = userRequest.getClientRegistration();
		String clientName = clientRegistration.getClientName();
		
		log.info("NAME: " + clientName);
		OAuth2User oAuth2User = super.loadUser(userRequest);
		Map<String, Object> paramMap = oAuth2User.getAttributes();
		
		String email = null;
		
		switch (clientName) {
			case "kakao":
				email = getKakaoEmail(paramMap);
				break;
		}
		
		log.info("===============================");
		log.info(email);
		log.info("===============================");
		
		return generateDto(email, paramMap);
	}
	
	private MemberSecurityDto generateDto(String email, Map<String, Object> params) {
		
		Optional<Member> result = memberRepository.findByEmail(email);
		
		// ????????????????????? ?????? ???????????? ???????????? ?????????
		if(result.isEmpty()) {
			// ?????? ?????? -- mid??? ????????? ??????/ ??????????????? 1111
			Member member = Member.builder()
					.name(email)
					.password(passwordEncoder.encode("1111"))
					.email(email)
					.social(true)
					.build();
			member.addRole(Role.USER);
			memberRepository.save(member);
			
			// MemberSecurityDto ?????? ??? ??????
			MemberSecurityDto memberSecurityDto = new MemberSecurityDto(email, "1111", email, false, true, 
														Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
			memberSecurityDto.setProps(params);
			
			return memberSecurityDto;
		} else {
			Member member = result.get();
			MemberSecurityDto memberSecurityDto = new MemberSecurityDto(member.getName(), member.getPassword(), 
						member.getEmail(), member.isDel(), member.isSocial(), 
						member.getRoleSet().stream().map(role -> new SimpleGrantedAuthority("ROLE_"+role.name()))
					.collect(Collectors.toList())
			);
			
			return memberSecurityDto;
		}
	}
	
	private String getKakaoEmail(Map<String, Object> paramMap) {
		
		log.info("KAKAO------------------------------------------");
		
		Object value = paramMap.get("kakao_account");
		
		log.info(value);
		
		LinkedHashMap accountMap = (LinkedHashMap) value;
		
		String email = (String)accountMap.get("email");
		
		log.info("email..." + email);
		
		return email;
	}

}
