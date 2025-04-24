package kr.co.itwillbs.de.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import kr.co.itwillbs.de.admin.dto.MenuDTO;
import kr.co.itwillbs.de.admin.dto.MenuSearchDTO;

@Mapper
public interface MenuMapper {

	/**
	 * 메뉴 등록(INSERT) pa
	 * @param menuDTO
	 * @return
	 */
	int registerMenu(MenuDTO menuDTO);

	/**
	 * 그룹 메뉴 리스트 조회 카운트 - 페이징용 parents_idx is null
	 * @param menuSearchDTO
	 * @return
	 */
	int getMenuCount(MenuSearchDTO menuSearchDTO);
	
	/**
	 * 그룹 메뉴 리스트 검색 조건 조회 parents_idx is null
	 * @param menuSearchDTO
	 * @return
	 */
	List<MenuDTO> getMenuList(MenuSearchDTO menuSearchDTO);

	/**
	 * 부모(parents_idx is null)의 하위 메뉴 조회(parents_idx = 부모메뉴row.idx)
	 * @param menuSearchDTO
	 * @return
	 */
	List<MenuDTO> getMenuItemListByParentsIdx(MenuSearchDTO menuSearchDTO);

	/**
	 * 메뉴 목록에서 isDeleted 수정하기 
	 * @param menuDTO
	 * @return
	 */
	int modifyMenuIsDeleted(MenuDTO menuDTO);

	
	/**
	 * 메뉴 대표메뉴 수정 
	 * @param menuDTO
	 * @return
	 */
	int modifyMenu(MenuDTO menuDTO);
	
	/**
	 * 메뉴 child(부메뉴) 등록 전 삭제
	 * @param menuDTO
	 * @return
	 */
	int removeChildMenu(MenuDTO menuDTO);
	
	/**
	 * 메뉴 child(부메뉴) 등록
	 * @param menuList
	 * @return
	 */
	int registerChildMenu(List<MenuDTO> menuList);

	/**
	 * 전체 메뉴 가져오기
	 * @return
	 */
	List<MenuDTO> getAllmenus();

	@Select("""
			SELECT
				idx,
				menu_name,
				parents_idx,
				url
			FROM t_menu
			WHERE url = #{uri}
			""")
	MenuDTO findByUri(@Param("uri") String uri);


}
