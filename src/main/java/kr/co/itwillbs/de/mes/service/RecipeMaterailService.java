package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.mes.dto.RecipeProcessDTO;
import kr.co.itwillbs.de.mes.dto.RecipeMaterialDTO;
import kr.co.itwillbs.de.mes.dto.RecipeMaterialSearchDTO;
import kr.co.itwillbs.de.mes.dto.RecipeMasterSearchDTO;
import kr.co.itwillbs.de.mes.mapper.RecipeProcessMapper;
import kr.co.itwillbs.de.mes.mapper.RecipeMaterialMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RecipeMaterailService {

	private final RecipeMaterialMapper recipeMaterialMapper;
	public RecipeMaterailService(RecipeMaterialMapper recipeMaterialMapper) {
		this.recipeMaterialMapper = recipeMaterialMapper;
	}
	
	/**
	 * 소요자재 등록
	 * @param recipeMaterialDTO
	 * @throws Exception
	 */
	public void registerRecipeMaterial(RecipeMaterialDTO recipeMaterialDTO) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		recipeMaterialMapper.registerRecipeMaterial(recipeMaterialDTO);
	}

	/**
	 * 소요자재 리스트 페이지 페이징용 카운트
	 * @param recipeMaterialSearchDTO
	 * @return
	 */
	public int getRecipeMaterialCountBySearchDTO(RecipeMaterialSearchDTO recipeMaterialSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return recipeMaterialMapper.getRecipeMaterialCountBySearchDTO(recipeMaterialSearchDTO);
	}
	
	/**
	 * 소요자재 리스트 검색 조건 조회
	 * @param recipeMaterialSearchDTO
	 * @return
	 */
	public List<RecipeMaterialDTO> getRecipeMaterialBySearchDTO(RecipeMaterialSearchDTO recipeMaterialSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return recipeMaterialMapper.getRecipeMaterialBySearchDTO(recipeMaterialSearchDTO);
	}

	/**
	 * 소요자재 상세 단건 조회
	 * @param idx
	 * @return
	 */
	public RecipeMaterialDTO getRecipeMaterialByIdx(String idx) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return recipeMaterialMapper.getRecipeMaterialByIdx(idx);
	}

	/**
	 * 소요자재 수정
	 * @param recipeMaterialDTO
	 */
	public void modifyRecipeMaterial(RecipeMaterialDTO recipeMaterialDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		recipeMaterialMapper.modifyRecipeMaterial(recipeMaterialDTO);
	}

}
