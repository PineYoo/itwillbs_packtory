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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;
import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.common.util.StringUtil;
import kr.co.itwillbs.de.mes.dto.RecipeProcessDTO;
import kr.co.itwillbs.de.mes.dto.RecipeProcessSearchDTO;
import kr.co.itwillbs.de.mes.service.RecipeProcessService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/mes/recipe/process")
public class RecipeProcessController {
	
	private final RecipeProcessService recipeProcessService;
	private final CommonCodeUtil commonCodeUtil;
	
	public RecipeProcessController(RecipeProcessService recipeProcessService, CommonCodeUtil commonCodeUtil) {
		this.recipeProcessService = recipeProcessService;
		this.commonCodeUtil = commonCodeUtil;
	}
	
	// 계속 사용하게 될 클래스 RequestMapping 문자열 값
	private final String RECIPE_PATH="/mes/recipe/process";
//	private final String COMMON_MAJOR_CODE_RECIPE_TYPE = "RECIPE_TYPE";
//	private final String COMMON_MAJOR_CODE_BOM_TYPE = "BOM_TYPE";
//	private final String COMMON_MAJOR_CODE_PRODUCT_TYPE = "PRODUCT_TYPE";
	
	/**
	 * MES > 레시피 > 레시피 공정 등록 화면 GET
	 * @param model
	 * @return
	 */
	@GetMapping(value={"/new"})
	private String registerRecipeForm(Model model,
									  @ModelAttribute RecipeProcessDTO recipeProcessDTO, 
									  @RequestParam("master_idx") String masterIdx) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		if (masterIdx != null) {
			recipeProcessDTO.setMasterIdx(masterIdx);
		}
		
		// thymeleaf th:object 용 모델 셋
		model.addAttribute("recipeProcessDTO", recipeProcessDTO);
//		model.addAttribute("recipeProcessDTO", new RecipeProcessDTO());
		
		return RECIPE_PATH+"/process_register_form";
	}
	
	/**
	 * MES > 레시피 > 레시피 공정 등록 POST(form)
	 * <br> @Valid 공통에 문제가 있어서 Post를 전부 Ajax로 대체함
	 * @param recipeProcessDTO
	 * @param model
	 * @return String
	 */
	// 파일을 받을 경우 MediaType.MULTIPART_FORM_DATA_VALUE
	@PostMapping(value= {"", "/"}, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.TEXT_HTML_VALUE})
	private String registerRecipe(@ModelAttribute @Valid RecipeProcessDTO recipeProcessDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("requestDTO : {}", StringUtil.objToString(recipeProcessDTO));

		try {
			recipeProcessService.registerRecipeProcess(recipeProcessDTO);
		} catch(RuntimeException e) {
			log.error(e.getMessage());
		}catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return "redirect:"+RECIPE_PATH;
	}
	
	/**
	 * MES > 레시피 > 레시피 공정 등록 POST(ajax)
	 * @param recipeProcessDTO
	 * @param model
	 * @return ResponseEntity<Map<String, Object>>
	 */
	@PostMapping(value= {"", "/"}, consumes= {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	private ResponseEntity<Map<String, Object>> registerRecipeForJson(@RequestBody @Valid RecipeProcessDTO recipeProcessDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("requestDTO : {}", StringUtil.objToString(recipeProcessDTO));
		
		//리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			recipeProcessService.registerRecipeProcess(recipeProcessDTO);
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
	 * MES > 레시피 > 레시피 공정 리스트
	 * @param recipeProcessSearchDTO
	 * @param model
	 * @return String
	 */
	@GetMapping(value= {"", "/"})
	public String getRecipes(@ModelAttribute RecipeProcessSearchDTO recipeProcessSearchDTO, 
							 @RequestParam(value = "master_idx", required = false) String masterIdx,
				             Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		if (masterIdx != null) {
			recipeProcessSearchDTO.setMasterIdx(masterIdx);
		}
		
		recipeProcessSearchDTO.getPageDTO().setTotalCount(recipeProcessService.getRecipesCountBySearchDTO(recipeProcessSearchDTO));
//		setcodeItems(recipeSearchDTO);
		model.addAttribute("recipeProcessSearchDTO", recipeProcessSearchDTO);
		
		model.addAttribute("recipeProcessDTOList", recipeProcessService.getRecipesProcessBySearchDTO(recipeProcessSearchDTO));
		
		return RECIPE_PATH+"/process_list";
	}
	
	/**
	 * MES > 레시피 > 레시피 공정 검색 조건 조회 
	 * @param recipeProcessSearchDTO
	 * @param model
	 * @return String
	 */
	@GetMapping(value= {"/search", "/search/"})
	public String getRecipesBySearchDTO(@ModelAttribute RecipeProcessSearchDTO recipeProcessSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		recipeProcessSearchDTO.getPageDTO().setTotalCount(recipeProcessService.getRecipesCountBySearchDTO(recipeProcessSearchDTO));
//		setcodeItems(recipeSearchDTO);
		model.addAttribute("recipeProcessSearchDTO", recipeProcessSearchDTO);
		
		model.addAttribute("recipeProcessDTOList", recipeProcessService.getRecipesProcessBySearchDTO(recipeProcessSearchDTO));
		
		return RECIPE_PATH+"/process_list";
	}
	
	/**
	 * MES > 레시피 > 레시피 상세 조회
	 * @param idx
	 * @param recipeProcessSearchDTO
	 * @param model
	 * @return String
	 */
	@GetMapping("/{idx}")
	public String getRecipe(@PathVariable("idx") String idx,
							@ModelAttribute RecipeProcessSearchDTO recipeProcessSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("request idx : {}, recipeSearchDTO : {}", idx, recipeProcessSearchDTO);
		
		// idx 값이 숫자가 아닐 때 리스트로 리다이렉트
		if(!StringUtil.isLongValue(idx)) {
			model.addAttribute("recipeProcessSearchDTO", recipeProcessSearchDTO);
			return "redirect:"+RECIPE_PATH;
		}
		
		model.addAttribute("recipeProcessDTO", recipeProcessService.getRecipeProcessByIdx(idx));
		
		return RECIPE_PATH+"/process_detail";
	}
	
	/**
	 * MES > 레시피 > 레시피 공정 상세 업데이트
	 * @param recipeProcessDTO
	 * @return ResponseEntity<Map<String, Object>>
	 */
	@PutMapping("/modifyRecipeProcess")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modifyRecipe(@RequestBody @Valid RecipeProcessDTO recipeProcessDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("requestBody : {}", recipeProcessDTO);
		
		//리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			recipeProcessService.modifyRecipeProcess(recipeProcessDTO);
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
//	private void setcodeItems(RecipeSearchDTO recipeSearchDTO) {
//		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
//		recipeSearchDTO.setTypeList(this.getCodeItems(COMMON_MAJOR_CODE_BOM_TYPE));
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
