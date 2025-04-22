package kr.co.itwillbs.de.mes.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.mes.dto.WarehouseTransactionLotsDTO;
import kr.co.itwillbs.de.mes.dto.WarehouseTransactionLotsSearchDTO;

@Mapper
public interface WarehouseTransactionLotsMapper {

	// 트랜잭션 LOT 등록
	public void insertWarehouseTransactionLots(WarehouseTransactionLotsDTO warehouseTransactionLotsDTO);

	// 트랜잭션 LOT 목록 페이징
	public int searchWarehouseTransactionLotsCount(WarehouseTransactionLotsSearchDTO searchDTO);

	// 트랜잭션 LOT 목록 + 검색
	public List<WarehouseTransactionLotsDTO> searchWarehouseTransactionLots(WarehouseTransactionLotsSearchDTO searchDTO);

	// 트랜잭션 LOT 상세 조회
	public WarehouseTransactionLotsDTO getWarehouseTransactionLotsByIdx(Long idx);

	// 트랜잭션 LOT 정보 수정
	public void updateWarehouseTransactionLots(WarehouseTransactionLotsDTO warehouseTransactionLotsDTO);

	// 트랜잭션 LOT 삭제
	public void deleteWarehouseTransactionLots(Long idx);

}
