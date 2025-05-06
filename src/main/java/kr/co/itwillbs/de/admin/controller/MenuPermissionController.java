package kr.co.itwillbs.de.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import kr.co.itwillbs.de.admin.dto.MenuPermissionDTO;
import kr.co.itwillbs.de.admin.dto.MenuPermissionSearchDTO;
import kr.co.itwillbs.de.admin.service.MenuPermissionService;
import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value="/admin/menu/permission", name = "메뉴 권한 관리")
public class MenuPermissionController {

	private final MenuPermissionService menuPermissionService;
	
	public MenuPermissionController(MenuPermissionService menuPermissionService) {
		this.menuPermissionService = menuPermissionService;
	}
	
	// 계속 사용하게 될 클래스 RequestMapping 문자열 값
	private final String VIEW_PATH="/admin/menu/permission";
	
	/**
	 * 어드민 > 메뉴 권한 관리 > 등록
	 * @param model
	 * @return
	 */
	@GetMapping(value="/new", name = "권한 등록")
	public String permissionRegisterForm(Model model) {
		LogUtil.logStart(log);
		
		// Form th:object를 위한 DTO 뷰에 전달
		model.addAttribute("permissionDTO", new MenuPermissionDTO());
		
		return VIEW_PATH+"/permission_register_form";
	}
	
	/**
	 * 어드민 > 메뉴 권한 관리 > 등록(INSERT)
	 * @param logDTO
	 * @return
	 */
	@PostMapping(value={"","/"})
	public String permissionRegister(@ModelAttribute("menuDTO") MenuPermissionDTO dto) {
		LogUtil.logStart(log);
		LogUtil.logDetail(log, "requestData : {}", StringUtil.objToString(dto));
		
		if(menuPermissionService.registerMenuPermission(dto) < 1) {
			return VIEW_PATH+"/log_register_form";
		}
		return "redirect:"+VIEW_PATH;
	}
	
	/**
	 * 어드민 > 메뉴 권한 관리 > 권한 리스트
	 * @param model
	 * @return
	 */
	@GetMapping(value= {"","/"}, name = "권한 리스트")
	public String getPermissions(@ModelAttribute MenuPermissionSearchDTO searchDTO, Model model) {
		LogUtil.logStart(log);
		
		// 리스트 검색 DTO 빈 값 뷰에 전달
		searchDTO.getPageDTO().setTotalCount(menuPermissionService.getMenuPermissionCountBySearchDTO(searchDTO));
		model.addAttribute("searchDTO", searchDTO);
		
		// 리스트 결과 DTOlist 뷰에 전달
		List<MenuPermissionDTO> dtoList = menuPermissionService.getMenuPermissionsBySearchDTO(searchDTO);
		model.addAttribute("permissionDTOList", dtoList);
		
		return VIEW_PATH+"/permission_list";
	}
	
	/**
	 * 어드민 > 메뉴 권한 관리 > 메뉴 검색 조건 조회
	 * @param menuSearchDTO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value="/search", name = "권한 조건 검색 리스트")
	public String getPermissionsBySearchDTO(@ModelAttribute MenuPermissionSearchDTO searchDTO, Model model) throws Exception {
		LogUtil.logStart(log);
		LogUtil.logDetail(log, "requestData : {}", searchDTO);
		
		// 리스트 검색 DTO 빈 값 뷰에 전달
		searchDTO.getPageDTO().setTotalCount(menuPermissionService.getMenuPermissionCountBySearchDTO(searchDTO));
		model.addAttribute("searchDTO", searchDTO);
		
		// 리스트 결과 DTOlist 뷰에 전달
		List<MenuPermissionDTO> dtoList = menuPermissionService.getMenuPermissionsBySearchDTO(searchDTO);
		model.addAttribute("permissionDTOList", dtoList);
		
		return VIEW_PATH+"/permission_list";
	}	
	
	/**
	 * 어드민 > 메뉴 권한 관리 > 권한 상세
	 * @param id t_menu.idx 값 
	 * @param model menuDTO(1depth), menuDTOIdList(2depth)
	 * @return
	 */
	@GetMapping(value="/{idx}", name="권한 상세")
	public String getPermissionByIdx(@PathVariable("idx") String idx,
								@ModelAttribute MenuPermissionSearchDTO searchDTO, Model model) {
		LogUtil.logStart(log);
		LogUtil.logDetail(log, "request param : {}, requestDTO {}", idx, searchDTO);
		
		// idx 값이 숫자가 아닐 때 리스트로 리다이렉트
		if(!StringUtil.isLongValue(idx)) {
			return "redirect:"+VIEW_PATH;
		}
		
		try {
			// 조회
			MenuPermissionDTO dto = menuPermissionService.getMenuPermissionsByIdx(idx);
			model.addAttribute("permissionDTO", dto);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			return "redirect:"+VIEW_PATH;
		}
		
		return VIEW_PATH+"/permission_detail";
	}

	/**
	 * 어드민 > 메뉴 목록 > 하위 메뉴 등록 -> 화면 상단의 메뉴 타입 업데이트
	 * <br> 만약, 2Depth 메뉴와 함께 수정하라고 하면.. 조금 머리가 아파질것 같다
	 * @param menuDTO
	 * @param model
	 * @return
	 */
	@PutMapping("/modify")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modifyPermission(@RequestBody MenuPermissionDTO dto,  Model model) {
		LogUtil.logStart(log);
		LogUtil.logDetail(log, "requestBodyData : {}", dto);
		
		//리턴 객체
		Map<String, Object> response = new HashMap<>();
		try {
			menuPermissionService.modifyMenuPermission(dto);
			response.put("status", "success");
			response.put("message", "정상적으로 수행 되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "fail");
			response.put("message", "정상적으로 수행되지 않았습니다.\n 잠시 후 다시 시도해주시기 바랍니다.");
		}
		
		return ResponseEntity.ok(response);
	}
	
}
