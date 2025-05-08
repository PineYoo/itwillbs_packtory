package kr.co.itwillbs.de.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import kr.co.itwillbs.de.admin.dto.MenuDTO;
import kr.co.itwillbs.de.admin.dto.MenuSearchDTO;
import kr.co.itwillbs.de.admin.service.MenuService;
import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value="/admin/menu", name = "메뉴 관리")
public class MenuController {

	private final MenuService menuService;
	
	//@Autowired
	public MenuController(MenuService menuService) {
		this.menuService = menuService;
	}
	
	// 계속 사용하게 될 클래스 RequestMapping 문자열 값
	private final String MENU_PATH="/admin/menu";
	
	/**
	 * 어드민 > 메뉴 관리 > 메뉴 등록
	 * @param model
	 * @return
	 */
	@GetMapping(value="/new", name = "메뉴 등록")
	public String menuRegisterForm(Model model) {
		LogUtil.logStart(log);
		
		// Form th:object를 위한 DTO 뷰에 전달
		model.addAttribute("menuDTO", new MenuDTO());
		// 메뉴 등록을 위한 RequestMapping 리스트
		model.addAttribute("mappingList", menuService.MappingConvertor());
		
		return MENU_PATH+"/menu_register_form";
	}
	
	/**
	 * 어드민 > 메뉴 관리 > 메뉴 등록(INSERT)
	 * @param logDTO
	 * @return
	 */
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping(value={"","/"}, consumes = MediaType.APPLICATION_JSON_VALUE) // 이녀석 만고 필요 없...던데? 
	@ResponseBody
	public ResponseEntity<Map<String, Object>> registerMenu(@RequestBody @Valid MenuDTO menuDTO, Model model) {
		LogUtil.logStart(log);
		LogUtil.logDetail(log, "requestDTO : {}", StringUtil.objToString(menuDTO));
		
		//리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			menuService.registerMenu(menuDTO);
			response.put("status", "success");
			response.put("message", "정상적으로 수행 되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "fail");
			response.put("message", "정상적으로 수행되지 않았습니다.\n 잠시 후 다시 시도해주시기 바랍니다.");
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}
//	public String menuRegister(@ModelAttribute("menuDTO") MenuDTO menuDTO) {
//		LogUtil.logStart(log);
//		LogUtil.logDetail(log, "requestDTO : {}", StringUtil.objToString(menuDTO));
//		
//		if(menuService.registerMenu(menuDTO) < 1) {
//			return MENU_PATH+"/log_register_form";
//		}
//		return "redirect:"+MENU_PATH;
//	}
	
	/**
	 * 어드민 > 메뉴 관리 > 메뉴 리스트 (1depth)
	 * @param model
	 * @return
	 */
	@GetMapping(value= {"","/"}, name = "메뉴 리스트")
	public String getMenuTypeList(@ModelAttribute MenuSearchDTO menuSearchDTO, Model model) {
		LogUtil.logStart(log);
		LogUtil.logDetail(log, "requestDTO : {}", StringUtil.objToString(menuSearchDTO));
		
		// 리스트 검색 DTO 빈 값 뷰에 전달
		menuSearchDTO.getPageDTO().setTotalCount(menuService.getMenuCount(menuSearchDTO));
		model.addAttribute("menuSearchDTO", menuSearchDTO);
		
		// 리스트 결과 DTOlist 뷰에 전달
		List<MenuDTO> menuDTOList = menuService.getMenuList(menuSearchDTO);
		model.addAttribute("menuDTOList", menuDTOList);
		
		return MENU_PATH+"/menu_parents_list";
	}
	
	/**
	 * 어드민 > 메뉴 관리 > 메뉴 검색 조건 조회(1depth)
	 * @param menuSearchDTO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value="/search", name = "메뉴 리스트")
	public String sampleGetList(@ModelAttribute MenuSearchDTO menuSearchDTO, Model model) throws Exception {
		LogUtil.logStart(log);
		LogUtil.logDetail(log, "requestDTO : {}", StringUtil.objToString(menuSearchDTO));
		
		// 리스트 검색 DTO 뷰에 전달
		menuSearchDTO.getPageDTO().setTotalCount(menuService.getMenuCount(menuSearchDTO));
		model.addAttribute("menuSearchDTO", menuSearchDTO);
		
		// 리스트 결과 DTOlist 뷰에 전달
		List<MenuDTO> menuDTOList = menuService.getMenuList(menuSearchDTO);
		model.addAttribute("menuDTOList", menuDTOList);
		
		return MENU_PATH+"/menu_parents_list";
	}	
	
	/**
	 * 어드민 > 메뉴 관리 > 메뉴 상세리스트(2depth)
	 * <br>DB에서는 menuId 값을 보며 (menuId != '0' >>1depth메뉴를 제외한),
	 * <br>menuType이 같은 아이들을 모아서 2depth 메뉴 리스트를 꾸린다.
	 * @param id t_menu.idx 값 
	 * @param model menuDTO(1depth), menuDTOIdList(2depth)
	 * @return
	 */
	@GetMapping(value="/{id}", name="메뉴 상세")
	public String getMenuIdList(@PathVariable("id") String id,
								@ModelAttribute MenuSearchDTO menuSearchDTO, Model model) {
		LogUtil.logStart(log);
		LogUtil.logDetail(log, "request param : {}, requestDTO", id, menuSearchDTO);
		
		// idx 값이 숫자가 아닐 때 리스트로 리다이렉트
		if(!StringUtil.isLongValue(id)) {
			return "redirect:"+MENU_PATH;
		}
		
		try {
			// 그룹 메뉴 조회를 위한 idx값 셋
			menuSearchDTO.setIdx(id);
			// 그룹 메뉴 조회
			MenuDTO menuDTO = menuService.getMenuTypeByIdx(menuSearchDTO);
			model.addAttribute("menuDTO", menuDTO);
			
			menuSearchDTO.setParentsIdx(id);
			model.addAttribute("menuDTOIdList", menuService.getMenuItemListByParentsIdx(menuSearchDTO));
			
			// 메뉴 등록을 위한 RequestMapping 리스트
			model.addAttribute("mappingList", menuService.MappingConvertor());
		} catch (Exception e) {
			log.error(e.getMessage());
			return "redirect:"+MENU_PATH;
		}
		
		return MENU_PATH+"/menu_child_list";
	}

	/**
	 * 어드민 > 메뉴 목록 > 하위 메뉴 등록 -> 화면 상단의 메뉴 타입 업데이트
	 * <br> 만약, 2Depth 메뉴와 함께 수정하라고 하면.. 조금 머리가 아파질것 같다
	 * @param menuDTO
	 * @param model
	 * @return
	 */
	@PutMapping("/modifyMenu")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modifyMenu(@RequestBody MenuDTO menuDTO,  Model model) {
		LogUtil.logStart(log);
		LogUtil.logDetail(log, "requestDTO : {}", StringUtil.objToString(menuDTO));
		
		//리턴 객체
		Map<String, Object> response = new HashMap<>();
		try {
			menuService.modifyMenu(menuDTO);
			response.put("status", "success");
			response.put("message", "정상적으로 수행 되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "fail");
			response.put("message", "정상적으로 수행되지 않았습니다.\n 잠시 후 다시 시도해주시기 바랍니다.");
		}
		
		// 비동기 통신 success에 들어가는 것은 HTTP 200||201 이 아닐까 하는 생각에 이렇게 리턴 객체 만듦
		return ResponseEntity.ok(response);
	}
	
	/**
	 * 어드민 > 메뉴 목록 > 하위 메뉴 등록 > 하위 메뉴 등록/수정
	 * <br> 전체 삭제 후 인서트 작업이기에 등록/수정 한곳에서 이루어짐
	 * @param menuList jsonObj >> {"menuType" : "foo", "menuId": "bar", "rank": n, "url":"bla/bla", "isDeleted":"Y/N", "description": "st"}
	 * @param model
	 * @return
	 */
	@PostMapping("/registerChildMenu")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> registerChildMenu(@RequestBody List<MenuDTO> menuList,  Model model) {
		LogUtil.logStart(log);
		LogUtil.logDetail(log, "requestDTO : {}", StringUtil.objToString(menuList));
		
		//리턴 객체
		Map<String, Object> response = new HashMap<>();
		try {
			menuService.registerChildMenu(menuList);
			response.put("status", "success");
			response.put("message", "정상적으로 수행 되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "fail");
			response.put("message", "정상적으로 수행되지 않았습니다.\n 잠시 후 다시 시도해주시기 바랍니다.");
		}

		// 비동기 통신 success에 들어가는 것은 HTTP 200||201 이 아닐까 하는 생각에 이렇게 리턴 객체 만듦
		return ResponseEntity.ok(response);
	}
	
	/**(어드민 > 메뉴 관리 > 메뉴 상세리스트(2depth) 에서 둘다 수정이 가능하게 화면구성하면서 사용하지 않음)
	 * 어드민 > 메뉴 관리 > 메뉴 사용 YN 업데이트
	 * <br>요청 시 클라이언트에서는 POST 방식이나, 히든메서드 필터에 의해 PUT 으로 변환됨
	 * <br>1depth 에서도, 2depth 에서도 가능하니 요청하는 url 을 받아 그곳으로 리턴해야겠다.
	 * @param idx
	 * @param menuDTO
	 * @param bindingResult
	 * @return
	 */
	@Deprecated
	@PutMapping("/{idx}")
	public String putItem(@PathVariable("idx") String idx, @ModelAttribute("menuDTO") MenuDTO menuDTO,
							@RequestParam String returnUrl) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		// 수정할 데이터들은 ItemDTO 객체에 저장되어 있지만 아이디 값이 없음
		// 따라서, id 파라미터로 전달 받은 값을 ItemDTO 객체에 저장 후 수정 요청
		log.info("{}", returnUrl);
		
		// idx 값이 숫자가 아닐 때 리스트로 리다이렉트
		if(!StringUtil.isLongValue(idx)) {
			return "redirect:"+MENU_PATH;
		}
		
		menuDTO.setIdx(idx);
		menuService.modifyMenuIsDeleted(menuDTO);
		
		// 상품 상세정보 조회 페이지 리다이렉트(GET 방식 /items/ 와 id값 결합 필요)
		return "redirect:"+MENU_PATH;
	}
	
}
