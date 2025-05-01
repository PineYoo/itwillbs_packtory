package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.mes.dto.RawMaterialStockDTO;
import kr.co.itwillbs.de.mes.dto.RawMaterialStockSearchDTO;
import kr.co.itwillbs.de.mes.mapper.RawMaterialStockMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RawMaterialStockService {

	private final RawMaterialStockMapper rawMaterialStockMapper; // 마스터 자재 관련 Mapper 주입

	/**
	 * 자재 재고 조회
	 * @param searchDTO
	 * @return List<RawMaterialStockDTO>
	 */
	public List<RawMaterialStockDTO> getMaterialStockList(RawMaterialStockSearchDTO searchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return rawMaterialStockMapper.getMaterialStockList(searchDTO);
	}

	/**
	 * 페이징처리용
	 * @param searchDTO
	 * @return int
	 */
	public int searchMaterialStockCount(RawMaterialStockSearchDTO searchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return rawMaterialStockMapper.searchMaterialStockCount(searchDTO);
	}

	
}
