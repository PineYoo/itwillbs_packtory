package kr.co.itwillbs.de.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.itwillbs.de.admin.dto.MenuDTO;
import kr.co.itwillbs.de.admin.dto.MenuSearchDTO;
import kr.co.itwillbs.de.admin.service.MenuService;
import kr.co.itwillbs.de.common.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/menu")
public class MenuController {

	// 계속 사용하게 될 클래스 RequestMapping 문자열 값
	private final String MENU_PATH="/admin/menu";
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private CommonUtils comUtils;
	
	/**
	 * 어드민 > 메뉴 관리 > 메뉴 등록
	 * @param model
	 * @return
	 */
	@GetMapping(value="/new")
	public String menuRegisterForm(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		model.addAttribute("menuDTO", new MenuDTO());
		
		return MENU_PATH+"/menu_register_form";
	}
	
	/**
	 * 어드민 > 메뉴 관리 > 메뉴 등록(INSERT)
	 * @param logDTO
	 * @return
	 */
	@PostMapping(value={"","/"})
	public String menuRegister(@ModelAttribute("menuDTO") MenuDTO menuDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("requestData : {}", comUtils.ObjToString(menuDTO));
		
		if(menuService.registerMenu(menuDTO) < 1) {
			return MENU_PATH+"/log_register_form";
		}
		return "redirect:"+MENU_PATH;
	}
	
	/**
	 * 어드민 > 메뉴 관리 > 메뉴 리스트 (1depth)
	 * @param model
	 * @return
	 */
	@GetMapping(value= {"","/"})
	public String getMenuTypeList(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		List<MenuDTO> menuDTOList = menuService.getMenuTypeList(new MenuSearchDTO());
		
		model.addAttribute("menuDTOList", menuDTOList);
		
		return MENU_PATH+"/menu_type_list";
	}
	
	/**
	 * 어드민 > 메뉴 관리 > 메뉴 상세리스트(2depth)
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/{id}")
	public String getMenuIdList(@PathVariable("id") String id,
								@ModelAttribute MenuSearchDTO menuSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("request param : {}, requestDTO", id, menuSearchDTO);
		
		// idx 값이 숫자가 아닐 때 리스트로 리다이렉트
		if(!comUtils.isLongValue(id)) {
			return "redirect:"+MENU_PATH;
		}
		menuSearchDTO.setMenuId(id);
		List<MenuDTO> menuDTOIdList = menuService.getMenuIdList(menuSearchDTO);
		model.addAttribute("menuDTOIdList", menuDTOIdList);
		
		return MENU_PATH+"/menu_id_list";
	}
	
	/**
	 * 어드민 > 메뉴 관리 > 메뉴 사용 YN 업데이트
	 * <br>요청 시 클라이언트에서는 POST 방식이나, 히든메서드 필터에 의해 PUT 으로 변환됨
	 * <br>1depth 에서도, 2depth 에서도 가능하니 요청하는 url 을 받아 그곳으로 리턴해야겠다.
	 * @param idx
	 * @param menuDTO
	 * @param bindingResult
	 * @return
	 */
	@PutMapping("/{idx}")
	public String putItem(@PathVariable("idx") String idx, @ModelAttribute("menuDTO") MenuDTO menuDTO,
							@RequestParam String returnUrl) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		// 수정할 데이터들은 ItemDTO 객체에 저장되어 있지만 아이디 값이 없음
		// 따라서, id 파라미터로 전달 받은 값을 ItemDTO 객체에 저장 후 수정 요청
		log.info("{}", returnUrl);
		
		// idx 값이 숫자가 아닐 때 리스트로 리다이렉트
		if(!comUtils.isLongValue(idx)) {
			return "redirect:"+MENU_PATH;
		}
		
		menuDTO.setIdx(idx);
		menuService.modifyMenuIsDeleted(menuDTO);
		
		// 상품 상세정보 조회 페이지 리다이렉트(GET 방식 /items/ 와 id값 결합 필요)
		return "redirect:"+MENU_PATH;
	}
}
