package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.mes.dto.WarehouseTransactionDTO;
import kr.co.itwillbs.de.mes.dto.WarehouseTransactionSearchDTO;
import kr.co.itwillbs.de.mes.mapper.WarehouseTransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseTransactionService {

	private final WarehouseTransactionMapper warehouseTransactionMapper;

	// 창고 정보 등록
	@LogExecution
	@Transactional
	public String insertWarehouseTransaction(WarehouseTransactionDTO warehouseTransactionDTO) {
		log.info("창고 정보 등록 요청: {}", warehouseTransactionDTO);
		warehouseTransactionMapper.insertWarehouseTransaction(warehouseTransactionDTO);
		log.info("창고 정보 등록 완료 - idx: {}", warehouseTransactionDTO.getIdx());
		return "redirect:/mes/warehousetransaction";
	}

	// 창고 정보 총 개수 (검색 조건 포함)
	public int searchWarehouseTransactionCount(WarehouseTransactionSearchDTO searchDTO) {
		log.info("창고 정보 개수 조회 - 검색 조건: {}", searchDTO);
		return warehouseTransactionMapper.searchWarehouseTransactionCount(searchDTO);
	}

	// 창고 정보 목록 조회 (검색 + 페이징)
	public List<WarehouseTransactionDTO> searchWarehouseTransaction(WarehouseTransactionSearchDTO searchDTO) {
		log.info("창고 정보 목록 조회 - 검색 조건: {}", searchDTO);
		return warehouseTransactionMapper.searchWarehouseTransaction(searchDTO);
	}

	// 창고 정보 상세 조회
	public WarehouseTransactionDTO getWarehouseTransactionByIdx	(Long idx) {
		log.info("창고 정보 상세 조회 - idx: {}", idx);
		return warehouseTransactionMapper.getWarehouseTransactionByIdx(idx);
	}

	// 창고 정보 수정
	@LogExecution
	@Transactional
	public void updateWarehouseTransaction(WarehouseTransactionDTO warehouseTransactionDTO) {
		log.info("창고 정보 수정 요청 - idx: {}", warehouseTransactionDTO.getIdx());

		// WarehouseTransactionDTO 가 널인지 체크
		if (warehouseTransactionDTO != null) {
			// 창고 정보 정보를 업데이트하는 쿼리 호출
			warehouseTransactionMapper.updateWarehouseTransaction(warehouseTransactionDTO);
			log.info("창고 정보 수정 완료 - qcIdx: {}", warehouseTransactionDTO.getIdx());
		}
	}

	// 창고 정보 삭제 (Soft delete)
	@Transactional
	public void deleteWarehouseTransaction(Long idx) {
		log.info("창고 정보 삭제 요청 - idx: {}", idx);
		warehouseTransactionMapper.deleteWarehouseTransaction(idx);
	}

}