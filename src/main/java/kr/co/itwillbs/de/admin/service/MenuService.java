package kr.co.itwillbs.de.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.admin.dto.MenuDTO;
import kr.co.itwillbs.de.admin.dto.MenuSearchDTO;
import kr.co.itwillbs.de.admin.mapper.MenuMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MenuService {

	@Autowired
	private MenuMapper menuMapper;
	
	/**
	 * 메뉴 등록(INSERT), 2depth 메뉴 등록 고민하기!
	 * @param menuDTO
	 * @return
	 */
	public int registerMenu(MenuDTO menuDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 세션에서 reg_id 가져와서 셋
		menuDTO.setRegId("superUser");
		
		return menuMapper.registerMenu(menuDTO);
	}

	/**
	 * 메뉴 리스트 조회(1depth) -> 조건은 생각해보자
	 * @param menuSearchDTO
	 * @return
	 */
	public List<MenuDTO> getMenuTypeList(MenuSearchDTO menuSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 1depth 메뉴들은 menuId가 0이다.
		menuSearchDTO.setMenuId("0");
		
		return menuMapper.getMenuTypeList(menuSearchDTO);
	}

	/**
	 * 메뉴 리스트 조회(2depth) -> 조건은 생각해보자
	 * @param menuSearchDTO
	 * @return
	 */
	public List<MenuDTO> getMenuIdList(MenuSearchDTO menuSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		// 바로 검색하려니까 menuType값을 알수가 없다.
		
		List<MenuDTO> menuDTOlist = getMenuTypeList(menuSearchDTO);
		log.info("menuDTOlist size{}, {}", menuDTOlist.size(), menuDTOlist);
		
		
		return menuMapper.getMenuIdList(menuSearchDTO);
	}

	/**
	 * 메뉴 목록에서 isDeleted 수정하기
	 * @param menuDTO
	 * @return
	 */
	public int modifyMenuIsDeleted(MenuDTO menuDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 세션에서 mod_id 가져와서 셋
		menuDTO.setModId("superUser");
		
		return menuMapper.modifyMenuIsDeleted(menuDTO);
	}

}
