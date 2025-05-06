package kr.co.itwillbs.de.admin.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.admin.dto.CodeDTO;
import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.admin.dto.CodeSearchDTO;
import kr.co.itwillbs.de.admin.mapper.CodeMapper;
import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.common.util.LogUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CodeService {

	private final CodeMapper codeMapper;
	public CodeService(CodeMapper codeMapper) {
		this.codeMapper = codeMapper;
	}
	
	/**
	 * INSERT t_commoncode 
	 * @param codeDTO
	 * @return
	 */
	@LogExecution
	@CacheEvict(value = "majorCodes", allEntries = true) // 캐시 삭제! true: 메서드 호출 전 실행, false: 메서드 호출 후 실행
	@Transactional
	public int registerCode(CodeDTO codeDTO) {
		LogUtil.logStart(log);
		
		int affectedRow = codeMapper.registerCode(codeDTO);
		LogUtil.logDetail(log, "affectedRow is {}", affectedRow);
		return affectedRow;
	}

	/**
	 * SELECT t_commoncode 
	 * @return List<CodeDTO>
	 */
	@Cacheable(value = "majorCodes") // 캐시 저장 기능 value의 이름이 존재/미존재 시 모두 헤서드 호출 후 실행? 맞아?
	@Transactional(readOnly = true)
	public List<CodeDTO> getCodes() {
		LogUtil.logStart(log);
		
		return codeMapper.getCodes();
	}
	
	/**
	 * SELECT t_commoncode_item where is_deleted = 'N' and mojor_code = #{majorCode}
	 * @param majorCode
	 * @return
	 */
	@Cacheable(value = "codeItems", key = "#p0") // #majorCode로 매핑이 될 것 같았는데 계속 Null 이라고 나온다. 기절하겠넹
	@Transactional(readOnly = true)
	public List<CodeItemDTO> getCodeItems(String majorCode) {
		LogUtil.logStart(log);
		
		return codeMapper.getCodeItems(majorCode);
	}
	
	/**
	 * SELECT count(*) t_commoncode 
	 * @param codeSearchDTO
	 * @return List<CodeDTO>
	 */
	@Transactional(readOnly = true)
	public int getCodesCountBySearchDTO(CodeSearchDTO codeSearchDTO) {
		LogUtil.logStart(log);
		
		return codeMapper.getCodesCountBySearchDTO(codeSearchDTO);
	}
		
	/**
	 * SELECT t_commoncode 
	 * @param codeSearchDTO
	 * @return List<CodeDTO>
	 */
	@Transactional(readOnly = true)
	public List<CodeDTO> getCodesBySearchDTO(CodeSearchDTO codeSearchDTO) {
		LogUtil.logStart(log);
		
		return codeMapper.getCodesBySearchDTO(codeSearchDTO);
	}

	/**
	 * SELECT t_commoncode where idx = #{codeSearchDTO.idx}
	 * @param codeSearchDTO
	 * @return CodeDTO
	 */
	@Transactional(readOnly = true)
	public CodeDTO getCodeByIdx(CodeSearchDTO codeSearchDTO) throws Exception {
		LogUtil.logStart(log);
		
		return codeMapper.getCodeByIdx(codeSearchDTO);
	}

	/**
	 * SELECT t_commoncode_item where major_code = #{codeSearchDTO.majorCode}
	 * @param codeSearchDTO
	 * @return List<CodeItemDTO>
	 */
	@Transactional(readOnly = true)
	public List<CodeItemDTO> getCodeItemsByMajorCode(CodeSearchDTO codeSearchDTO) throws Exception {
		LogUtil.logStart(log);
		
		return codeMapper.getCodeItemsByMajorCode(codeSearchDTO);
	}

	/**
	 * UPDATE t_commoncode set codeDTO where idx = #{codeDTO.idx}
	 * @param codeDTO
	 * @throws Exception 
	 */
	@LogExecution
	@CacheEvict(value = "codeItems", key = "#codeDTO.majorCode")
	@Transactional
	public void modifyCode(CodeDTO codeDTO) throws Exception {
		LogUtil.logStart(log);
		
		int affectedRow = codeMapper.modifyCode(codeDTO);
		if(affectedRow < 1) {
			throw new RuntimeException("그룹코드 수정을 완료하지 못했습니다.\\n잠시 후 다시 시도해주시기 바랍니다.");
		}
	}

	/**
	 * DELETE t_commoncode_item where major_code = #{itemList.get(0).majorCode}
	 * @param itemList
	 * @throws Exception 
	 */
	@LogExecution // 로그 남길 서비스
	@CacheEvict(value = "codeItems", key = "#itemList.get(0).majorCode")
	@Transactional
	public void registerCodeItems(List<CodeItemDTO> itemList) throws Exception {
		LogUtil.logStart(log);
		
		if(!itemList.isEmpty()) {
			int affectedRow = 0;
			//등록 전에 삭제부터 하고
			affectedRow = codeMapper.removeCodeItems((CodeItemDTO)itemList.get(0));
			
			affectedRow = 0;
			affectedRow = codeMapper.registerCodeItems(itemList);
			
			if(affectedRow < 1 || itemList.size() != affectedRow) {
				throw new RuntimeException("코드 입력을 완료하지 못했습니다.\\n잠시 후 다시 시도해주시기 바랍니다.");
			}
		} else {
			LogUtil.warn(log, "등록 할 아이템이 없습니다. itemList.size is {}", itemList.size());
		}
		
	}
	
}
