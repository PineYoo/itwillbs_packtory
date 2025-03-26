package kr.co.itwillbs.de.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.admin.dto.MenuDTO;
import kr.co.itwillbs.de.admin.dto.MenuSearchDTO;

@Mapper
public interface MenuMapper {

	/**
	 * 메뉴 등록(INSERT), 2depth 메뉴 등록 고민하기!
	 * @param menuDTO
	 * @return
	 */
	int registerMenu(MenuDTO menuDTO);

	/**
	 * 메뉴 리스트 조회(1depth) -> 조건은 생각해보자
	 * @param menuSearchDTO
	 * @return
	 */
	List<MenuDTO> getMenuTypeList(MenuSearchDTO menuSearchDTO);

	/**
	 * 메뉴 리스트 조회(2depth) -> 조건은 생각해보자
	 * @param menuSearchDTO
	 * @return
	 */
	List<MenuDTO> getMenuIdList(MenuSearchDTO menuSearchDTO);

	/**
	 * 메뉴 목록에서 isDeleted 수정하기 
	 * @param menuDTO
	 * @return
	 */
	int modifyMenuIsDeleted(MenuDTO menuDTO);

	
	/**
	 * 메뉴 1depth(대메뉴) 수정
	 * @param menuDTO
	 * @return
	 */
	int modifyMenu1Depth(MenuDTO menuDTO);
	
	/**
	 * 메뉴 2depth(소메뉴) 등록 전 삭제
	 * @param menuDTO
	 * @return
	 */
	int removeMenu2Depth(MenuDTO menuDTO);
	
	/**
	 * 메뉴 2depth(소메뉴) 등록
	 * @param menuList
	 * @return
	 */
	int registerMenu2Depth(List<MenuDTO> menuList);


}
