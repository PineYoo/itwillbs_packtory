package kr.co.itwillbs.de.common.util;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import kr.co.itwillbs.de.admin.dto.CodeDTO;
import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.common.service.CommonService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CommonCodeUtil {

	private final CommonService commonService;
	
	public CommonCodeUtil(CommonService commonService) {
		this.commonService = commonService;
	}
	
	/**
	 * 공통코드 majorCodeList 가져오기
	 * <br>(내부용) getCodeItems에서만 사용됨
	 * @return
	 */
	public List<CodeDTO> getCodes() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return commonService.getCodes();
	}
	
	/**
	 * 공통코드 조회해서 전달하기
	 * @param majorCode
	 * @return
	 */
	public List<CodeItemDTO> getCodeItems(String majorCode) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("input parameter is {}", majorCode);
		// majorCode valid
		if(!StringUtils.hasLength(majorCode)) {
			return null;
		}
		List<CodeDTO> codeList = getCodes();
		// 요청한 majorCode가 존재 할 경우 아이템 리스트 가져오기
		boolean hasContains = false;
		for (CodeDTO item : codeList) {
			if(item.getMajorCode().equals(majorCode)) {
				hasContains = true;
				break;
			}
		}
		
		if(!hasContains) {
			log.info("요청 한 majorCode가 majorCodeList에 존재하지 않습니다.");
			return null;
		}
		return commonService.getCodeItems(majorCode);
	}
}
