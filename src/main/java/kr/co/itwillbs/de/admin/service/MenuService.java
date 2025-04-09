package kr.co.itwillbs.de.admin.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import kr.co.itwillbs.de.admin.dto.MenuDTO;
import kr.co.itwillbs.de.admin.dto.MenuSearchDTO;
import kr.co.itwillbs.de.admin.dto.RequestMappingDTO;
import kr.co.itwillbs.de.admin.mapper.MenuMapper;
import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MenuService {

	private final MenuMapper menuMapper;
	private final RequestMappingHandlerMapping requestMappingHandlerMapping;
	
	//@Autowired
	public MenuService(MenuMapper menuMapper, RequestMappingHandlerMapping requestMappingHandlerMapping) {
		this.menuMapper = menuMapper;
		this.requestMappingHandlerMapping = requestMappingHandlerMapping;
	}
	
	/**
	 * 메뉴 등록(INSERT), 2depth 메뉴 등록 고민하기!
	 * @param menuDTO
	 * @return
	 */
	@LogExecution
	public int registerMenu(MenuDTO menuDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return menuMapper.registerMenu(menuDTO);
	}

	/**
	 * 그룹 메뉴 리스트 검색 조건 조회 카운트 - 페이징용
	 * @param menuSearchDTO
	 * @return
	 */
	public int getMenuTypeCount(MenuSearchDTO menuSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 1depth 메뉴들은 menuId가 0이다.
		menuSearchDTO.setMenuId("0");
		log.info("menuSearchDTO : {}", menuSearchDTO);
		return menuMapper.getMenuTypeCount(menuSearchDTO);
	}
	
	/**
	 * 그룹 메뉴 리스트 검색 조건 조회
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
	 * 메뉴 관리 2depth에서 1depth 정보 조회하기
	 * @param menuSearchDTO
	 * @return
	 * @throws Exception 
	 */
	public MenuDTO getMenuTypeByIdx(MenuSearchDTO menuSearchDTO) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		List<MenuDTO> menuDTOlist = getMenuTypeList(menuSearchDTO);
		log.info("menuDTOlist size{}, {}", menuDTOlist.size(), menuDTOlist);
		
		MenuDTO menuDTO = null;
		
		// 1depth 객체는 하나여야 한다. 그냥 따로 만들자니.. 좀 그래서..
		if(menuDTOlist.size() == 1) {
			// 가져온 menuDTOList에서 menuType을 searchDTO 에 넣는다
			menuDTO = menuDTOlist.get(0);
		} else {
			throw new Exception("메뉴 정보를 찾을 수 없습니다.");
		}
		
		return menuDTO;
	}
	
	/**
	 * 메뉴 리스트 조회(2depth) -> 조건은 생각해보자
	 * @param menuSearchDTO
	 * @return
	 */
	public List<MenuDTO> getMenuIdListByMenuType(MenuSearchDTO menuSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 컨트롤러에서 넘겨준 idx와 menuType 파라미터 확인
		log.info("menuSearchDTO is {}", menuSearchDTO);
		
		// 가져온 menuDTOList에서 menuType을 searchDTO 에 넣는다
		/*
		 * menuSearchDTO.setMenuType(menuDTOlist.get(0).getMenuType());
		 * menuSearchDTO.setIsDeleted(IsDeleted.N);
		 */
		log.info("menuSearchDTO parameters : {}", menuSearchDTO);
		
		return menuMapper.getMenuIdListByMenuType(menuSearchDTO);
	}

	/**
	 * 메뉴 목록에서 isDeleted 수정하기
	 * @param menuDTO
	 * @return
	 */
	@LogExecution
	public int modifyMenuIsDeleted(MenuDTO menuDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return menuMapper.modifyMenuIsDeleted(menuDTO);
	}

	/**
	 * 1Depth메뉴(대메뉴) 수정
	 * @param menuDTO
	 * @throws Exception 
	 */
	@LogExecution
	public void modifyTypeMenu(MenuDTO menuDTO) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		int affectedRow = menuMapper.modifyTypeMenu(menuDTO);
		if(affectedRow < 1) {
			throw new Exception("데이터 수정에 실패 했습니다.");
		}
	}
	
	/**
	 * 
	 * @param menuList
	 * @throws Exception
	 */
	@LogExecution
	@Transactional
	public void registerChildMenu(List<MenuDTO> menuList) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		//등록 전에 삭제부터 하고
		menuMapper.removeChildMenu((MenuDTO)menuList.get(0));
		
		int affectedRow = menuMapper.registerChildMenu(menuList);
		log.info("itemList.size is {}, // affectedRow is {}", menuList.size(), affectedRow);
		
		if(affectedRow < 1 || menuList.size() != affectedRow) {
			throw new Exception("데이터 등록에 실패 했습니다.");
		}
	}
	
	/**
	 * <pre>
	 * @Controller에 @RequestMapping 관련 어노테이션으로 구현된 컨트롤러 요소들을
	 * RequestMappingHandlerMapping 으로 찾으려 만듦.
	 * 오랜만에 보니까 하나도 모르겠네.. GPT 만세!!
	 * </pre>
	 * @return List<RequestMappingDTO>
	 */
	public List<RequestMappingDTO> MappingConvertor() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		List<RequestMappingDTO> mappingList = new ArrayList<>();
		
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
		for(Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
			RequestMappingInfo requestMappingInfo = entry.getKey();
			HandlerMethod handlerMethod = entry.getValue();
			
			Set<String> patterns = requestMappingInfo.getPatternValues();
			Set<RequestMethod> methods = requestMappingInfo.getMethodsCondition().getMethods();
			
			RequestMappingDTO dto = null;

			for (String pattern : patterns) {
				for (RequestMethod method : methods) {
					dto = new RequestMappingDTO();
					dto.setMethod(method.name());
					dto.setMethodName(handlerMethod.getMethod().getName());
					dto.setUrlPattern(pattern);
					dto.setSimpleName(handlerMethod.getBeanType().getSimpleName());
					mappingList.add(dto);
				}
			}
		}
		
		// 와 진짜 이거 뭐지? 이렇게 한다고 정렬이 된다고? 정신을 못차리겠네 이게 무슨말이야
		mappingList.sort(Comparator
				.comparing(RequestMappingDTO::getSimpleName)
				.thenComparing(RequestMappingDTO::getMethodName));
		return mappingList;
	}
}
