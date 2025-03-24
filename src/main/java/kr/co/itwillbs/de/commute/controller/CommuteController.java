package kr.co.itwillbs.de.commute.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.itwillbs.de.commute.dto.CommuteDTO;
import kr.co.itwillbs.de.commute.service.CommuteService;
import kr.co.itwillbs.de.sample.dto.ItemDTO;
import kr.co.itwillbs.de.sample.dto.ItemSearchDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/bs") // 반복되는 경로를 미리 매핑(클래스 내부에서는 추가되는 경로만 매핑)
public class CommuteController {
/**
 * 수업 시간에 진행한 JPA 게시판!
 */
	
	@Autowired
	private CommuteService commuteService;
	
	/**
	 * 출퇴근 목록 조회(SELECT)을 요청하는 "/commute" 주소 매핑(GET)
	 * @return
	 */
	@GetMapping(value={"/commute","/commute/"}) // 이미 컨트롤러 자체에서 공통 경로 "/commute" 를 매핑했으므로 널스트링("")을 지원
	public String getCommuteList(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		List<CommuteDTO> commuteList = commuteService.getCommuteList();
		log.debug("commuteList : " + commuteList);
		
		model.addAttribute("commuteDTOList",commuteService.getCommuteList());
		// 입력값 검증 및 파라미터로 활용할 CommuteSearchDTO 객체 생성 후 Model 에 저장
//		CommuteSearchDTO commuteSearchDTO = new CommuteSearchDTO();
//		model.addAttribute("commuteSearchDTO", commuteSearchDTO);
		
		return "commute/commute_management";
	}
	
}
