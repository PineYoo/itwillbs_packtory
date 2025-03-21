package kr.co.itwillbs.de.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainController {

	/**
	 * 브라우저 주소창에 Host:port 만 입력 했을 때 들어오는 페이지
	 * <br>예시)localhost:8080 또는 localhost:8080/ 둘다 index.html 로 연결 시켜주기 위함
	 * <br>""만 있어도 가능하나, 고객사들이 "/" 남겨놓고 왜 안되냐는 VOC가 많아 PTSD적으로 작성하는 버릇이 있음
	 * @return
	 */
	@GetMapping(value= {"","/"})
	public String mainView() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return "/index";
	}
}
