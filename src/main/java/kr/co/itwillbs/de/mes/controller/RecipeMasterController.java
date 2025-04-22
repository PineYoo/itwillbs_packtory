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
import kr.co.itwillbs.de.mes.dto.RecipeProcessDTO;
import kr.co.itwillbs.de.mes.dto.RecipeMaterialDTO;
import kr.co.itwillbs.de.mes.dto.RecipeMaterialSearchDTO;
import kr.co.itwillbs.de.mes.dto.RecipeMasterDTO;
import kr.co.itwillbs.de.mes.dto.RecipeMasterSearchDTO;
import kr.co.itwillbs.de.mes.service.RecipeMasterService;
import kr.co.itwillbs.de.mes.service.RecipeMaterailService;
import kr.co.itwillbs.de.mes.service.RecipeProcessService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/mes/recipe/master")
public class RecipeMasterController {
/**
 * currency rate(환율) 정보를 어떻게 관리하지? 이건 실시간으로 해야 의미가 있다!
 * https://www.data.go.kr/data/3068846/openapi.do 환율 api
 */
	private final RecipeMasterService recipeMasterService;
	private final CommonCodeUtil commonCodeUtil;
	
	public RecipeMasterController(RecipeMasterService recipeMasterService, CommonCodeUtil commonCodeUtil) {
		this.recipeMasterService = recipeMasterService;
		this.commonCodeUtil = commonCodeUtil;
	}
	
	// 계속 사용하게 될 클래스 RequestMapping 문자열 값
	private final String VIEW_PATH="/mes/recipe/master";
	private final String COMMON_MAJOR_CODE_RECIPE_MASTER_APPROVAL = "RECIPE_MASTER_APPROVAL";
	
	/**
	 * MES > 레시피 > 레시피 등록 화면 GET
	 * @param model
	 * @return
	 */
	@GetMapping(value={"/new"})
	private String registerRecipeMaster(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// thymeleaf th:object 용 모델 셋
		model.addAttribute("recipeMasterDTO", new RecipeMasterDTO());
		
		return VIEW_PATH+"/master_register_form";
	}
	
	/**
	 * MES > 레시피 > 레시피 등록 POST(form)
	 * <br> @Valid 공통에 문제가 있어서 Post를 전부 Ajax로 대체함
	 * @param recipeMasterDTO
	 * @param model
	 * @return String
	 */
	// 파일을 받을 경우 MediaType.MULTIPART_FORM_DATA_VALUE
	@PostMapping(value= {"", "/"}, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.TEXT_HTML_VALUE})
	private String registerRecipeMaster(@ModelAttribute @Valid RecipeMasterDTO recipeMasterDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("requestDTO : {}", StringUtil.objToString(recipeMasterDTO));

		try {
			recipeMasterService.registerRecipeMaster(recipeMasterDTO);
		} catch(RuntimeException e) {
			log.error(e.getMessage());
		}catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return "redirect:"+VIEW_PATH;
	}
	
	/**
	 * MES > 레시피 > 레시피 소요자재 등록 POST(ajax)
	 * @param recipeMasterDTO
	 * @param model
	 * @return ResponseEntity<Map<String, Object>>
	 */
	@PostMapping(value= {"", "/"}, consumes= {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	private ResponseEntity<Map<String, Object>> registerRecipeMasterForJson(@RequestBody @Valid RecipeMasterDTO recipeMasterDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("requestDTO : {}", StringUtil.objToString(recipeMasterDTO));
		
		//리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			recipeMasterService.registerRecipeMaster(recipeMasterDTO);
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
	 * MES > 레시피 > 레시피 리스트
	 * @param recipeMasterSearchDTO
	 * @param model
	 * @return String
	 */
	@GetMapping(value= {"", "/"})
	public String getRecipesMaster(@ModelAttribute RecipeMasterSearchDTO recipeMasterSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		recipeMasterSearchDTO.getPageDTO().setTotalCount(recipeMasterService.getRecipeMasterCountBySearchDTO(recipeMasterSearchDTO));
		setcodeItems(recipeMasterSearchDTO);
		log.info("recipeMasterSearchDTO : {}", recipeMasterSearchDTO);
		model.addAttribute("recipeMasterSearchDTO", recipeMasterSearchDTO);
		
		model.addAttribute("recipeMasterDTOList", recipeMasterService.getRecipeMasterBySearchDTO(recipeMasterSearchDTO));
		
		return VIEW_PATH+"/master_list";
	}
	
	/**
	 * MES > 레시피 > 레시피 검색 조건 조회 
	 * @param recipeMasterSearchDTO
	 * @param model
	 * @return String
	 */
	@GetMapping(value= {"/search", "/search/"})
	public String getRecipesMasterBySearchDTO(@ModelAttribute RecipeMasterSearchDTO recipeMasterSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		recipeMasterSearchDTO.getPageDTO().setTotalCount(recipeMasterService.getRecipeMasterCountBySearchDTO(recipeMasterSearchDTO));
		setcodeItems(recipeMasterSearchDTO);
		model.addAttribute("recipeMasterSearchDTO", recipeMasterSearchDTO);
		
		model.addAttribute("recipeMasterDTOList", recipeMasterService.getRecipeMasterBySearchDTO(recipeMasterSearchDTO));
		
		return VIEW_PATH+"/master_list";
	}
	
	/**
	 * MES > 레시피 > 레시피 상세 조회
	 * @param idx
	 * @param recipeMasterSearchDTO
	 * @param model
	 * @return String
	 */
	@GetMapping("/{idx}")
	public String getRecipeMaster(@PathVariable("idx") String idx,
							@ModelAttribute RecipeMasterSearchDTO recipeMasterSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("request idx : {}, recipeSearchDTO : {}", idx, recipeMasterSearchDTO);
		
		// idx 값이 숫자가 아닐 때 리스트로 리다이렉트
		if(!StringUtil.isLongValue(idx)) {
			model.addAttribute("recipeMasterSearchDTO", recipeMasterSearchDTO);
			return "redirect:"+VIEW_PATH;
		}
		
		model.addAttribute("recipeMasterDTO", recipeMasterService.getRecipeMasterByIdx(idx));
		
		return VIEW_PATH+"/master_detail";
	}
	
	/**
	 * MES > 레시피 > 레시피 상세 업데이트
	 * @param recipeMasterDTO
	 * @return ResponseEntity<Map<String, Object>>
	 */
	@PutMapping("/modifyRecipeMaterial")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modifyRecipeMaster(@RequestBody @Valid RecipeMasterDTO recipeMasterDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("requestBody : {}", recipeMasterDTO);
		
		//리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			recipeMasterService.modifyRecipeMaster(recipeMasterDTO);
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
		recipeMasterSearchDTO.setTypeList(this.getCodeItems(COMMON_MAJOR_CODE_RECIPE_MASTER_APPROVAL));
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
}
