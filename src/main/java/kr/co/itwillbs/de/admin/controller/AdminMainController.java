package kr.co.itwillbs.de.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminMainController {

	/**
	 * 
	 * @return
	 */
	@GetMapping(value={"","/"})
	public String mainView(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return "admin/view";
	}
	
	@GetMapping(value={"/dashboard","/dashboard/"})
	public String dashBoardView(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return "admin/dashBoard";
	}
	
}
