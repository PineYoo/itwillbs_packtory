package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.mes.dto.WarehouseDTO;
import kr.co.itwillbs.de.mes.dto.WarehouseSearchDTO;
import kr.co.itwillbs.de.mes.mapper.WarehouseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseService {

	private final WarehouseMapper warehouseMapper;

	// 창고 등록
	@LogExecution
	@Transactional
	public void registerWarehouse(WarehouseDTO warehouseDTO) {
		LogUtil.logStart(log);

		warehouseMapper.insertWarehouse(warehouseDTO);
	}

	// 창고 총 개수 (검색 조건 포함)
	public int searchWarehouseCount(WarehouseSearchDTO searchDTO) {
		LogUtil.logStart(log);

		return warehouseMapper.WarehouseCount(searchDTO);
	}

	// 창고 목록 조회 (검색 + 페이징)
	public List<WarehouseDTO> getWarehouseList(WarehouseSearchDTO searchDTO) {
		LogUtil.logStart(log);

		return warehouseMapper.Warehouse(searchDTO);
	}

	// 창고 상세 조회
	public WarehouseDTO getWarehouseByIdx(Long idx) {
		LogUtil.logStart(log);

		return warehouseMapper.getWarehouseByIdx(idx);
	}

	// 창고 수정
	@LogExecution
	@Transactional
	public void updateWarehouse(WarehouseDTO warehouseDTO) {
		LogUtil.logStart(log);

		warehouseMapper.updateWarehouse(warehouseDTO);
	}

	// 창고 정보 담아 가기 (외부용)
	public List<WarehouseDTO> getWarehouseList() {
		return warehouseMapper.selectWarehouseList();
	}

}