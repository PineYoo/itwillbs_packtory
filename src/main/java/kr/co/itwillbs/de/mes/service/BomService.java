package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.mes.dto.BomDTO;
import kr.co.itwillbs.de.mes.dto.BomSearchDTO;
import kr.co.itwillbs.de.mes.mapper.BomMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BomService {

	private final BomMapper bomMapper;
	public BomService(BomMapper bomMapper) {
		this.bomMapper = bomMapper;
	}
	
	/**
	 * BOM 등록
	 * @param bomDTO
	 * @throws Exception
	 */
	public void registerBom(BomDTO bomDTO) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		bomMapper.registerBom(bomDTO);
	}

	/**
	 * BOM 리스트 페이지 페이징용 카운트
	 * @param bomSearchDTO
	 * @return
	 */
	public int getBomsCountBySearchDTO(BomSearchDTO bomSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return bomMapper.getBomsCountBySearchDTO(bomSearchDTO);
	}
	
	/**
	 * BOM 리스트 검색 조건 조회
	 * @param bomSearchDTO
	 * @return
	 */
	public List<BomDTO> getBomsBySearchDTO(BomSearchDTO bomSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return bomMapper.getBomsBySearchDTO(bomSearchDTO);
	}

	/**
	 * BOM 상세 단건 조회
	 * @param idx
	 * @return
	 */
	public BomDTO getBomByIdx(String idx) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return bomMapper.getBomByIdx(idx);
	}

	/**
	 * BOM 수정
	 * @param bomDTO
	 */
	public void modifyBom(BomDTO bomDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		bomMapper.modifyBom(bomDTO);
	}

}
