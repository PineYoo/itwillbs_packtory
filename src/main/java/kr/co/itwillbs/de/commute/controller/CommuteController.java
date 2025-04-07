package kr.co.itwillbs.de.commute.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.common.util.StringUtil;
import kr.co.itwillbs.de.commute.dto.CommuteDTO;
import kr.co.itwillbs.de.commute.dto.CommuteListDTO;
import kr.co.itwillbs.de.commute.dto.CommuteSearchDTO;
import kr.co.itwillbs.de.commute.service.CommuteService;
import lombok.extern.slf4j.Slf4j;

/* 근태 관리 */
@Slf4j
@Controller
@RequestMapping("/commute") // 반복되는 경로를 미리 매핑(클래스 내부에서는 추가되는 경로만 매핑)
public class CommuteController {

	@Autowired
	private CommuteService commuteService;
	@Autowired
	private CommonCodeUtil commonCodeUtil;
	
	private final String COMMON_MAJOR_CODE_TRADE = "WORK_STATUS_CODE";

	
	// 출퇴근 목록 조회(SELECT)을 요청(GET)
	@GetMapping("")	// "/commute"
	public String getCommuteList(@ModelAttribute CommuteSearchDTO commuteSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		// 출퇴근 기록 코드
		List<CodeItemDTO> codeItemList = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_TRADE);
		System.out.println("codeItemList : " + codeItemList);
		model.addAttribute("codeItemList", codeItemList);
		
		// 하드코딩
		String id = "100006";	// 허민의 사번 100006 하드코딩
		
		List<CommuteListDTO> commuteList = commuteService.getCommuteList(id, commuteSearchDTO);
		log.info("commuteList : " + commuteList);
		model.addAttribute("commuteList",commuteList);
		
		return "commute/commute_management";
	}
	
	
	// 출퇴근 목록 조회(SELECT) 요청(GET) - 검색 조건
	@GetMapping(value = {"/search"}) // "/commute/search"
	public String getCommuteListBySearchDTO(@ModelAttribute CommuteSearchDTO commuteSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("commuteSearchDTO : {}", StringUtil.objToString(commuteSearchDTO));
		
		// 출퇴근 기록 코드
		List<CodeItemDTO> codeItemList = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_TRADE);
		System.out.println("codeItemList : " + codeItemList);
		model.addAttribute("codeItemList", codeItemList);
		
		// 하드코딩
		String id = "100006";	// 허민의 사번 100006 하드코딩
		
		// 출퇴근 조건 검색 리스트 조회 요청(SELECT) - 재사용
		List<CommuteListDTO> commuteList = commuteService.getCommuteList(id, commuteSearchDTO);
		log.info("commuteList : " + commuteList);
		model.addAttribute("commuteList", commuteList);
		
		return "commute/commute_management";
	}
	
	// --------------------------------------------------
	// 임시) 메인페이지에 들어 갈 출퇴근 기록 버튼
	// 페이지 이동(get)
	@GetMapping("/button")	// "/commute/button"
	public String getCommuteRecode(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 출퇴근 기록 코드
		List<CodeItemDTO> codeItemList = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_TRADE);
		System.out.println("codeItemList : " + codeItemList);
		model.addAttribute("codeItemList", codeItemList);
		
		// 사번_하드코딩
		String id = "100006";	// 허민의 사번 100006 하드코딩
		
		// 로그인한 사번의 오늘 출퇴근 기록 조회
		LocalDate today = LocalDate.now();
		
		// 오늘 출근 기록
		CommuteDTO checkInRecord = commuteService.getCheckInTime(id, today);
		CommuteDTO checkOutRecord = commuteService.getCheckOutTime(id, today);
		
		// 마지막 기록
		CommuteDTO lastCommuteRecord = commuteService.getTodayLastCommute(id, today);
		
		System.out.println("checkInTime : " + checkInRecord);
		model.addAttribute("checkInTime", checkInRecord != null ? checkInRecord.getRegDate().toLocalTime() : null);	// 오늘 출근기록 있을 경우 출근시간
		model.addAttribute("checkOutTime", checkOutRecord != null ? checkOutRecord.getRegDate().toLocalTime() : null);	// 오늘 퇴근기록 있을 경우 퇴근시간
		System.out.println("lastCommuteRecord : " + lastCommuteRecord);	// 오늘 출퇴근 마지막 기록
		model.addAttribute("lastCommuteRecord", lastCommuteRecord);	// 오늘 출퇴근 마지막 기록
		
		return "commute/commute_recode_button";
	}
	
	// 출퇴근 버튼 클릭 시 저장 ajax
	@PostMapping("/save")
	@ResponseBody
	public ResponseEntity<String> saveCommute(@RequestBody CommuteDTO commuteDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("commuteDTO : {}", StringUtil.objToString(commuteDTO));
	    
		// 사번_하드코딩
		String id = "100006";	// 허민의 사번 100006 하드코딩
		
		commuteDTO.setEmployeeId(id);	// 사번
		commuteDTO.setRegDate(LocalDateTime.now()); // 현재 시간 설정
		log.info("commuteDTO22 : {}", StringUtil.objToString(commuteDTO));
		
		// 출퇴근 기록 요청(insert)
		commuteService.saveCommuteInfo(commuteDTO);
	    return ResponseEntity.ok("저장 완료");
	}
}
