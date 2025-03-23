package kr.co.itwillbs.de.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
	
	/**
	 * 검색로봇에게 사이트 및 웹페이지를 수집할 수 있도록 허용하거나 제한하는 국제 권고안
	 * <br>robots.txt 에 대해서 (href: https://searchadvisor.naver.com/guide/seo-basic-robots)
	 * <br>(href: https://jihyunhillcs.tistory.com/39#Spring_Boot_%EC%97%90%EC%84%9C_%EC%A0%81%EC%9A%A9%ED%95%98%EA%B8%B0)
	 * @return
	 */
	@GetMapping(value={"/robots.txt", "/robot.txt"})
	@ResponseBody
	public String robots() {
		return "User-agent: *\n" +
				"Allow: /\n" +
				"Disallow: /files/*\n" +
				"Disallow: /admin/*\n";
	}
}
