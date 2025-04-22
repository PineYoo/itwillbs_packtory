package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
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
	public String registerWarehouse(WarehouseDTO warehouseDTO) {
		log.info("창고 등록 요청: {}", warehouseDTO);
		warehouseMapper.insertWarehouse(warehouseDTO);
		log.info("창고 등록 완료 - name: {}", warehouseDTO.getName());
		return "redirect:/mes/warehouse";
	}

	// 창고 총 개수 (검색 조건 포함)
	public int searchWarehouseCount(WarehouseSearchDTO searchDTO) {
		log.info("창고 개수 조회 - 검색 조건: {}", searchDTO);
		return warehouseMapper.searchWarehouseCount(searchDTO);
	}

	// 창고 목록 조회 (검색 + 페이징)
	public List<WarehouseDTO> getWarehouseList(WarehouseSearchDTO searchDTO) {
		log.info("창고 목록 조회 - 검색 조건: {}", searchDTO);
		return warehouseMapper.searchWarehouse(searchDTO);
	}

	// 창고 상세 조회
	public WarehouseDTO getWarehouseByIdx(Long idx) {
		log.info("창고 상세 조회 - idx: {}", idx);
		return warehouseMapper.getWarehouseByIdx(idx);
	}

	// 창고 수정
	@LogExecution
    @Transactional
    public void updateWarehouse(WarehouseDTO warehouseDTO) {
        log.info("창고 수정 요청 - idx: {}", warehouseDTO.getIdx());

        // productDTO가 null이 아닌지 체크
        if (warehouseDTO != null) {
            // 창고 정보를 업데이트하는 쿼리 호출
        	warehouseMapper.updateWarehouse(warehouseDTO);
            log.info("창고 수정 완료 - name: {}", warehouseDTO.getName());
        } 
    }

	// 창고 정보 담아 가기 (외부용)
	public List<WarehouseDTO> getWarehouseList() {
		return warehouseMapper.selectWarehouseList();
	}
	
}