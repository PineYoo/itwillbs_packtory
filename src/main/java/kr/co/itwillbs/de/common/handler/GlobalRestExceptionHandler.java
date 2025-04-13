package kr.co.itwillbs.de.common.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalRestExceptionHandler {
/**
 * <pre>
 * @RestControllerAdvice: Accept: application/json
 * 이 클래스는 @ResponseBody로 매핑되는 클래스에서 발생하는 자바 Exception을 관리하기 위해 만들어 짐
 * 1) @Valid 에서 파생되는 MethodArgumentNotValidException.class 을 처리함
 * 2) 아마 ControllerAdvice 에는 Http 400, 404, 405, 500 등의 오류 처리를 추가 하겠지? -> 근데 이것도 요즘 Enum으로 예쁘게 하던데 그렇게 가볼까?
 * 9) ... 내 todo는 CustomException인데.. 시간이 허락할까?
 * </pre> 
 */
	
	/**
	 * REST API 혹은 AJAX 요청에서 @Valid 유효성 검사 실패 시 처리
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		BindingResult bindingResult = ex.getBindingResult();
	
		// 필드 에러 정보를 추출해서 JSON으로 만들기
		Map<String, String> errorMap = new HashMap<>();
		bindingResult.getFieldErrors().forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
	
		// 응답 구조 정의
		Map<String, Object> response = new HashMap<>();
		response.put("status", "fail");
		response.put("errors", errorMap);
		
		log.info("response is {}", response);
		/*
		 * badRequest = 400 error ajax 응답할때 전부 200으로 보내서 status 값으로 if 분기 처리하는게 편한데. 너무 SI 식 사고방식인가?
		 * 의미론적으로는 실패는 badRequest가 맞다. 400으로 주고, 
		 */
		return ResponseEntity.badRequest().body(response);
	}
}
