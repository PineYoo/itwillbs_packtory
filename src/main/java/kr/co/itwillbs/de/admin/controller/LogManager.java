package kr.co.itwillbs.de.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/logManager")
public class LogManager {

	/**
	 * 어드민 > 시스템 사용 로그 > 리스트 뷰
	 * <br> 인터셉터에서 입력된 로그 테이블을 조회하는 기능
	 * @param model
	 * @return
	 */
	@GetMapping(value={"","/"})
	public String LogMainGetLogList(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return "/admin/logManager/log_list";
	}
}
