package kr.co.itwillbs.de.common.securityHandler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.itwillbs.de.admin.dto.LogDTO;
import kr.co.itwillbs.de.admin.mapper.LogMapper;
import kr.co.itwillbs.de.common.util.DeviceUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomLogoutHandler implements LogoutHandler {

	private final LogMapper logMapper;
	private final DeviceUtil deviceUtil;
	
	public CustomLogoutHandler(LogMapper logMapper, DeviceUtil deviceUtil) {
		this.logMapper = logMapper;
		this.deviceUtil = deviceUtil;
	}
	
	private final String LOG_ACCESS_TYPE_LOGIN = "9";
	
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		if (authentication != null) {
			String memberId = authentication.getName();
			log.info("login memberId: {}", memberId);

			try {
				LogDTO logDTO = LogDTO.builder()
					.accessId(memberId)
					.accessType(LOG_ACCESS_TYPE_LOGIN)
					.accessDevice(deviceUtil.getDeviceType(request))
					.ip(request.getRemoteAddr()) // 너 왜 ipv6가 찍히냐? https://developer.mozilla.org/ko/docs/Web/HTTP/Reference/Headers/X-Forwarded-For
					.url("/login")
					.parameters("logoutSuccess")
					.build();
					
				logMapper.registerLog(logDTO);
			} catch (Exception e) {
				log.error("Failed to write logout log to database {}", e.getMessage());
			}
		}
	}
}
