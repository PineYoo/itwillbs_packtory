package kr.co.itwillbs.de.common.securityHandler;

import java.io.IOException;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.itwillbs.de.admin.dto.LogDTO;
import kr.co.itwillbs.de.admin.mapper.LogMapper;
import kr.co.itwillbs.de.common.util.DeviceUtil;
import lombok.extern.slf4j.Slf4j;

// 로그인 실패 시 자동으로 처리할 핸들러 정의
// SimpeUrlAuthenticationFailureHandler 클래스 상속 대신 authticationFailureHandler 인터페이스 구현도 가능
// => 반대로, 성공 시 SimpleUrlAuthenticationSuccessHandler 또는 AuthenticationSuccessHandler의 구현체 정의
@Slf4j
@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	private final LogMapper logMapper;
	private final DeviceUtil deviceUtil;
	
	public CustomAuthenticationFailureHandler(LogMapper logMapper, DeviceUtil deviceUtil) {
		this.logMapper = logMapper;
		this.deviceUtil = deviceUtil;
	}
	
	private final String LOG_ACCESS_TYPE_LOGIN = "9";
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		//super.onAuthenticationFailure(request, response, exception);
		// HttpServletRequest 객체로부터 HttpSession 객체 가져오기
		String memberId = request.getParameter("memberId");
		String failureReason = getFailureReason(exception);
		
		try {
			LogDTO logDTO = LogDTO.builder()
				.accessId(memberId)
				.accessType(LOG_ACCESS_TYPE_LOGIN)
				.accessDevice(deviceUtil.getDeviceType(request))
				.ip(request.getRemoteAddr())
				.url("/login")
				.parameters(failureReason)
				.build();
				
			logMapper.registerLog(logDTO);
		} catch(Exception e) {
			log.error("Failed to write login log to database {}", e.getMessage());
		}
		
		// 로그인 실패 메시지 세션에 저장
		HttpSession session = request.getSession();
		session.setAttribute("errorMessage", "아이디 또는 비밀번호가 올바르지 않습니다.");
		// 로그인 페이지로 리디렉션
		response.sendRedirect("/login?error=true");
		// 이 클래스 정의 후 SecurityConfig 클래스에서 로그인 실패 시 failureHandler() 메서드로 핸들러 연결 필수
	}
	
	private String getFailureReason(AuthenticationException exception) {
		if (exception instanceof BadCredentialsException) {
			return "잘못된 비밀번호";
		} else if (exception instanceof UsernameNotFoundException) {
			return "존재하지 않는 사용자";
		} else if (exception instanceof AccountExpiredException) {
			return "만료된 계정";
		} else if (exception instanceof LockedException) {
			return "잠긴 계정";
		} else if (exception instanceof DisabledException) {
			return "비활성화된 계정";
		} else {
			return "기타 오류";
		}
	}
}
