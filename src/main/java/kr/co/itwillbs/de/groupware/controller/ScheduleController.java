package kr.co.itwillbs.de.groupware.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;
import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.common.util.StringUtil;
import kr.co.itwillbs.de.groupware.dto.ScheduleDTO;
import kr.co.itwillbs.de.groupware.dto.ScheduleSearchDTO;
import kr.co.itwillbs.de.groupware.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/groupware/schedule")
public class ScheduleController {
	
	private final ScheduleService scheduleService;
	private final CommonCodeUtil commonCodeUtil;
	
	public ScheduleController(ScheduleService scheduleService, CommonCodeUtil commonCodeUtil) {
		this.scheduleService = scheduleService;
		this.commonCodeUtil = commonCodeUtil;
	}
	
	// 계속 사용하게 될 클래스 RequestMapping 문자열 값
	private final String VIEW_PATH="/groupware/schedule";
	private final String COMMON_MAJOR_DEPARTMENT_CODE = "department_code";
	
	/**
	 * 그룹웨어 > 일정관리 > 등록 화면 GET
	 * @param model
	 * @return
	 */
	@GetMapping(value={"/new"})
	private String registerForm(Model model) {
		LogUtil.logStart(log);
		
		// thymeleaf th:object 용 모델 셋
		model.addAttribute("codeItems", this.getCodeItems(COMMON_MAJOR_DEPARTMENT_CODE));
		model.addAttribute("scheduleDTO", new ScheduleDTO());
		
		return VIEW_PATH+"/schedule_register_form";
	}
	
	/**
	 * 그룹웨어 > 일정관리 > 등록 POST(ajax)
	 * @param ScheduleDTO
	 * @param model
	 * @return
	 */
	@PostMapping(value= {"", "/"}, consumes= {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	private ResponseEntity<Map<String, Object>> registerScheduleForJson(@RequestBody @Valid ScheduleDTO dto) {
		LogUtil.logStart(log);
		LogUtil.logDetail(log,"requestDTO : {}", StringUtil.objToString(dto));
		
		//리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			scheduleService.registerSchedule(dto);
			response.put("status", "success");
			response.put("message", "정상적으로 수행 되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "fail");
			response.put("message", "정상적으로 수행되지 않았습니다.\n 잠시 후 다시 시도해주시기 바랍니다.");
		}
		
		// 비동기 통신 success에 들어가는 것은 HTTP 200||201 이 아니었나? 하는 기억에 리턴 객체 만듦
		return ResponseEntity.ok(response);
	}
	
	/**
	 * 그룹웨어 > 일정관리 > 리스트
	 * @param ScheduleSearchDTO
	 * @param model
	 * @return
	 */
	@GetMapping(value= {"", "/"})
	public String getSchedules(@ModelAttribute ScheduleSearchDTO searchDTO, Model model) {
		LogUtil.logStart(log);

		int totalCount = scheduleService.getSchedulesCountBySearchDTO(searchDTO);
		searchDTO.getPageDTO().setTotalCount(totalCount);
		
		List<ScheduleDTO> dtoList = totalCount > 0 ? scheduleService.getSchedulesBySearchDTO(searchDTO) : List.of();
		
		model.addAttribute("searchDTO", searchDTO);
		model.addAttribute("DTOList", dtoList);
		
		return VIEW_PATH+"/schedule_list";
	}
	
	/**
	 * 그룹웨어 > 일정관리 > 검색 조건 조회 
	 * @param ScheduleSearchDTO
	 * @param model
	 * @return
	 */
	@GetMapping(value= {"/search", "/search/"})
	public String getSchedulesBySearchDTO(@ModelAttribute ScheduleSearchDTO searchDTO, Model model) {
		LogUtil.logStart(log);

		int totalCount = scheduleService.getSchedulesCountBySearchDTO(searchDTO);
		searchDTO.getPageDTO().setTotalCount(totalCount);
		
		List<ScheduleDTO> dtoList = totalCount > 0 ? scheduleService.getSchedulesBySearchDTO(searchDTO) : List.of();
		
		model.addAttribute("searchDTO", searchDTO);
		model.addAttribute("DTOList", dtoList);
		
		return VIEW_PATH+"/schedule_list";
	}
	
	/**
	 * 그룹웨어 > 일정관리 > 상세 조회
	 * @param idx
	 * @param ScheduleSearchDTO
	 * @param model
	 * @return
	 */
	@GetMapping("/{idx}")
	public String getSchedule(@PathVariable("idx") String idx,
							@ModelAttribute ScheduleSearchDTO searchDTO, Model model) {
		LogUtil.logStart(log);
		LogUtil.logDetail(log,"request idx : {}, bomSearchDTO : {}", idx, StringUtil.objToString(searchDTO));
		
		// idx 값이 숫자가 아닐 때 리스트로 리다이렉트
		if(!StringUtil.isLongValue(idx)) {
			model.addAttribute("bomSearchDTO", searchDTO);
			return "redirect:"+VIEW_PATH;
		}
		
		model.addAttribute("typeList", this.getCodeItems(COMMON_MAJOR_DEPARTMENT_CODE));
		model.addAttribute("bomDTO", scheduleService.getScheduleByIdx(idx));
		
		return VIEW_PATH+"/schedule_detail";
	}
	
	/**
	 * 그룹웨어 > 일정관리 > 상세 업데이트
	 * @param ScheduleDTO
	 * @return
	 */
	@PutMapping("/modify")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modifySchedule(@RequestBody @Valid ScheduleDTO dto) {
		LogUtil.logStart(log);
		LogUtil.logDetail(log,"requestBody : {}", dto);
		
		//리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			scheduleService.modifySchedule(dto);
			response.put("status", "success");
			response.put("message", "정상적으로 수행 되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "fail");
			response.put("message", "정상적으로 수행되지 않았습니다.\n 잠시 후 다시 시도해주시기 바랍니다.");
		}
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * 입력 수정 화면에서 사용할 list
	 * <br> 이거 맞아?
	 * @param str
	 * @return
	 */
	private List<CodeItemDTO> getCodeItems(String str) {
		LogUtil.logStart(log);
		return commonCodeUtil.getCodeItems(str);
	}
}
