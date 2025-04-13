package kr.co.itwillbs.de.common.exception;

import org.springframework.validation.BindingResult;

public class ValidatedFormException extends RuntimeException {

	/**
	 * 이거 뭐때문에 컴파일러에서 만들라고 말하는건지 기억이 안난다 ㄱ-)
	 */
	private static final long serialVersionUID = -2350431184097558838L;
	
	private final BindingResult bindingResult;
	
	public ValidatedFormException(BindingResult bindingResult) {
		this.bindingResult = bindingResult;
	}
	
	public BindingResult getBindingResult() {
		return bindingResult;
	}
}
