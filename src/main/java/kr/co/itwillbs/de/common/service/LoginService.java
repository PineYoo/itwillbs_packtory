package kr.co.itwillbs.de.common.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.common.mapper.LoginMapper;
import kr.co.itwillbs.de.common.vo.LoginVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoginService implements UserDetailsService {
/**
 * 스프링 시큐리티에서 loginForm에서 보낸 것은 여기로 호출되고...
 * loadUserByUsername 으로 된 저녀석이 뭔가 한다는데...?
 */
	private final LoginMapper loginMapper;
	private final PasswordEncoder passwordEncoder;
	
	public LoginService(LoginMapper LoginMapper, PasswordEncoder passwordEncoder) {
		this.loginMapper = LoginMapper;
		this.passwordEncoder = passwordEncoder;
	}
	
	/*
	public UserDetails loadUserByUsername(LoginVO loginVO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		LoginVO memberDTO = loginMapper.getMemberByUserName(loginVO);
		
		if(memberDTO == null) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + loginVO.getMemberId());
		}
		
		return memberDTO;
	}
	*/

	@Override
	public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("memberId is {}", memberId);
		
		LoginVO loginVO = loginMapper.getMemberByUserName(memberId);
		log.info("loginVO is {}", loginVO);
		
		if(loginVO == null) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + memberId);
		}		
		return loginVO;
	}
}
