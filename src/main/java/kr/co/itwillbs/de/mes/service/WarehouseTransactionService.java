package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.common.util.LogUtil;
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
	public void insertWarehouseTransaction(WarehouseTransactionDTO warehouseTransactionDTO) {
		LogUtil.logStart(log);

		warehouseTransactionMapper.insertWarehouseTransaction(warehouseTransactionDTO);
	}

	// 창고 정보 총 개수 (검색 조건 포함)
	public int searchWarehouseTransactionCount(WarehouseTransactionSearchDTO searchDTO) {
		LogUtil.logStart(log);

		return warehouseTransactionMapper.WarehouseTransactionCount(searchDTO);
	}

	// 창고 정보 목록 조회 (검색 + 페이징)
	public List<WarehouseTransactionDTO> searchWarehouseTransaction(WarehouseTransactionSearchDTO searchDTO) {
		LogUtil.logStart(log);

		return warehouseTransactionMapper.WarehouseTransaction(searchDTO);
	}

	// 창고 정보 상세 조회
	public WarehouseTransactionDTO getWarehouseTransactionByIdx(Long idx) {
		LogUtil.logStart(log);

		return warehouseTransactionMapper.getWarehouseTransactionByIdx(idx);
	}

	// 창고 정보 수정
	@LogExecution
	@Transactional
	public void updateWarehouseTransaction(WarehouseTransactionDTO warehouseTransactionDTO) {
		LogUtil.logStart(log);

		warehouseTransactionMapper.updateWarehouseTransaction(warehouseTransactionDTO);
	}
}