package kr.co.itwillbs.de.common.securityHandler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.itwillbs.de.admin.dto.LogDTO;
import kr.co.itwillbs.de.admin.mapper.LogMapper;
import kr.co.itwillbs.de.common.util.ServletRequestUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 로그인 성공 시 처리할 부분
 */
@Slf4j
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final LogMapper logMapper;
	private final ServletRequestUtil servletRequestUtil;
	
	public CustomAuthenticationSuccessHandler(LogMapper logMapper, ServletRequestUtil servletRequestUtil) {
		this.logMapper = logMapper;
		this.servletRequestUtil = servletRequestUtil;
	}
	
	private final String LOG_ACCESS_TYPE_LOGIN = "9";
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, 
										HttpServletResponse response,
										Authentication authentication) throws IOException, ServletException {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		// 로그인 한 사용자의 인증 정보가 Authentication 타입 파라미터로 전달 됨.
		String memberId = authentication.getName();
		log.info("login process succeeded && memberId is {}",memberId);
		
		// DB에 로그인 기록
		
		try {
			LogDTO logDTO = LogDTO.builder()
				.accessId(memberId)
				.accessType(LOG_ACCESS_TYPE_LOGIN)
				.accessDevice(servletRequestUtil.getDeviceType(request))
				.ip(servletRequestUtil.getClientIp(request))
				.url("/login")
				.parameters("loginSuccess")
				.build();
				
			logMapper.registerLog(logDTO);
		} catch(Exception e) {
			log.error("Failed to write login log to database {}", e.getMessage());
		}
		
//		Member member = (Member) authentication.getPrincipal();
//		
//		// 세션에 데이터 저장을 위해 httpServletRequest 객체로부터 HttpSession 객체 추출
//		HttpSession session = request.getSession(false); // 세션이 존재하지 않을 경우 새로 생성하지 않도록 false 값 전달
//		
//		// 세션이 존재할 경우에만 사용자 정보 저장
//		if(session != null) {
//			//session.setAttribute("memberDTO", member.toDTO());
//			
//			session.setAttribute("name", member.getName());
//			session.setAttribute("email", member.getEmail());
//		}
//		
//		// 로그인 성공 후 리디렉션 할 페이지를 지정
//		// => HttpServletResponse 객체의 sendRedirect() 메서드를 통해 리디렉션 할 URL 전달
		response.sendRedirect("/main"); // 메인 페이지
	}

}
