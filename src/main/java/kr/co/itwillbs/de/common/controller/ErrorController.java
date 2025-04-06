package kr.co.itwillbs.de.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/error")
public class ErrorController {
/**
 * 스프링 시큐리티는 기본적으로 /error/403, 404, 500 등으로 에러 페이지 매핑을 제공
 * 운좋게도 에러컨트롤러의 매핑과 이름이 같아서 별다른 설정이 없이 동작되고 있는것! 처럼 느낄 수 있음.
 * 간단하게 처리되고 있는 것이지 직접 제어하고 있지 않기에 공부가 필요하다.
 * AI 형님이 ExceptionTranslationFilter 에서 처리된다고 하는데... 과연?
 * 상세 설정을 위해서는 @ControllerAdvice
 */
	
	
	/**
	 * 403 ERROR
	 * <br> (href: https://developer.mozilla.org/ko/docs/Web/HTTP/Reference/Status/403)
	 * @param model
	 * @return
	 */
	@GetMapping("/403")
	public String accessDenied(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		model.addAttribute("errorMessage", "해당 페이지에 접근할 권한이 없습니다.");
		return "error/403"; // 에러 페이지 뷰 이름
	}
	
	/**
	 * 404 ERROR
	 * <br> (href: https://developer.mozilla.org/ko/docs/Web/HTTP/Reference/Status/404)
	 * @param model
	 * @return
	 */
	@GetMapping("/404")
	public String fileNotFound(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		model.addAttribute("errorMessage", "해당 페이지를 찾을 수 없습니다.");
		return "error/404"; // 에러 페이지 뷰 이름
	}
	
	/**
	 * 500 ERROR
	 * <br> (href: https://developer.mozilla.org/ko/docs/Web/HTTP/Reference/Status/500)
	 * @param model
	 * @return
	 */
	@GetMapping("/500")
	public String internalServerError(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		model.addAttribute("errorMessage", "서버 에러가 발생.");
		return "error/500"; // 에러 페이지 뷰 이름
	}
}
