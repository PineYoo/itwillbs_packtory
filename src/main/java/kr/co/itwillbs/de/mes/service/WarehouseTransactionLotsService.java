package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.common.util.LogUtil;
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
	public void insertWarehouseTransactionLots(WarehouseTransactionLotsDTO warehouseTransactionLotsDTO) {
		LogUtil.logStart(log);

		warehouseTransactionLotsMapper.insertWarehouseTransactionLots(warehouseTransactionLotsDTO);
	}

	// 트랜잭션 LOT 총 개수 (검색 조건 포함)
	public int searchWarehouseTransactionLotsCount(WarehouseTransactionLotsSearchDTO searchDTO) {
		LogUtil.logStart(log);

		return warehouseTransactionLotsMapper.WarehouseTransactionLotsCount(searchDTO);
	}

	// 트랜잭션 LOT 목록 조회 (검색 + 페이징)
	public List<WarehouseTransactionLotsDTO> searchWarehouseTransactionLots(
			WarehouseTransactionLotsSearchDTO searchDTO) {
		LogUtil.logStart(log);

		return warehouseTransactionLotsMapper.WarehouseTransactionLots(searchDTO);
	}

	// 트랜잭션 LOT 상세 조회
	public WarehouseTransactionLotsDTO getWarehouseTransactionLotsByIdx(Long idx) {
		LogUtil.logStart(log);

		return warehouseTransactionLotsMapper.getWarehouseTransactionLotsByIdx(idx);
	}

	// 트랜잭션 LOT 수정
	@LogExecution
	@Transactional
	public void updateWarehouseTransactionLots(WarehouseTransactionLotsDTO warehouseTransactionLotsDTO) {
		LogUtil.logStart(log);

		warehouseTransactionLotsMapper.updateWarehouseTransactionLots(warehouseTransactionLotsDTO);
	}
}