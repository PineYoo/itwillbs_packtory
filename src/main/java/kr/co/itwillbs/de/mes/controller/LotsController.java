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
import kr.co.itwillbs.de.common.util.StringUtil;
import kr.co.itwillbs.de.mes.dto.LotsDTO;
import kr.co.itwillbs.de.mes.dto.LotsSearchDTO;
import kr.co.itwillbs.de.mes.service.LotsService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/mes/lots")
public class LotsController {
	
	private final LotsService lotsService;
	private final CommonCodeUtil commonCodeUtil;
	
	public LotsController(LotsService lotsService, CommonCodeUtil commonCodeUtil) {
		this.lotsService = lotsService;
		this.commonCodeUtil = commonCodeUtil;
	}
	
	// 계속 사용하게 될 클래스 RequestMapping 문자열 값
	private final String VIEW_PATH="/mes/lots";
	
	/**
	 * MES > lots > lot 등록 화면 GET
	 * @param model
	 * @return Strings
	 */
	@GetMapping(value={"/new"})
	private String registerLotsForm(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// thymeleaf th:object 용 모델 셋
		model.addAttribute("lotsDTO", new LotsDTO());
		
		return VIEW_PATH+"/lots_register_form";
	}
	
	/**
	 * MES > lots > lot 등록 POST(form)
	 * <br> @Valid 공통에 문제가 있어서 Post를 전부 Ajax로 대체함
	 * @param lotsDTO
	 * @param model
	 * @return String
	 */
	// 파일을 받을 경우 MediaType.MULTIPART_FORM_DATA_VALUE
	@PostMapping(value= {"", "/"}, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.TEXT_HTML_VALUE})
	private String registerLot(@ModelAttribute @Valid LotsDTO lotsDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("requestDTO : {}", StringUtil.objToString(lotsDTO));

		try {
			lotsService.registerLots(lotsDTO);
		} catch(RuntimeException e) {
			log.error(e.getMessage());
		}catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return "redirect:"+VIEW_PATH;
	}
	
	/**
	 * MES > lots > lot 등록 POST(ajax)
	 * @param lotsDTO
	 * @param model
	 * @return ResponseEntity<Map<String, Object>>
	 */
	@PostMapping(value= {"", "/"}, consumes= {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	private ResponseEntity<Map<String, Object>> registerLotForJson(@RequestBody @Valid LotsDTO lotsDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("requestDTO : {}", StringUtil.objToString(lotsDTO));
		
		//리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			lotsService.registerLots(lotsDTO);
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
	 * MES > lots > lot 리스트
	 * @param lotsSearchDTO
	 * @param model
	 * @return String
	 */
	@GetMapping(value= {"", "/"})
	public String getLots(@ModelAttribute LotsSearchDTO lotsSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		lotsSearchDTO.getPageDTO().setTotalCount(lotsService.getLotsCountBySearchDTO(lotsSearchDTO));
//		setcodeItems(recipeMaterialSearchDTO);
		model.addAttribute("lotsSearchDTO", lotsSearchDTO);
		
		model.addAttribute("lotsDTOList", lotsService.getRecipeLotsBySearchDTO(lotsSearchDTO));
		
		return VIEW_PATH+"/lots_list";
	}
	
	/**
	 * MES > lots > lot 검색 조건 조회 
	 * @param lotsSearchDTO
	 * @param model
	 * @return String
	 */
	@GetMapping(value= {"/search", "/search/"})
	public String getLotsBySearchDTO(@ModelAttribute LotsSearchDTO lotsSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		lotsSearchDTO.getPageDTO().setTotalCount(lotsService.getLotsCountBySearchDTO(lotsSearchDTO));
//		setcodeItems(recipeMaterialSearchDTO);
		model.addAttribute("lotsSearchDTO", lotsSearchDTO);
		
		model.addAttribute("lotsDTOList", lotsService.getRecipeLotsBySearchDTO(lotsSearchDTO));
		
		
		return VIEW_PATH+"/lots_list";
	}
	
	/**
	 * MES > lots > lot 상세 조회
	 * @param idx
	 * @param lotsSearchDTO
	 * @param model
	 * @return String
	 */
	@GetMapping("/{idx}")
	public String getLot(@PathVariable("idx") String idx,
							@ModelAttribute LotsSearchDTO lotsSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("request idx : {}, recipeSearchDTO : {}", idx, lotsSearchDTO);
		
		// idx 값이 숫자가 아닐 때 리스트로 리다이렉트
		if(!StringUtil.isLongValue(idx)) {
			model.addAttribute("lotsSearchDTO", lotsSearchDTO);
			return "redirect:"+VIEW_PATH;
		}
		
		model.addAttribute("lotsDTO", lotsService.getLotsByIdx(idx));
		
		return VIEW_PATH+"/lots_detail";
	}
	
	/**
	 * MES > lots > lot 상세 업데이트
	 * @param lotsDTO
	 * @return ResponseEntity<Map<String, Object>>
	 */
	@PutMapping("/modifyLots")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modifyLot(@RequestBody @Valid LotsDTO lotsDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("requestBody : {}", lotsDTO);
		
		//리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			lotsService.modifyLots(lotsDTO);
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
//	private void setcodeItems(RecipeMaterialSearchDTO recipeMaterialSearchDTO) {
//		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
//	}
	
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
}
