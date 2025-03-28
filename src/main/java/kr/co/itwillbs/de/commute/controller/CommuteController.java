package kr.co.itwillbs.de.commute.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.itwillbs.de.commute.dto.CommuteDTO;
import kr.co.itwillbs.de.commute.dto.CommuteListDTO;
import kr.co.itwillbs.de.commute.service.CommuteService;
import kr.co.itwillbs.de.sample.dto.ItemDTO;
import kr.co.itwillbs.de.sample.dto.ItemSearchDTO;
import lombok.extern.slf4j.Slf4j;

/* 근태 관리 */
@Slf4j
@Controller
@RequestMapping("/commute") // 반복되는 경로를 미리 매핑(클래스 내부에서는 추가되는 경로만 매핑)
public class CommuteController {

	@Autowired
	private CommuteService commuteService;
	
	// 출퇴근 목록 조회(SELECT)을 요청하는 "/commute" 주소 매핑(GET)
	@GetMapping(value={"/management","/management/"})	// 경로 : /commute/management
	public String getCommuteList(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		// 하드코딩
		String id = "100006";	// 허민의 사번 100006 하드코딩
		
		List<CommuteListDTO> commuteList = commuteService.getCommuteList(id);
		log.info("commuteList : " + commuteList);
		
		model.addAttribute("commuteList",commuteList);
		
		return "commute/commute_management";
	}
	
}
