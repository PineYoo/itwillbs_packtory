package kr.co.itwillbs.de.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ServletRequestUtil {
/**
 * 추억을 되살려 spring-mobile-device 를 사용해보려 했는데(https://mvnrepository.com/artifact/org.springframework.mobile/spring-mobile-device)
 * <br>2015년 이후로 업데이트가 없네?
 * <br>그냥 만들자! 근데 이 상수를.. 이렇게 하는게 맞니? 하드코딩 아니냐!?
 */
	
	/**
	 * UserAgent에서 device 정보 가져오기
	 * @param request
	 * @return 1: PC, 2: Mobile, 3: Tablet
	 */
	public String getDeviceType(HttpServletRequest request) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		String userAgent = request.getHeader("User-Agent").toLowerCase();

		if (userAgent.contains("mobile") 
			|| userAgent.contains("android") 
			|| userAgent.contains("iphone")
			|| userAgent.contains("ipad") 
			|| userAgent.contains("windows phone")) {
			return "2"; // "MOBILE";
		} else if (userAgent.contains("tablet") 
					|| userAgent.contains("ipad") 
					|| userAgent.contains("kindle")) {
			return "3"; // "TABLET";
			
		} else {
			return "1"; // "PC";
		}
	}
	
	/**
	 * UserAgent에서 OS정보 가져오기
	 * @param request
	 * @return WINDOWS, MAC, LINUX, ANDROID, IOS, UNKNOWN
	 */
	public String getPlatform(HttpServletRequest request) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		String userAgent = request.getHeader("User-Agent").toLowerCase();

		if (userAgent.contains("windows")) {
			return "WINDOWS";
		} else if (userAgent.contains("mac")) {
			return "MAC";
		} else if (userAgent.contains("linux")) {
			return "LINUX";
		} else if (userAgent.contains("android")) {
			return "ANDROID";
		} else if (userAgent.contains("iphone") || userAgent.contains("ipad")) {
			return "IOS";
		} else {
			return "UNKNOWN";
		}
	}
	
	/**
	 * request에서 IPv4 주소를 직접 가져옴
	 * @param request
	 * @return
	 */
	public String getClientIp(HttpServletRequest request) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		String ip = request.getHeader("X-Forwarded-For");
		
		// ip == null || ip.length() == 0 => !StringUtils.hasLength(ip) 로 바꿈
		if (!StringUtils.hasLength(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (!StringUtils.hasLength(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (!StringUtils.hasLength(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (!StringUtils.hasLength(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (!StringUtils.hasLength(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (!StringUtils.hasLength(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (!StringUtils.hasLength(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-RealIP");
		}
		if (!StringUtils.hasLength(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("REMOTE_ADDR");
		}
		if (!StringUtils.hasLength(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		if (ip.equals("0:0:0:0:0:0:0:1") || ip.equals("127.0.0.1")) {
			try {
				InetAddress address = InetAddress.getLocalHost();
				ip = address.getHostAddress();
				return ip;
			} catch (UnknownHostException e) {
				log.error(e.getMessage());
			}
		}
		
		log.info("ip is {}", ip);
		// IPv6를 IPv4로 변환 (필요한 경우) 나도 모름 AI가 시킴
		if (ip.contains(":")) {
			// IPv6 주소를 IPv4로 변환하는 로직
			// 예: 0:0:0:0:0:ffff:192.168.1.1 -> 192.168.1.1
			String[] parts = ip.split(":");
			if (parts.length > 0) {
				String lastPart = parts[parts.length - 1];
				if (lastPart.contains(".")) {
					return lastPart;
				}
			}
		}

		return ip;
	}

	/**
	 * 이게 맞나 모르겠다. ㄱ-)
	 * 
	 * @param request
	 * @return 1: 프론트, 2: 어드민, 3: 파트너, 9: 로그인(하드코딩됨)
	 */
	public String getAccessType(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		
		if(requestURI.startsWith("/admin")) {
			return "2";
		} else if(requestURI.startsWith("/partner")) {
			return "3";
		} else {
			return "1";
		}
	}
}
