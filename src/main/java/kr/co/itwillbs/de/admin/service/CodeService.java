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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CodeService {

	private final CodeMapper codeMapper;
	//@Autowired
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
	public int registerCode(CodeDTO codeDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		int affectedRow = codeMapper.registerCode(codeDTO);
		log.info("affectedRow is {}", affectedRow);
		return affectedRow;
	}

	/**
	 * SELECT t_commoncode 
	 * @return List<CodeDTO>
	 */
	@Cacheable(value = "majorCodes") // 캐시 저장 기능 value의 이름이 존재/미존재 시 모두 헤서드 호출 후 실행? 맞아?
	public List<CodeDTO> getCodes() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return codeMapper.getCodes();
	}
	
	/**
	 * SELECT t_commoncode_item where is_deleted = 'N' and mojor_code = #{majorCode}
	 * @param majorCode
	 * @return
	 */
	@Cacheable(value = "codeItems", key = "#p0") // #majorCode로 매핑이 될 것 같았는데 계속 Null 이라고 나온다. 기절하겠넹
	public List<CodeItemDTO> getCodeItems(String majorCode) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("Caching with key: {}", majorCode);
		
		return codeMapper.getCodeItems(majorCode);
	}
	
	/**
	 * SELECT count(*) t_commoncode 
	 * @param codeSearchDTO
	 * @return List<CodeDTO>
	 */
	public int getCodesCountBySearchDTO(CodeSearchDTO codeSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return codeMapper.getCodesCountBySearchDTO(codeSearchDTO);
	}
		
	/**
	 * SELECT t_commoncode 
	 * @param codeSearchDTO
	 * @return List<CodeDTO>
	 */
	public List<CodeDTO> getCodesBySearchDTO(CodeSearchDTO codeSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return codeMapper.getCodesBySearchDTO(codeSearchDTO);
	}

	/**
	 * SELECT t_commoncode where idx = #{codeSearchDTO.idx}
	 * @param codeSearchDTO
	 * @return CodeDTO
	 */
	public CodeDTO getCodeByIdx(CodeSearchDTO codeSearchDTO) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return codeMapper.getCodeByIdx(codeSearchDTO);
	}

	/**
	 * SELECT t_commoncode_item where major_code = #{codeSearchDTO.majorCode}
	 * @param codeSearchDTO
	 * @return List<CodeItemDTO>
	 */
	public List<CodeItemDTO> getCodeItemsByMajorCode(CodeSearchDTO codeSearchDTO) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return codeMapper.getCodeItemsByMajorCode(codeSearchDTO);
	}

	/**
	 * UPDATE t_commoncode set codeDTO where idx = #{codeDTO.idx}
	 * @param codeDTO
	 * @throws Exception 
	 */
	@LogExecution
	@CacheEvict(value = "codeItems", key = "#codeDTO.majorCode")
	public void modifyCode(CodeDTO codeDTO) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		int affectedRow = codeMapper.modifyCode(codeDTO);
		if(affectedRow < 1) {
			throw new Exception("그룹코드 수정을 완료하지 못했습니다.\\n잠시 후 다시 시도해주시기 바랍니다.");
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
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		int affectedRow = 0;
		if(itemList.size() > 0) {
			//등록 전에 삭제부터 하고
			affectedRow = codeMapper.removeCodeItems((CodeItemDTO)itemList.get(0));
			log.info("after removeCodeItems, affectedRow is {}", affectedRow);
		}
		
		affectedRow = 0;
		affectedRow = codeMapper.registerCodeItems(itemList);
		log.info("itemList.size is {}, // affectedRow is {}", itemList.size(), affectedRow);
		
		if(affectedRow < 1 || itemList.size() != affectedRow) {
			throw new Exception("코드 입력을 완료하지 못했습니다.\\n잠시 후 다시 시도해주시기 바랍니다.");
		}
	}
	
}
