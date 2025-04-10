package kr.co.itwillbs.de.commute.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.common.service.CustomUserDetails;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.common.util.StringUtil;
import kr.co.itwillbs.de.common.vo.LoginVO;
import kr.co.itwillbs.de.commute.dto.CommuteDTO;
import kr.co.itwillbs.de.commute.dto.CommuteListDTO;
import kr.co.itwillbs.de.commute.dto.CommuteSearchDTO;
import kr.co.itwillbs.de.commute.dto.CommuteStatusDTO;
import kr.co.itwillbs.de.commute.service.CommuteService;
import kr.co.itwillbs.de.human.dto.DepartmentInfoDTO;
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
	private final String COMMON_MAJOR_CODE_TRADE_DEP = "DEPARTMENT_CODE";
	private final String CHECKIN_NAME_KR = "출근";
	private final String LATE_NAME_KR = "지각";
	
	// 출퇴근 목록 조회(SELECT)을 요청(GET)
	@GetMapping("")	// "/commute"
	public String getCommuteList(@ModelAttribute CommuteSearchDTO commuteSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		// 자기 사번의 부서, 직급에 따라 조회할 수 있는 범위가 달라짐 !!!!!!!!!!!!!!!!!!!!!!!!!
		// ------------------------------
		// 세션 아이디(사번) 불러오는 코드
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
		String id = userDetails.getUsername();
		log.info("id!!!!!!!!!!!!" + id);
		// ------------------------------

		// 출퇴근 코드
		List<CodeItemDTO> codeItemList = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_TRADE);
		System.out.println("codeItemList : " + codeItemList);
		model.addAttribute("codeItemList", codeItemList);
		
		// 부서 코드(검색바)
//		List<CodeItemDTO> departmentList = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_TRADE_DEP);
//		System.out.println("departmentList : " + departmentList);
//		model.addAttribute("departmentList", departmentList);

		// 하위 부서코드(검색바)
		List<DepartmentInfoDTO> subDeptList = commuteService.getDepartmentList(id);
		System.out.println("subDeptList : " + subDeptList);
		model.addAttribute("subDeptList", subDeptList);
		
        // 출퇴근 목록 조회(같은 부서, 더 낮은 직급인 사원만 조회 - 본인포함)
		List<CommuteListDTO> commuteList = commuteService.getCommuteList(id, commuteSearchDTO);
		log.info("commuteList : " + commuteList);
		// 페이징용 카운트 가져오기
		commuteSearchDTO.getPageDTO().setTotalCount(commuteService.getCommuteCountForPaging(id, commuteSearchDTO));
		model.addAttribute("commuteSearchDTO", commuteSearchDTO);
		model.addAttribute("commuteList",commuteList);
		
		return "commute/commute_management";
	}
	
	
	// 출퇴근 목록 조회(SELECT) 요청(GET) - 검색 조건
	@GetMapping(value = {"/search"}) // "/commute/search"
	public String getCommuteListBySearchDTO(@ModelAttribute CommuteSearchDTO commuteSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("commuteSearchDTO : {}", StringUtil.objToString(commuteSearchDTO));
		
		// 자기 사번의 부서, 직급에 따라 조회할 수 있는 범위가 달라짐 !!!!!!!!!!!!!!!!!!!!!!!!!
		// ------------------------------
		// 세션 아이디(사번) 불러오는 코드
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
		String id = userDetails.getUsername();
		// ------------------------------
		
		// 출퇴근 기록 코드
		List<CodeItemDTO> codeItemList = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_TRADE);
		System.out.println("codeItemList : " + codeItemList);
		model.addAttribute("codeItemList", codeItemList);
		
		// 부서 코드(검색바)
//		List<CodeItemDTO> departmentList = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_TRADE_DEP);
//		System.out.println("departmentList : " + departmentList);
//		model.addAttribute("departmentList", departmentList);
		
		// 하위 부서코드(검색바)
		List<DepartmentInfoDTO> subDeptList = commuteService.getDepartmentList(id);
		System.out.println("subDeptList : " + subDeptList);
		model.addAttribute("subDeptList", subDeptList);
		
		// 출퇴근 조건 검색 리스트 조회 요청(SELECT) - 재사용
		List<CommuteListDTO> commuteList = commuteService.getCommuteList(id, commuteSearchDTO);
		log.info("commuteList : " + commuteList);
		
		// 페이징용 카운트 가져오기
		commuteSearchDTO.getPageDTO().setTotalCount(commuteService.getCommuteCountForPaging(id, commuteSearchDTO));
		
		model.addAttribute("commuteSearchDTO", commuteSearchDTO);
		model.addAttribute("commuteList", commuteList);
		
		return "commute/commute_management";
	}
	
	// 근태현황 페이지 이동(GET)
	@GetMapping("/chart")	// "/commute/chart"
	public String getCommuteChart(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 날짜별 지각 건수
	    List<CommuteStatusDTO> lateCountByDate = commuteService.getCommuteCountByDate();
	    model.addAttribute("lateCountByDate", lateCountByDate);
		
		
		return "commute/commute_chart";
	}
	
	
	
	
	// =======================================================================================================
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 나중에 메인으로 이동 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// 임시) 메인페이지에 들어 갈 출퇴근 기록 버튼
	// 페이지 이동(get)
	@GetMapping("/button")	// "/commute/button"
	public String getCommuteRecode(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 출퇴근 기록 코드
		List<CodeItemDTO> codeItemList = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_TRADE);
		System.out.println("codeItemList : " + codeItemList);
		model.addAttribute("codeItemList", codeItemList);
		
		// ------------------------------
		// 세션 아이디(사번) 불러오는 코드
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String id = userDetails.getUsername();
        // ------------------------------
		
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
//	@PostMapping("/save")	// "/commute/save"
//	@ResponseBody
//	public ResponseEntity<String> saveCommute(@RequestBody CommuteDTO commuteDTO) {
//		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
//		log.info("commuteDTO : {}", StringUtil.objToString(commuteDTO));
//	    
////		commuteDTO.setEmployeeId(id);	// 사번
//		LocalDateTime now = LocalDateTime.now();
//		commuteDTO.setRegDate(now); // 현재 시간 등록
//
//		// 코드 리스트 조회 (WORK_STATUS_CODE)
//		List<CodeItemDTO> tradeCode = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_TRADE);
//		String checkInCode = getMinorCodeByMinorName(tradeCode, CHECKIN_NAME_KR); // "출근(1)"
//		String lateCode = getMinorCodeByMinorName(tradeCode, LATE_NAME_KR);       // "지각(5)"
//		log.info("checkInCode : " + checkInCode + ", lateCode : " + lateCode);
//		
//		
// 		// 출근이면 지각 여부 확인
//		if (checkInCode.equals(commuteDTO.getWorkStatusCode())) {
//			LocalTime standardTime = LocalTime.of(9, 10); // 오전 9시 10분 기준
//			if (now.toLocalTime().isAfter(standardTime)) {
//				commuteDTO.setWorkStatusCode(lateCode); // 지각 처리
//				log.info("※ 지각 처리됨!!!!!!!!!: {}", lateCode);
//			}
//		}
//		
//		log.info("commuteDTO22 : {}", StringUtil.objToString(commuteDTO));
//		
//		// 출퇴근 기록 요청(insert)
//		commuteService.saveCommuteInfo(commuteDTO);
//	    return ResponseEntity.ok("저장 완료");
//	}
	
	// ===================================================================
	/**
	 * 특정 minorName을 가진 minorCode를 반환하는 유틸 메서드
	 * @param codeList
	 * @param targetName(한글명)
	 * @return
	 */
	private String getMinorCodeByMinorName(List<CodeItemDTO> codeList, String targetName) {
		for (CodeItemDTO item : codeList) {
			if (targetName.equals(item.getMinorName())) {
				return item.getMinorCode();
			}
		}
		return null;
	}
}
