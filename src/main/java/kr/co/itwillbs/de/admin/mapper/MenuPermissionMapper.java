package kr.co.itwillbs.de.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import kr.co.itwillbs.de.admin.dto.MenuPermissionDTO;
import kr.co.itwillbs.de.admin.dto.MenuPermissionSearchDTO;
import kr.co.itwillbs.de.common.vo.MenuPermissionVO;

@Mapper
public interface MenuPermissionMapper {

	/**
	 * 등록(INSERT)
	 * @param MenuPermissionDTO
	 * @return
	 */
	int registerMenuPermission(MenuPermissionDTO dto);

	/**
	 * 리스트 조회 카운트 - 페이징용
	 * @param MenuPermissionSearchDTO
	 * @return
	 */
	int getMenuPermissionCountBySearchDTO(MenuPermissionSearchDTO searchDTO);
	
	/**
	 * 리스트 검색 조건 조회
	 * @param MenuPermissionSearchDTO
	 * @return
	 */
	List<MenuPermissionDTO> getMenuPermissionsBySearchDTO(MenuPermissionSearchDTO searchDTO);

	
	/**
	 * 상세 조회
	 * @param idx
	 * @return
	 */
	MenuPermissionDTO getMenuPermissionsByIdx(@Param("idx") String idx);
	
	/**
	 * 부모(parents_idx is null)의 하위 조회(parents_idx = 부모메뉴row.idx)
	 * @param MenuPermissionSearchDTO
	 * @return
	 */
	List<MenuPermissionDTO> getMenuPermissionsByParentsIdx(MenuPermissionSearchDTO searchDTO);

	/**
	 * 목록에서 isDeleted 수정하기 
	 * @param MenuPermissionDTO
	 * @return
	 */
	int modifyMenuPermissionIsDeleted(MenuPermissionDTO dto);

	
	/**
	 * parents 수정 
	 * @param MenuPermissionDTO
	 * @return
	 */
	int modifyMenuPermission(MenuPermissionDTO dto);
	
	/**
	 * child 등록 전 삭제
	 * @param MenuPermissionDTO
	 * @return
	 */
	int removeMinorMenuPermission(MenuPermissionDTO dto);
	
	/**
	 * child 등록
	 * @param List<MenuPermissionDTO>
	 * @return
	 */
	int registerMinorMenuPermission(List<MenuPermissionDTO> list);

	// 한번 써보고 싶었다. 소원 성취
	@Select("""
			SELECT
				menu_idx,
				owner_member_id,
				group_id,
				permission_code
			FROM t_menu_permission
			WHERE menu_idx = #{menuIdx}
			""")
	List<MenuPermissionVO> findAllByMenuIdx(@Param("menuIdx") Long menuIdx);
}
