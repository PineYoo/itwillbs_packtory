package kr.co.itwillbs.de.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.admin.constant.IsDeleted;
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
		log.info("menuSearchDTO : {}", menuSearchDTO);
		return menuMapper.getMenuTypeList(menuSearchDTO);
	}

	
	/**
	 * 메뉴 관리 2depth에 마스터 정보 조회하기
	 * @param menuSearchDTO
	 * @return
	 */
	public MenuDTO getMenuIdMaster(MenuSearchDTO menuSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		List<MenuDTO> menuDTOlist = getMenuTypeList(menuSearchDTO);
		log.info("menuDTOlist size{}, {}", menuDTOlist.size(), menuDTOlist);
		
		// 가져온 menuDTOList에서 menuType을 searchDTO 에 넣는다
		MenuDTO menuDTO = menuDTOlist.get(0);
		
		return menuDTO;
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
		
		// 가져온 menuDTOList에서 menuType을 searchDTO 에 넣는다
		menuSearchDTO.setMenuType(menuDTOlist.get(0).getMenuType());
		menuSearchDTO.setIsDeleted(IsDeleted.N);
		log.info("menuSearchDTO parameters : {}", menuSearchDTO);
		
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

	/**
	 * 1Depth메뉴(대메뉴) 수정
	 * @param menuDTO
	 * @throws Exception 
	 */
	public void modifyMenu1Depth(MenuDTO menuDTO) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 세션에서 mod_id 가져와서 셋
		menuDTO.setModId("superUser");
		
		int affectedRow = menuMapper.modifyMenu1Depth(menuDTO);
		if(affectedRow < 1) {
			throw new Exception("데이터 수정에 실패 했습니다.");
		}
	}
	
	@Transactional
	public void registerMenu2Depth(List<MenuDTO> menuList) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		//등록 전에 삭제부터 하고
		menuMapper.removeMenu2Depth((MenuDTO)menuList.get(0));
		//등록 한다 등록하기전 등록자 세션에서 아이디 가져온다.
		for (MenuDTO menuDTO : menuList) {
			menuDTO.setRegId("superUser");
		}
		int affectedRow = menuMapper.registerMenu2Depth(menuList);
		if(affectedRow < 1 && menuList.size() != affectedRow) {
			throw new Exception("데이터 등록에 실패 했습니다.");
		}
	}


}
