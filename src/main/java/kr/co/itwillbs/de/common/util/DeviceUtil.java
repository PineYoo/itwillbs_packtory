package kr.co.itwillbs.de.common.util;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class DeviceUtil {
/**
 * 추억을 되살려 spring-mobile-device 를 사용해보려 했는데(https://mvnrepository.com/artifact/org.springframework.mobile/spring-mobile-device)
 * <br>2015년 이후로 업데이트가 없네?
 * <br>그냥 만들자! 근데 이 상수를.. 이렇게 하는게 맞니? 하드코딩 아니냐!?
 */
	
	public String getDeviceType(HttpServletRequest request) {
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
	
	public String getPlatform(HttpServletRequest request) {
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
}
