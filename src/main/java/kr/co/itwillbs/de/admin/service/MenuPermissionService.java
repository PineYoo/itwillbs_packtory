package kr.co.itwillbs.de.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.admin.dto.MenuPermissionDTO;
import kr.co.itwillbs.de.admin.dto.MenuPermissionSearchDTO;
import kr.co.itwillbs.de.admin.mapper.MenuPermissionMapper;
import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.common.util.LogUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MenuPermissionService {

	private final MenuPermissionMapper menuPermissionMapper;
	
	public MenuPermissionService(MenuPermissionMapper menuPermissionMapper) {
		this.menuPermissionMapper = menuPermissionMapper;
	}
	
	/**
	 * 등록(INSERT)
	 * @param MenuPermissionDTO
	 * @return
	 */
	@LogExecution
	@Transactional
	public int registerMenuPermission(MenuPermissionDTO dto) {
		LogUtil.logStart(log);
		
		return menuPermissionMapper.registerMenuPermission(dto);
	}

	/**
	 * 리스트 검색 조건 조회 카운트 - 페이징용
	 * @param menuSearchDTO
	 * @return
	 */
	@Transactional(readOnly = true)
	public int getMenuPermissionCountBySearchDTO(MenuPermissionSearchDTO searchDTO) {
		LogUtil.logStart(log);
		
		return menuPermissionMapper.getMenuPermissionCountBySearchDTO(searchDTO);
	}
	
	/**
	 * 리스트 검색 조건 조회
	 * @param menuSearchDTO
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<MenuPermissionDTO> getMenuPermissionsBySearchDTO(MenuPermissionSearchDTO searchDTO) {
		LogUtil.logStart(log);
		
		return menuPermissionMapper.getMenuPermissionsBySearchDTO(searchDTO);
	}

	
	/**
	 * 메뉴 관리 2depth에서 1depth 정보 조회하기
	 * @param menuSearchDTO
	 * @return
	 * @throws Exception 
	 */
	@Transactional(readOnly = true)
	public MenuPermissionDTO getMenuPermissionsByIdx(String idx) throws Exception {
		LogUtil.logStart(log);
		
		return menuPermissionMapper.getMenuPermissionsByIdx(idx);
	}
	
	/**
	 * 부모(parents_idx is null)의 하위 조회(parents_idx = 부모메뉴row.idx)
	 * @param MenuPermissionSearchDTO
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<MenuPermissionDTO> getMenuPermissionsByParentsIdx(MenuPermissionSearchDTO searchDTO) {
		LogUtil.logStart(log);
		
		// 컨트롤러에서 넘겨준 idx와 menuType 파라미터 확인
		LogUtil.logDetail(log, "searchDTO is {}", searchDTO);
		// 가져온 menuDTOList에서 menuType을 searchDTO 에 넣는다
		/*
		 * menuSearchDTO.setMenuType(menuDTOlist.get(0).getMenuType());
		 * menuSearchDTO.setIsDeleted(IsDeleted.N);
		 */
		
		return menuPermissionMapper.getMenuPermissionsByParentsIdx(searchDTO);
	}

	/**
	 * 목록에서 isDeleted 수정하기 
	 * @param MenuPermissionDTO
	 * @return
	 */
	@LogExecution
	@Transactional
	public int modifyMenuPermissionIsDeleted(MenuPermissionDTO dto) {
		LogUtil.logStart(log);
		
		return menuPermissionMapper.modifyMenuPermissionIsDeleted(dto);
	}

	/**
	 * parents 수정 
	 * @param MenuPermissionDTO
	 * @throws Exception 
	 */
	@LogExecution
	@Transactional
	public void modifyMenuPermission(MenuPermissionDTO dto) throws Exception {
		LogUtil.logStart(log);
		
		int affectedRow = menuPermissionMapper.modifyMenuPermission(dto);
		if(affectedRow < 1) {
			throw new RuntimeException("데이터 수정에 실패 했습니다.");
		}
	}
	
	/**
	 * child 삭제, 등록
	 * @param List<MenuPermissionDTO> list
	 * @throws Exception
	 */
	@LogExecution
	@Transactional
	public void registerChildMenuPermission(List<MenuPermissionDTO> list) throws Exception {
		LogUtil.logStart(log);
		
		//등록 전에 삭제부터 하고
		menuPermissionMapper.removeMinorMenuPermission((MenuPermissionDTO)list.get(0));
		
		int affectedRow = menuPermissionMapper.registerMinorMenuPermission(list);
		log.info("itemList.size is {}, // affectedRow is {}", list.size(), affectedRow);
		
		if(affectedRow < 1 || list.size() != affectedRow) {
			throw new RuntimeException("데이터 등록에 실패 했습니다.");
		}
	}
}
