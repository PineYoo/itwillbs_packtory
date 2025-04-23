package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.mes.dto.RecipeMasterDTO;
import kr.co.itwillbs.de.mes.dto.RecipeMasterSearchDTO;
import kr.co.itwillbs.de.mes.mapper.RecipeMasterMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RecipeMasterService {

	private final RecipeMasterMapper recipeMasterMapper;
	public RecipeMasterService(RecipeMasterMapper recipeMasterMapper) {
		this.recipeMasterMapper = recipeMasterMapper;
	}
	
	/**
	 * 레시피 등록
	 * @param recipeMasterDTO
	 * @throws Exception
	 */
	public void registerRecipeMaster(RecipeMasterDTO recipeMasterDTO) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		recipeMasterMapper.registerRecipeMaster(recipeMasterDTO);
	}

	/**
	 * 레시피 리스트 페이지 페이징용 카운트
	 * @param recipeMasterSearchDTO
	 * @return int
	 */
	public int getRecipeMasterCountBySearchDTO(RecipeMasterSearchDTO recipeMasterSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return recipeMasterMapper.getRecipeMasterCountBySearchDTO(recipeMasterSearchDTO);
	}
	
	/**
	 * 레시피 리스트 검색 조건 조회
	 * @param recipeMasterSearchDTO
	 * @return List<RecipeMasterDTO>
	 */
	public List<RecipeMasterDTO> getRecipeMasterBySearchDTO(RecipeMasterSearchDTO recipeMasterSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return recipeMasterMapper.getRecipeMasterBySearchDTO(recipeMasterSearchDTO);
	}

	/**
	 * 레시피 상세 단건 조회
	 * @param idx
	 * @return RecipeMasterDTO
	 */
	public RecipeMasterDTO getRecipeMasterByIdx(String idx) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return recipeMasterMapper.getRecipeMasterByIdx(idx);
	}

	/**
	 * 레시피 수정
	 * @param recipeMasterDTO
	 */
	public void modifyRecipeMaster(RecipeMasterDTO recipeMasterDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		recipeMasterMapper.modifyRecipeMaster(recipeMasterDTO);
	}

}
