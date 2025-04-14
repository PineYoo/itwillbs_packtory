package kr.co.itwillbs.de.mes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/bs")
public class BootStrapController {

	@GetMapping(value={"/table","/table/"})
	public String tableView() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return "/bs/tables";
	}
	
	@GetMapping(value={"/main","/main/"})
	public String mainView() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return "/bs/main";
	}
}
