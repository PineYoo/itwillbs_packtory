package kr.co.itwillbs.de.common.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
/*
 * @Valid BindingResult 처리분기가 form enctype="application/x-www-form-urlencoded"
 * 인데도 여기로 오지 않고 @RestControllerAdvice 로 가버려서 우선순위를 넣게 됨 스프링부트야 넌 진짜 RestApi가
 * 최적화된 셋팅으로 만들어져 버린거니? 그립다 dispatch-servlet 설정아...
 */
@ControllerAdvice
public class GlobalViewExceptionHandler {
	/**
	 * <pre>
	 * @ControllerAdvice: Accept: text/html? html5 기본인가? "application/x-www-form-urlencoded" 이거 아녔숴?
	 * 여튼 spring-boot에서 위에 말한 Header 값과 produces, consumes가 일치하지 않으면 이 클래스가 흔들일 일이 생긴다!
	 * 이 클래스는 @ResponseBody로 매핑되는 클래스에서 발생하는 자바 Exception을 관리하기 위해 만들어 짐
	 * 1) @Valid 에서 파생되는 MethodArgumentNotValidException.class 을 처리함
	 * 2) 아마 ControllerAdvice 에는 Http 400, 404, 405, 500 등의 오류 처리를 추가 하겠지? -> 근데 이것도 요즘 Enum으로 예쁘게 하던데 그렇게 가볼까?
	 * 9) ... 내 todo는 CustomException인데.. 시간이 허락할까?
	 * </pre>
	 */

	/**
	 * @Valid 에 걸려서 post 전송한 곳에서 이전 페이지 .../new 또는 /register_form 메서드의 return view 를
	 *        리턴해야한다.
	 * @param ex
	 * @param model
	 * @return returnView 입력 폼? 어라? 수정도? 해야되네?
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public String handleValidationException(MethodArgumentNotValidException ex, Model model,
			HttpServletRequest request) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		BindingResult bindingResult = ex.getBindingResult();

		// 유효성 오류 메시지를 수집
		Map<String, String> errorMap = new HashMap<>();
		bindingResult.getFieldErrors().forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));

		// 에러 메시지 map을 모델에 담아서 뷰로 전달
		model.addAttribute("errorMap", errorMap);
		// 원래 입력값 전달
		model.addAttribute("codeDTO", bindingResult.getTarget());

		// 기본적으로 폼 페이지로 다시 보내기 (뷰 이름은 상황에 맞게 수정)
		return "redirect:" + toMakeRefererURIForView(request);
	}

	private String toMakeRefererURIForView(HttpServletRequest request) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		// ======================================================
		// 이거 requestURI 보려면 HttpServletRequest 가 있어야 하는거 아녀?
//		String requestUri = request.getRequestURI(); // 예: /admin/code
//		log.info("requestUri is {}", requestUri);
//		String referer = request.getHeader("Referer"); // http://localhost:8080/admin/code/new  <- 이놈이 힌트네?
//		log.info("referer is {}", referer);
//		String servletPath = request.getServletPath(); // /admin/code
//		String pathInfo = request.getPathInfo(); // null ? 호옹?
//		log.info("servletPath is {}, pathInfo is {} ", servletPath, pathInfo);
//		String scheme = request.getScheme(); // http
//		log.info("scheme is {}", scheme);
//		String servetContextPath = request.getContextPath(); // ???
//		log.info("servetContextPath is {}", servetContextPath);
//		String servletServerName = request.getServerName(); // localhost
//		log.info("servletServerName is {}", servletServerName);
//		String servletServerPort = String.valueOf(request.getServerPort()); // 8080
//		log.info("servletServerPort is {}", servletServerPort);
//		String url = (servletPath != null ? servletPath : "/") + (pathInfo != null ? pathInfo : "");
//		log.info("url is {}", url); // /admin/code
		// ======================================================
		// 현재 요청의 URI를 다시 리턴해서 같은 URL로 포워드
		// ServletUriComponentsBuilder 이거 뭐야? 신기술이야!? 시간이 없으니 우선 촌스럽게 만들자.

		String referer = request.getHeader("Referer"); //
		String serverBase = request.getScheme() + "://" + request.getServerName()
							+ (request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" + request.getServerPort());
		String viewPath = "";
		if (referer != null && referer.startsWith(serverBase)) {
			viewPath = referer.replace(serverBase, "");
		} else {
			viewPath = "/"; // fallback
		}
		// 서버 주소 제거하고 URI만 남기기 (예: /admin/code/new)
		log.info("viewPath is {}", viewPath);
		return viewPath;
	}
}
