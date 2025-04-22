package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.mes.dto.RecipeProcessDTO;
import kr.co.itwillbs.de.mes.dto.RecipeMaterialDTO;
import kr.co.itwillbs.de.mes.dto.RecipeMaterialSearchDTO;
import kr.co.itwillbs.de.mes.dto.LotsDTO;
import kr.co.itwillbs.de.mes.dto.LotsSearchDTO;
import kr.co.itwillbs.de.mes.dto.RecipeMasterSearchDTO;
import kr.co.itwillbs.de.mes.mapper.RecipeProcessMapper;
import kr.co.itwillbs.de.mes.mapper.LotsMapper;
import kr.co.itwillbs.de.mes.mapper.RecipeMaterialMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LotsService {

	private final LotsMapper lotsMapper;
	public LotsService(LotsMapper lotsMapper) {
		this.lotsMapper = lotsMapper;
	}
	
	/**
	 * Lot 등록
	 * @param lotsDTO
	 * @throws Exception
	 */
	public void registerLots(LotsDTO lotsDTO) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		lotsMapper.registerLots(lotsDTO);
	}

	/**
	 * Lot 리스트 페이지 페이징용 카운트
	 * @param lotsSearchDTO
	 * @return int
	 */
	public int getLotsCountBySearchDTO(LotsSearchDTO lotsSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return lotsMapper.getLotsCountBySearchDTO(lotsSearchDTO);
	}
	
	/**
	 * Lot 리스트 검색 조건 조회
	 * @param lotsSearchDTO
	 * @return
	 */
	public List<LotsDTO> getRecipeLotsBySearchDTO(LotsSearchDTO lotsSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return lotsMapper.getLotsBySearchDTO(lotsSearchDTO);
	}

	/**
	 * Lot 상세 단건 조회
	 * @param idx
	 * @return LotsDTO
	 */
	public LotsDTO getLotsByIdx(String idx) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return lotsMapper.getLotsByIdx(idx);
	}

	/**
	 * Lot 수정
	 * @param recipeMaterialDTO
	 */
	public void modifyLots(LotsDTO lotsDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		lotsMapper.modifyLots(lotsDTO);
	}

}
