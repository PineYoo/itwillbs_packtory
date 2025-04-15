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
import kr.co.itwillbs.de.mes.dto.BomDTO;
import kr.co.itwillbs.de.mes.dto.BomSearchDTO;
import kr.co.itwillbs.de.mes.service.BomService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/mes/bom")
public class BomController {
/**
 * currency rate(환율) 정보를 어떻게 관리하지? 이건 실시간으로 해야 의미가 있다!
 * https://www.data.go.kr/data/3068846/openapi.do 환율 api
 */
	
	private final BomService bomService;
	private final CommonCodeUtil commonCodeUtil;
	
	public BomController(BomService bomService, CommonCodeUtil commonCodeUtil) {
		this.bomService = bomService;
		this.commonCodeUtil = commonCodeUtil;
	}
	
	// 계속 사용하게 될 클래스 RequestMapping 문자열 값
	private final String BOM_PATH="/mes/bom";
	private final String COMMON_MAJOR_CODE_BOM_TYPE = "bom_type";
	
	/**
	 * MES > BOM > BOM 등록 화면 GET
	 * @param model
	 * @return
	 */
	@GetMapping(value={"/new"})
	private String registerForm(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// thymeleaf th:object 용 모델 셋
		model.addAttribute("typeList", this.getCodeItems(COMMON_MAJOR_CODE_BOM_TYPE));
		model.addAttribute("bomDTO", new BomDTO());
		
		return BOM_PATH+"/bom_register_form";
	}
	
	/**
	 * MES > BOM > BOM 등록 POST(from)
	 * <br> @Valid 공통에 문제가 있어서 Post를 전부 Ajax로 대체함
	 * @param bomDTO
	 * @param model
	 * @return
	 */
	// 파일을 받을 경우 MediaType.MULTIPART_FORM_DATA_VALUE
	@Deprecated
	@PostMapping(value= {"", "/"}, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.TEXT_HTML_VALUE})
	private String registerBom(@ModelAttribute @Valid BomDTO bomDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("requestDTO : {}", StringUtil.objToString(bomDTO));

		try {
			bomService.registerBom(bomDTO);
		} catch(RuntimeException e) {
			log.error(e.getMessage());
		}catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return "redirect:"+BOM_PATH;
	}
	
	/**
	 * MES > BOM > BOM 등록 POST(ajax)
	 * @param bomDTO
	 * @param model
	 * @return
	 */
	@PostMapping(value= {"", "/"}, consumes= {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	private ResponseEntity<Map<String, Object>> registerBomForJson(@RequestBody @Valid BomDTO bomDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("requestDTO : {}", StringUtil.objToString(bomDTO));
		
		//리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			bomService.registerBom(bomDTO);
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
	 * MES > BOM > BOM 리스트
	 * @param bomSearchDTO
	 * @param model
	 * @return
	 */
	@GetMapping(value= {"", "/"})
	public String getBoms(@ModelAttribute BomSearchDTO bomSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		bomSearchDTO.getPageDTO().setTotalCount(bomService.getBomsCountBySearchDTO(bomSearchDTO));
		setcodeItems(bomSearchDTO);
		model.addAttribute("bomSearchDTO", bomSearchDTO);
		
		model.addAttribute("bomDTOList", bomService.getBomsBySearchDTO(bomSearchDTO));
		
		return BOM_PATH+"/bom_list";
	}
	
	@GetMapping(value= {"/search", "/search/"})
	public String getBomsBySearchDTO(@ModelAttribute BomSearchDTO bomSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		bomSearchDTO.getPageDTO().setTotalCount(bomService.getBomsCountBySearchDTO(bomSearchDTO));
		setcodeItems(bomSearchDTO);
		model.addAttribute("bomSearchDTO", bomSearchDTO);
		
		model.addAttribute("bomDTOList", bomService.getBomsBySearchDTO(bomSearchDTO));
		
		return BOM_PATH+"/bom_list";
	}
	
	@GetMapping("/{idx}")
	public String getBom(@PathVariable("idx") String idx,
							@ModelAttribute BomSearchDTO bomSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("request idx : {}, bomSearchDTO : {}", idx, bomSearchDTO);
		
		// idx 값이 숫자가 아닐 때 리스트로 리다이렉트
		if(!StringUtil.isLongValue(idx)) {
			model.addAttribute("bomSearchDTO", bomSearchDTO);
			return "redirect:"+BOM_PATH;
		}
		
		model.addAttribute("typeList", this.getCodeItems(COMMON_MAJOR_CODE_BOM_TYPE));
		model.addAttribute("bomDTO", bomService.getBomByIdx(idx));
		
		return BOM_PATH+"/bom_detail";
	}
	
	@PutMapping("/modifyBom")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modifyBom(@RequestBody @Valid BomDTO bomDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("requestBody : {}", bomDTO);
		
		//리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			bomService.modifyBom(bomDTO);
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
	 * @param bomSearchDTO
	 * @return
	 */
	private void setcodeItems(BomSearchDTO bomSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		bomSearchDTO.setTypeList(this.getCodeItems(COMMON_MAJOR_CODE_BOM_TYPE));
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
