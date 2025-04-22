package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.mes.dto.WarehouseTransactionLotsDTO;
import kr.co.itwillbs.de.mes.dto.WarehouseTransactionLotsSearchDTO;
import kr.co.itwillbs.de.mes.mapper.WarehouseTransactionLotsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseTransactionLotsService {

	private final WarehouseTransactionLotsMapper warehouseTransactionLotsMapper;

	// 트랜잭션 LOT 등록
	@LogExecution
	@Transactional
	public String insertWarehouseTransactionLots(WarehouseTransactionLotsDTO warehouseTransactionLotsDTO) {
		log.info("트랜잭션 LOT 등록 요청: {}", warehouseTransactionLotsDTO);
		warehouseTransactionLotsMapper.insertWarehouseTransactionLots(warehouseTransactionLotsDTO);
		log.info("트랜잭션 LOT 등록 완료 - idx: {}", warehouseTransactionLotsDTO.getIdx());
		return "redirect:/mes/warehousetransactionlots";
	}

	// 트랜잭션 LOT 총 개수 (검색 조건 포함)
	public int searchWarehouseTransactionLotsCount(WarehouseTransactionLotsSearchDTO searchDTO) {
		log.info("트랜잭션 LOT 개수 조회 - 검색 조건: {}", searchDTO);
		return warehouseTransactionLotsMapper.searchWarehouseTransactionLotsCount(searchDTO);
	}

	// 트랜잭션 LOT 목록 조회 (검색 + 페이징)
	public List<WarehouseTransactionLotsDTO> searchWarehouseTransactionLots(WarehouseTransactionLotsSearchDTO searchDTO) {
		log.info("트랜잭션 LOT 목록 조회 - 검색 조건: {}", searchDTO);
		return warehouseTransactionLotsMapper.searchWarehouseTransactionLots(searchDTO);
	}

	// 트랜잭션 LOT 상세 조회
	public WarehouseTransactionLotsDTO getWarehouseTransactionLotsByIdx	(Long idx) {
		log.info("트랜잭션 LOT 상세 조회 - idx: {}", idx);
		return warehouseTransactionLotsMapper.getWarehouseTransactionLotsByIdx(idx);
	}

	// 트랜잭션 LOT 수정
	@LogExecution
	@Transactional
	public void updateWarehouseTransactionLots(WarehouseTransactionLotsDTO warehouseTransactionLotsDTO) {
		log.info("트랜잭션 LOT 수정 요청 - idx: {}", warehouseTransactionLotsDTO.getIdx());

		// WarehouseTransactionLotsDTO 가 널인지 체크
		if (warehouseTransactionLotsDTO != null) {
			// 트랜잭션 LOT 정보를 업데이트하는 쿼리 호출
			warehouseTransactionLotsMapper.updateWarehouseTransactionLots(warehouseTransactionLotsDTO);
			log.info("트랜잭션 LOT 수정 완료 - qcIdx: {}", warehouseTransactionLotsDTO.getIdx());
		}
	}

	// 트랜잭션 LOT 삭제 (Soft delete)
	@Transactional
	public void deleteWarehouseTransactionLots(Long idx) {
		log.info("트랜잭션 LOT 삭제 요청 - idx: {}", idx);
		warehouseTransactionLotsMapper.deleteWarehouseTransactionLots(idx);
	}

}