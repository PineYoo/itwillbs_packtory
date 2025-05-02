package kr.co.itwillbs.de.mes.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.mes.dto.WarehouseDTO;
import kr.co.itwillbs.de.mes.dto.WarehouseSearchDTO;

@Mapper
public interface WarehouseMapper {

	// 창고 등록
	public void insertWarehouse(WarehouseDTO warehouseDTO);

	// 창고 목록 페이징
	public int WarehouseCount(WarehouseSearchDTO searchDTO);

	// 창고 목록 + 검색
	public List<WarehouseDTO> Warehouse(WarehouseSearchDTO searchDTO);

	// 창고 상세 조회
	public WarehouseDTO getWarehouseByIdx(Long idx);

	// 창고 정보 수정
	public void updateWarehouse(WarehouseDTO warehouseDTO);

	// 창고 정보 들고 가기 (외부용)
	public List<WarehouseDTO> selectWarehouseList();
}
