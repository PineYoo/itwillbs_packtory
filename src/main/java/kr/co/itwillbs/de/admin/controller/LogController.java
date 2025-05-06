package kr.co.itwillbs.de.admin.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.itwillbs.de.admin.dto.LogDTO;
import kr.co.itwillbs.de.admin.dto.LogSearchDTO;
import kr.co.itwillbs.de.admin.service.LogService;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value="/admin/log", name = "로그 관리")
public class LogController {
/**
 * <pre>
 * 로그 관리 하는 것에 대해서 요즘 기술들을 찾아보니
 * Oracle, MySQL 할 것 없이 longText나 clob 데이터 타입을 JSON형식으로 저장하는것 같다.
 * RDBMS에서 JSON 을 왜 지원하는지에 대해 더 알아봐야겠지만,
 * 텍스트 타입데이터에 Index는 풀스캔이 상식적이겠지만
 * JSON (K, V) 에서 K에 해쉬나 인덱스가 걸리는 것 같더라,
 * 그렇다면 파라미터 컬럼 검색에 이점이 확실히 있다고 봐야겠음.
 * 다음엔 그렇게 만들어봐야지! 
 * </pre>
 */
	
	private final LogService logService;
	private final CommonCodeUtil commonCodeUtil;
	
	public LogController(LogService logService, CommonCodeUtil commonCodeUtil) {
		this.logService = logService;
		this.commonCodeUtil = commonCodeUtil;
	}
	
	// 계속 사용하게 될 클래스 RequestMapping 문자열 값
	private final String VIEW_PATH="/admin/log";
	private final String COMMON_MAJOR_CODE_ACCESS_TYPE = "LOG_ACCESS_TYPE";
	private final String COMMON_MAJOR_CODE_ACCESS_DEVICE = "LOG_ACCESS_DEVICE";
	/**
	 * (개발테스트용)어드민 > 시스템 로그 > 로그 등록
	 * @param model
	 * @return
	 */
	@GetMapping(value="/new")
	public String logRegisterForm(Model model) {
		LogUtil.logStart(log);
		
		model.addAttribute("logDTO", new LogDTO());
		
		return VIEW_PATH+"/log_register_form";
	}
	
	/**
	 * (개발테스트용)어드민 > 시스템 로그 > 로그 등록(INSERT)
	 * @param logDTO
	 * @return
	 */
	@PostMapping(value={"","/"})
	public String logRegister(@ModelAttribute("logDTO") LogDTO logDTO) {
		LogUtil.logStart(log);
		LogUtil.logDetail(log, "requestDTO : {}", StringUtil.objToString(logDTO));
		
		if(logService.registerLog(logDTO) < 1) {
			return VIEW_PATH+"/log_register_form";
		}
		return "redirect:"+VIEW_PATH;
	}
	
	/**
	 * 어드민 > 시스템 사용 로그 > 리스트 뷰
	 * @param model
	 * @return
	 */
	@GetMapping(value= {"","/"})
	public String getLogList(@ModelAttribute LogSearchDTO logSearchDTO, Model model) {
		LogUtil.logStart(log);
		LogUtil.logDetail(log, "requestDTO : {}", StringUtil.objToString(logSearchDTO));
		
		// 페이징용 totalCount 가져오기
		int totalCount = logService.getLogSearchCountForPaging(logSearchDTO);
		logSearchDTO.getPageDTO().setTotalCount(totalCount);
		
		List<LogDTO> logSerachList = totalCount > 0 ? logService.getLogSearchList(logSearchDTO) : List.of();
		
		model.addAttribute("logSearchDTO", logSearchDTO);
		// 로그 리스트 가져와서 뷰에 전달
		model.addAttribute("logDTOList", logSerachList);
		// 리스트 검색 파라미터로 활용할 LogSearchDTO 객체 생성 후 Model에 저장
		model.addAttribute("accessTypeList", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_ACCESS_TYPE)); // 로그 엑세스타입 셀렉트박스용
		model.addAttribute("accessDeviceList", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_ACCESS_DEVICE)); // 로그 엑세스디바이스 셀렉트박스용
		
		return VIEW_PATH+"/log_list";
	}
	
	/**
	 * 어드민 > 시스템 사용 로그 > 리스트 뷰
	 * <br> 인터셉터에서 입력된 로그 테이블을 조회하는 기능
	 * @param model
	 * @return
	 */
	@GetMapping(value={"/search","/search/"})
	public String getLogSearchList(@ModelAttribute LogSearchDTO logSearchDTO, Model model) {
		LogUtil.logStart(log);
		LogUtil.logDetail(log, "requestDTO : {}", StringUtil.objToString(logSearchDTO));
		
		// 페이징용 totalCount 가져오기
		int totalCount = logService.getLogSearchCountForPaging(logSearchDTO);
		logSearchDTO.getPageDTO().setTotalCount(totalCount);
		
		List<LogDTO> logSerachList = totalCount > 0 ? logService.getLogSearchList(logSearchDTO) : List.of();
		
		model.addAttribute("logSearchDTO", logSearchDTO);
		// 로그 리스트 가져와서 뷰에 전달
		model.addAttribute("logDTOList", logSerachList);
		// 리스트 검색 파라미터로 활용할 LogSearchDTO 객체 생성 후 Model에 저장
		model.addAttribute("accessTypeList", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_ACCESS_TYPE)); // 로그 엑세스타입 셀렉트박스용
		model.addAttribute("accessDeviceList", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_ACCESS_DEVICE)); // 로그 엑세스디바이스 셀렉트박스용
		
		return VIEW_PATH+"/log_list";
	}
	
	@GetMapping("/{idx}")
	public String getLog(@PathVariable("idx") String idx, Model model) {
		LogUtil.logStart(log);
		LogUtil.logDetail(log, "requestData : {}", idx);
		
		// idx 값이 숫자가 아닐 때 리스트로 리다이렉트
		if(!StringUtil.isLongValue(idx)) {
			return "redirect:"+VIEW_PATH;
		}
		
		LogDTO logDTO = logService.getLog(Long.parseLong(idx));
		
		model.addAttribute("accessTypeItemList", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_ACCESS_TYPE));
		model.addAttribute("accessDeviceItemList", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_ACCESS_DEVICE));
		model.addAttribute("logDTO", logDTO);
		
		return VIEW_PATH+"/log_detail";
	}
	
	
	/* ==============================
	 * ==========통곡의 벽이길==========
	 * ==============================
	*/ 
	
	/**
	 * 필요 시 파트너 로그인 부분 구현
	 * @param model
	 * @return
	 */
	@GetMapping(value={"/partner","/partner/"})
	public String LogGetPartnerLogList(Model model) {
		LogUtil.logStart(log);
		
		return "/admin/logManager/partner_log_list";
	}
}
