package kr.co.itwillbs.de.common.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import kr.co.itwillbs.de.common.vo.LoginVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomUserDetails implements UserDetails {

	private final LoginVO loginVO;
	
	public CustomUserDetails(LoginVO loginVO) {
		this.loginVO = loginVO;
	}
	
	public LoginVO getLoginVO() {
		return loginVO;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		log.info("{}---start1", Thread.currentThread().getStackTrace()[1].getMethodName());
		return true; // 강제로 잠기지 않았다고 리턴
		// 단, DB에 별도로 만료 여부 값을 저장한 후 해당 값을 조회하여 리턴하도록 하면 만료 여부 컨트롤 가능
	}
	
	// 7) 계정 사용 가능 여부 리턴하는 메서드(true : 사용 가능, false : 안됨) - 선택 2
	@Override
	public boolean isEnabled() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return !"Y".equalsIgnoreCase(loginVO.getIsDeleted()); // 탈퇴 계정이면 false 처리; // 강제로 만료되지 않았다고 리턴
		// 단, DB에 별도로 만료 여부 값을 저장한 후 해당 값을 조회하여 리턴하도록 하면 만료 여부 컨트롤 가능
	}
	
	// 4) 계정 만료 여부 리턴하는 메서드(true : 만료 안됨, false : 만료) - 선택 3
	@Override
	public boolean isAccountNonExpired() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return true; // 강제로 만료되지 않았다고 리턴
		// 단, DB에 별도로 만료 여부 값을 저장한 후 해당 값을 조회하여 리턴하도록 하면 만료 여부 컨트롤 가능
	}
	
	// 3) 사용자의 패스워드 리턴하는 메서드 - 필수 4 
	// => 다만, UserDetails 인터페이스 구현한 상태에서 Member 엔티티의 password에 대한 getPassword() 메서드는 이미 존재함
	//	기존 getPassword() 메서드와의 중복 문제를 발생하지 않도록 명시적으로 구현하자!
	@Override
	public String getPassword() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return loginVO.getPassword();
	}
	
	// 6) 계정 비밀번호 만료 여부 리턴하는 메서드(true : 만료 안됨, false : 만료) - 선택 5
	@Override
	public boolean isCredentialsNonExpired() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return true; // 강제로 만료되지 않았다고 리턴
		// 단, DB에 별도로 만료 여부 값을 저장한 후 해당 값을 조회하여 리턴하도록 하면 만료 여부 컨트롤 가능
	}
	
	// 6
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		// 숫자 role을 문자열 role로 매핑
		String roleName;
		switch (loginVO.getRole()) {
		case "1":
			roleName = "ADMIN";
			break;
		case "2":
			roleName = "MANAGER";
			break;
		case "3":
			roleName = "EMPLOYEE";
			break;
		case "4":
			roleName = "USER";
			break;
		default:
			roleName = "USER"; // 기본값
		}
		log.info("role is {} roleName is {}", loginVO.getRole(), roleName);
		
		//return List.of(new SimpleGrantedAuthority("ROLE_"+ roleName));
		return List.of(new SimpleGrantedAuthority(roleName));
	}
	
	// 2) 로그인 한 사용자를 식별할 수 있는 Unique 한 값인 사용자 이름(= 진짜 이름이 아닌, username 의미) 리턴하는 메서드 - 필수 7
	// => 현재 어플리케이션에서는 email 을 아이디로 사용하므로 email 값 리턴
	@Override
	public String getUsername() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return loginVO.getMemberId();
	}
	
}
