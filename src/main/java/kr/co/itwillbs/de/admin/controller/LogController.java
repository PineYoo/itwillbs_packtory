package kr.co.itwillbs.de.admin.controller;

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
import kr.co.itwillbs.de.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/log")
public class LogController {
	
	private final LogService logService;
	private final CommonCodeUtil commonCodeUtil;
	//@Autowired
	public LogController(LogService logService, CommonCodeUtil commonCodeUtil) {
		this.logService = logService;
		this.commonCodeUtil = commonCodeUtil;
	}
	
	// 계속 사용하게 될 클래스 RequestMapping 문자열 값
	private final String LOG_PATH="/admin/log";
	private final String COMMON_MAJOR_CODE_ACCESS_TYPE = "log_access_type";
	private final String COMMON_MAJOR_CODE_ACCESS_DEVICE = "log_access_device";
	/**
	 * (개발테스트용)어드민 > 시스템 로그 > 로그 등록
	 * @param model
	 * @return
	 */
	@GetMapping(value="/new")
	public String logRegisterForm(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		model.addAttribute("logDTO", new LogDTO());
		
		return LOG_PATH+"/log_register_form";
	}
	
	/**
	 * (개발테스트용)어드민 > 시스템 로그 > 로그 등록(INSERT)
	 * @param logDTO
	 * @return
	 */
	@PostMapping(value={"","/"})
	public String logRegister(@ModelAttribute("logDTO") LogDTO logDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("requestData : {}", StringUtil.objToString(logDTO));
		
		if(logService.registerLog(logDTO) < 1) {
			return LOG_PATH+"/log_register_form";
		}
		return "redirect:"+LOG_PATH;
	}
	
	/**
	 * 어드민 > 시스템 사용 로그 > 리스트 뷰
	 * @param model
	 * @return
	 */
	@GetMapping(value= {"","/"})
	public String getLogList(@ModelAttribute LogSearchDTO logSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 리스트 검색 파라미터로 활용할 LogSearchDTO 객체 생성 후 Model에 저장
		model.addAttribute("accessTypeList", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_ACCESS_TYPE)); // 로그 엑세스타입 셀렉트박스용
		model.addAttribute("accessDeviceList", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_ACCESS_DEVICE)); // 로그 엑세스디바이스 셀렉트박스용
		
		// 페이징용 totalCount 가져오기
		logSearchDTO.getPageDTO().setTotalCount(logService.getLogSearchCountForPaging(logSearchDTO));
		model.addAttribute("logSearchDTO", logSearchDTO);
		// 로그 리스트 가져와서 뷰에 전달
		model.addAttribute("logDTOList", logService.getLogSearchList(logSearchDTO));
		
		return LOG_PATH+"/log_list";
	}
	
	/**
	 * 어드민 > 시스템 사용 로그 > 리스트 뷰
	 * <br> 인터셉터에서 입력된 로그 테이블을 조회하는 기능
	 * @param model
	 * @return
	 */
	@GetMapping(value={"/search","/search/"})
	public String getLogSearchList(@ModelAttribute LogSearchDTO logSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("requestData : {}", StringUtil.objToString(logSearchDTO));
		
		model.addAttribute("accessTypeList", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_ACCESS_TYPE)); // 로그 엑세스타입 셀렉트박스용
		model.addAttribute("accessDeviceList", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_ACCESS_DEVICE)); // 로그 엑세스디바이스 셀렉트박스용

		// 페이징용 totalCount 가져오기
		logSearchDTO.getPageDTO().setTotalCount(logService.getLogSearchCountForPaging(logSearchDTO));
		model.addAttribute("logSearchDTO", logSearchDTO);
		// 로그 리스트 가져와서 뷰에 전달
		model.addAttribute("logDTOList", logService.getLogSearchList(logSearchDTO));
		
		return LOG_PATH+"/log_list";
	}
	
	@GetMapping("/{idx}")
	public String getLog(@PathVariable("idx") String idx, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("requestData : {}", idx);
		
		// idx 값이 숫자가 아닐 때 리스트로 리다이렉트
		if(!StringUtil.isLongValue(idx)) {
			return "redirect:"+LOG_PATH;
		}
		LogDTO logDTO = logService.getLog(Long.parseLong(idx));
		model.addAttribute("accessTypeItemList", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_ACCESS_TYPE));
		model.addAttribute("accessDeviceItemList", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_ACCESS_DEVICE));
		model.addAttribute("logDTO", logDTO);
		
		return LOG_PATH+"/log_detail";
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
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return "/admin/logManager/partner_log_list";
	}
}
