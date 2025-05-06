package kr.co.itwillbs.de.mes.controller;

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
import kr.co.itwillbs.de.mes.dto.RecipeMasterSearchDTO;
import kr.co.itwillbs.de.mes.dto.WorkOrdersFormDTO;
import kr.co.itwillbs.de.mes.dto.WorkOrdersItemsDTO;
import kr.co.itwillbs.de.mes.dto.WorkOrdersItemsSearchDTO;
import kr.co.itwillbs.de.mes.dto.WorkOrdersSearchDTO;
import kr.co.itwillbs.de.mes.service.WorkOrdersService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/mes/workorders")
public class WorkOrdersController {
	
	private final WorkOrdersService workOrdersService;
	private final CommonCodeUtil commonCodeUtil;
	
	public WorkOrdersController(WorkOrdersService workOrdersService, CommonCodeUtil commonCodeUtil) {
		this.workOrdersService = workOrdersService;
		this.commonCodeUtil = commonCodeUtil;
	}
	
	// 계속 사용하게 될 클래스 RequestMapping 문자열 값
	private final String VIEW_PATH="/mes/workorders";
	private final String COMMON_MAJOR_CODE_STATUS = "WORK_ORDERS_STATUS";
	
	/**
	 * MES > 작업지시 등록 화면 GET
	 * @param model
	 * @return
	 */
	@GetMapping(value={"/new"})
	private String registerWorkOrders(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// thymeleaf th:object 용 모델 셋
		model.addAttribute("workOrdersFormDTO", new WorkOrdersFormDTO());
		
		return VIEW_PATH+"/work_orders_register_form";
	}
	
	/**
	 * MES > 작업지시 등록 POST(ajax)
	 * @param workOrdersFormDTO
	 * @param model
	 * @return ResponseEntity<Map<String, Object>>
	 */
	@PostMapping(value= {"", "/"}, consumes= {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	private ResponseEntity<Map<String, Object>> registerWorkOrdersForJson(@RequestBody @Valid WorkOrdersFormDTO workOrdersFormDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("requestDTO : {}", StringUtil.objToString(workOrdersFormDTO));
		
		//리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			workOrdersService.registerWorkOrders(workOrdersFormDTO);
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
	 * MES > 작업지시 리스트 GET
	 * @param workOrdersSearchDTO
	 * @param model
	 * @return String
	 */
	@GetMapping(value= {"", "/"})
	public String getWorkOrders(@ModelAttribute WorkOrdersSearchDTO workOrdersSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		workOrdersSearchDTO.getPageDTO().setTotalCount(workOrdersService.getWorkOrdersCountBySearchDTO(workOrdersSearchDTO));
//		setcodeItems(recipeMasterSearchDTO);
		model.addAttribute("workOrdersSearchDTO", workOrdersSearchDTO);
		
		model.addAttribute("workOrdersMasterDTOList", workOrdersService.getWorkOrdersBySearchDTO(workOrdersSearchDTO));
		
		return VIEW_PATH+"/work_orders_list";
	}
	
	/**
	 * MES > 작업지시 검색 조건 조회 
	 * @param workOrdersSearchDTO
	 * @param model
	 * @return String
	 */
	@GetMapping(value= {"/search", "/search/"})
	public String getWorkOrdersBySearchDTO(@ModelAttribute WorkOrdersSearchDTO workOrdersSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		workOrdersSearchDTO.getPageDTO().setTotalCount(workOrdersService.getWorkOrdersCountBySearchDTO(workOrdersSearchDTO));
//		setcodeItems(workOrdersSearchDTO);
		model.addAttribute("workOrdersSearchDTO", workOrdersSearchDTO);
		
		model.addAttribute("workOrdersMasterDTOList", workOrdersService.getWorkOrdersBySearchDTO(workOrdersSearchDTO));
		
		return VIEW_PATH+"/work_orders_list";
	}
	
	/**
	 * MES > 작업지시 상세 조회
	 * @param idx
	 * @param workOrdersSearchDTO
	 * @param model
	 * @return String
	 */
	@GetMapping("/{idx}")
	public String getWorkOrder(@PathVariable("idx") String idx,
							@ModelAttribute WorkOrdersSearchDTO workOrdersSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("request idx : {}, recipeSearchDTO : {}", idx, workOrdersSearchDTO);
		
		// idx 값이 숫자가 아닐 때 리스트로 리다이렉트
		if(!StringUtil.isLongValue(idx)) {
			model.addAttribute("workOrdersSearchDTO", workOrdersSearchDTO);
			return "redirect:"+VIEW_PATH;
		}
		
		model.addAttribute("workOrdersFormDTO", workOrdersService.getWorkOrdersByIdx(idx));
		
		return VIEW_PATH+"/work_orders_detail";
	}
	
	/**
	 * MES > 작업지시 상세 업데이트
	 * @param workOrdersFormDTO
	 * @return ResponseEntity<Map<String, Object>>
	 */
	@PutMapping("/modifyWorkOrders")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modifyWorkOrders(@RequestBody @Valid WorkOrdersFormDTO workOrdersFormDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("requestBody : {}", workOrdersFormDTO);
		
		//리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			workOrdersService.modifyWorkOrders(workOrdersFormDTO);
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
	 * 검색 폼에 사용할 공통코드 가져오기
	 * <br>이것도 고민. 객체지향 스럽게 class를 return 타입으로 메서드를 만드드냐 -> 이건 뉴하는거라 메모리가..
	 * <br> 아규로 전달 받은 class에 set 하고 끝내느냐...?
	 * <br> 무엇이 더 직관적일까?
	 * @param recipeSearchDTO
	 * @return
	 */
	private void setcodeItems(RecipeMasterSearchDTO recipeMasterSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
//		recipeMasterSearchDTO.setTypeList(this.getCodeItems(COMMON_MAJOR_CODE_RECIPE_MASTER_APPROVAL));
	}
	
	/**
	 * 입력 수정 화면에서 사용할 list
	 * <br> 이거 맞아?
	 * @param str
	 * @return
	 */
	private List<CodeItemDTO> getCodeItems(String str) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return commonCodeUtil.getCodeItems(str);
	}
	
	/**
	 * <pre>
	 * 공장에 관리 권한이 있는 사람이 접속하여
	 * 작업지시 문서에 실제 값을 보고하기 위함
	 * MES > ? > 작업 현황 보고 리스트
	 * </pre>
	 * @return
	 */
	@GetMapping(value={"/items", "/items/"})
	public String getWorkOrderItems(@ModelAttribute WorkOrdersItemsSearchDTO searchDTO, Model model) {
		LogUtil.logStart(log);
		
		int totalCount = workOrdersService.selectWorkItemsCountBySearchDTO(searchDTO);
		searchDTO.getPageDTO().setTotalCount(totalCount);
		List<WorkOrdersItemsDTO> list = totalCount > 0 
							? workOrdersService.selectWorkItemsListBySearchDTO(searchDTO) 
							: List.of();
		
		model.addAttribute("codeItems", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_STATUS));
		model.addAttribute("searchDTO", searchDTO);
		model.addAttribute("workItems", list);
		
		return VIEW_PATH+"/work_items_list";
	}
	
	/**
	 * 작업 현황 보고서 상세보기
	 * @param idx
	 * @param model
	 * @return
	 */
	@GetMapping(value={"/items/{idx}"})
	public String getWorkOrderItem(@PathVariable("idx") String idx, Model model) {
		LogUtil.logStart(log);
		
		model.addAttribute("workItemDTO", workOrdersService.selectWorkItemByIdx(idx));
		model.addAttribute("codeItems", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_STATUS));
		
		return VIEW_PATH+"/work_items_detail";
	}
	
	/**
	 * 작업 현황 보고 업데이트 하기
	 * @param dto
	 * @return
	 */
	@PutMapping("/modifyWorkItem")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modifyWorkItem(@RequestBody @Valid WorkOrdersItemsDTO dto) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("requestBody : {}", dto);
		
		//리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			workOrdersService.modifyWorkOrdersItems(dto);
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
}
