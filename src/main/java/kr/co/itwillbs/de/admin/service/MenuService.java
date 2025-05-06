package kr.co.itwillbs.de.admin.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
import kr.co.itwillbs.de.common.util.LogUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MenuService {

	private final MenuMapper menuMapper;
	private final RequestMappingHandlerMapping requestMappingHandlerMapping;
	
	public MenuService(MenuMapper menuMapper, @Qualifier("requestMappingHandlerMapping")RequestMappingHandlerMapping requestMappingHandlerMapping) {
		this.menuMapper = menuMapper;
		this.requestMappingHandlerMapping = requestMappingHandlerMapping;
	}
	
	/**
	 * 메뉴 등록(INSERT), 2depth 메뉴 등록 고민하기!
	 * @param menuDTO
	 * @return
	 */
	@CacheEvict(value = {"menus", "menuByUri"}, allEntries = true)
	@LogExecution
	@Transactional(readOnly = true)
	public int registerMenu(MenuDTO menuDTO) {
		LogUtil.logStart(log);
		
		return menuMapper.registerMenu(menuDTO);
	}

	/**
	 * 그룹 메뉴 리스트 검색 조건 조회 카운트 - 페이징용
	 * @param menuSearchDTO
	 * @return
	 */
	@Transactional(readOnly = true)
	public int getMenuCount(MenuSearchDTO menuSearchDTO) {
		LogUtil.logStart(log);
		
		return menuMapper.getMenuCount(menuSearchDTO);
	}
	
	/**
	 * 그룹 메뉴 리스트 검색 조건 조회
	 * @param menuSearchDTO
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<MenuDTO> getMenuList(MenuSearchDTO menuSearchDTO) {
		LogUtil.logStart(log);
		
		return menuMapper.getMenuList(menuSearchDTO);
	}

	
	/**
	 * 메뉴 관리 2depth에서 1depth 정보 조회하기
	 * @param menuSearchDTO
	 * @return
	 * @throws Exception 
	 */
	@Transactional(readOnly = true)
	public MenuDTO getMenuTypeByIdx(MenuSearchDTO menuSearchDTO) throws Exception {
		LogUtil.logStart(log);
		List<MenuDTO> menuDTOlist = getMenuList(menuSearchDTO);
		LogUtil.logDetail(log, "menuDTOlist size: {}, data: {}", menuDTOlist.size(), menuDTOlist);
		
		MenuDTO menuDTO = null;
		
		// 1depth 객체는 하나여야 한다. 그냥 따로 만들자니.. 좀 그래서..
		if(menuDTOlist.size() == 1) {
			// 가져온 menuDTOList에서 menuType을 searchDTO 에 넣는다
			menuDTO = menuDTOlist.get(0);
		} else {
			throw new RuntimeException("메뉴 정보를 찾을 수 없습니다.");
		}
		
		return menuDTO;
	}
	
	/**
	 * 메뉴 리스트 조회(2depth) -> 조건은 생각해보자
	 * @param menuSearchDTO
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<MenuDTO> getMenuItemListByParentsIdx(MenuSearchDTO menuSearchDTO) {
		LogUtil.logStart(log);
		
		return menuMapper.getMenuItemListByParentsIdx(menuSearchDTO);
	}

	/**
	 * 메뉴 목록에서 isDeleted 수정하기
	 * @param menuDTO
	 * @return
	 */
	@Caching(evict = {
		@CacheEvict(value = "menus", allEntries = true),
		@CacheEvict(value = "menuByUri", allEntries = true)
		})
	@LogExecution
	@Transactional
	public int modifyMenuIsDeleted(MenuDTO menuDTO) {
		LogUtil.logStart(log);
		
		return menuMapper.modifyMenuIsDeleted(menuDTO);
	}

	/**
	 * 1Depth메뉴(대메뉴) 수정
	 * @param menuDTO
	 * @throws Exception 
	 */
	@CacheEvict(value = {"menus", "menuByUri"}, allEntries = true)
	@LogExecution
	@Transactional
	public void modifyMenu(MenuDTO menuDTO) throws Exception {
		LogUtil.logStart(log);
		
		int affectedRow = menuMapper.modifyMenu(menuDTO);
		if(affectedRow < 1) {
			throw new RuntimeException("데이터 수정에 실패 했습니다.");
		}
	}
	
	/**
	 * 
	 * @param menuList
	 * @throws Exception
	 */
	@CacheEvict(value = {"menus", "menuByUri"}, allEntries = true)
	@LogExecution
	@Transactional
	public void registerChildMenu(List<MenuDTO> menuList) throws Exception {
		LogUtil.logStart(log);
		
		//등록 전에 삭제부터 하고
		menuMapper.removeChildMenu((MenuDTO)menuList.get(0));
		
		int affectedRow = menuMapper.registerChildMenu(menuList);
		log.info("itemList.size is {}, // affectedRow is {}", menuList.size(), affectedRow);
		
		if(affectedRow < 1 || menuList.size() != affectedRow) {
			throw new RuntimeException("데이터 등록에 실패 했습니다.");
		}
	}

	@Cacheable(value = "menus")
	@Transactional(readOnly = true)
	public List<MenuDTO> getAllMenus() {
		LogUtil.logStart(log);
		
		return menuMapper.getAllmenus();
	}
	
	@Cacheable(value = "menuByUri", key = "#p0")
	@Transactional(readOnly = true)
	public MenuDTO findByUri(String uri) {
		LogUtil.logStart(log);
		
		return menuMapper.findByUri(uri);
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
		LogUtil.logStart(log);
		
		List<RequestMappingDTO> mappingList = new ArrayList<>();
		
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
		for(Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
			HandlerMethod handlerMethod = entry.getValue();
			
			/* 25.04.19 굳이 밑에까지 내려가야 할까?
			 * 조건: error, sample, items ... 등등 컨트롤러는 바로 컨티뉴
			 * handlerMethod.getBean(): (Object)mainController <- 좋은가? 1점
			 * handlerMethod.getBeanType().getPackageName(): kr.co.itwillbs.de.common.controller 애매한데?
			 * handlerMethod.getBeanType().getName(): kr.co.itwillbs.de.common.MainController endsWith()? 너인가?
			 * handlerMethod.getBeanType().getSimpleName(): MainController equals 너님 채용
			 */
			if(handlerMethod.getBeanType().getPackageName().startsWith("kr.co.itwillbs.de.common") // error,file,main,search
				|| handlerMethod.getBeanType().getPackageName().startsWith("kr.co.itwillbs.de.sample") // bs, items, sample
				|| handlerMethod.getBeanType().getSimpleName().equals("AdminMainController")
				) {
				continue;
			}
				
			RequestMappingInfo requestMappingInfo = entry.getKey();
			
			String description = requestMappingInfo.getName(); // @RequestMapping(name="bla") 를 가져온다
			Set<String> patterns = requestMappingInfo.getPatternValues();
			Set<RequestMethod> methods = requestMappingInfo.getMethodsCondition().getMethods();
			RequestMappingDTO dto = null;

			for (String pattern : patterns) {
				// 25.04.18 - 등록에 도움되지 않는 혹은 사용자에게 노출하지 않아도 되는 mapping 정보는 제외 시키자
//				if(pattern.startsWith("/error") || pattern.startsWith("/sample") || pattern.startsWith("/items")) continue; // error||sample||items 페이지는 메뉴 등록을 필요치 않음
				
				for (RequestMethod method : methods) {
					
					if(!RequestMethod.GET.equals(method)) continue; // 메뉴 연결 uri 의 method는 GET 이임으로 이외 method는 by pass 핸들러 메서드를 가져올 떄 GetMapping만 가져오는건 어떨까?
					
					dto = new RequestMappingDTO();
					dto.setUriPattern(pattern);
					dto.setMethod(method.name());
					dto.setMethodName(handlerMethod.getMethod().getName());
					dto.setSimpleName(handlerMethod.getBeanType().getSimpleName());
					dto.setDescription(description);
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
