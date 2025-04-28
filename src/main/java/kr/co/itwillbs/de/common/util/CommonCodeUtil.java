package kr.co.itwillbs.de.common.util;

import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import kr.co.itwillbs.de.admin.dto.CodeDTO;
import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.admin.service.CodeService;
import kr.co.itwillbs.de.common.service.CustomUserDetails;
import kr.co.itwillbs.de.common.vo.LoginVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CommonCodeUtil {

	private final CodeService codeService;
	
	public CommonCodeUtil(CodeService codeService) {
		this.codeService = codeService;
	}
	
	/**
	 * 공통코드 majorCodeList 가져오기
	 * <br>(내부용) getCodeItems에서만 사용됨
	 * <br>25.04.13 캐시적용으로 사용하지 않음
	 * @return List<CodeDTO>
	 */
	@Deprecated
	public List<CodeDTO> getCodes() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return codeService.getCodes();
	}
	
	/**
	 * 공통코드 조회해서 전달하기
	 * @param majorCode 그룹코드
	 * @return List<CodeItemDTO>
	 */
	public List<CodeItemDTO> getCodeItems(String majorCode) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("input parameter is {}", majorCode);
		// majorCode valid
		if(!StringUtils.hasLength(majorCode)) {
			return null;
		}
		//List<CodeDTO> codeList = getCodes(); // 25.04.13 캐시 적용을 위한 내부 메서드 호출 X, 아래와 같은 이유로 자기 자신 내부를 호출하면 캐시가 무시됨
		List<CodeDTO> codeList = codeService.getCodes(); // 캐시 어노테이션은 프록시 기반으로 동작, 스프링이 관리하는 @Service빈을 직접 참조해야 캐시가 작동함
		// 요청한 majorCode가 존재 할 경우 아이템 리스트 가져오기
		boolean hasContains = false;
		for (CodeDTO item : codeList) {
			//if(item.getMajorCode().toLowerCase().equals(majorCode.toLowerCase())) {
			if(majorCode.equalsIgnoreCase(item.getMajorCode())) {
				hasContains = true;
				break;
			}
		}
		
		if(!hasContains) {
			log.error("요청 한 majorCode가 majorCodeList에 존재하지 않습니다.");
			return null;
		}
		return codeService.getCodeItems(majorCode);
	}
	
	public LoginVO getAuthUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			LoginVO loginVO = userDetails.getLoginVO();
			String memberId = userDetails.getUsername(); // 이렇게 자유롭게 사용 가능!
			LogUtil.logDetail(log,"userDetails is {}", userDetails);
			LogUtil.logDetail(log,"loginVO is {}", loginVO);
			
			return loginVO;
		}
		return null;
	}
	
	/**
	 * 공통 코드 캐시들을 미리 불러와서 셋해두자!
	 */
	@Order(1) // ApplicationReadyEvent.1st 순서를 제어하고 싶다면 Order 어노테이션으로 제어 가능
	@EventListener(ApplicationReadyEvent.class) //@EventListener(ApplicationReadyEvent.class) 이벤트 리스너들의 순서는 보장되지 않는다고 함
	private void initCacheToCommonCode() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 공통 코드 Major들 가져오기
		List<CodeDTO> codeList = codeService.getCodes();
		
		// 전부 호출하여 캐시에 올라가도록 하자
		for(CodeDTO item : codeList) {
			codeService.getCodeItems(item.getMajorCode());
		}
	}
}
