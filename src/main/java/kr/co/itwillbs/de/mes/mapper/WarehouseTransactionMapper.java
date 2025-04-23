package kr.co.itwillbs.de.mes.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.mes.dto.WarehouseTransactionDTO;
import kr.co.itwillbs.de.mes.dto.WarehouseTransactionSearchDTO;

@Mapper
public interface WarehouseTransactionMapper {

	// 창고 정보 등록
	public void insertWarehouseTransaction(WarehouseTransactionDTO warehouseTransactionDTO);

	// 창고 정보 목록 페이징
	public int searchWarehouseTransactionCount(WarehouseTransactionSearchDTO searchDTO);

	// 창고 정보 목록 + 검색
	public List<WarehouseTransactionDTO> searchWarehouseTransaction(WarehouseTransactionSearchDTO searchDTO);

	// 창고 정보 상세 조회
	public WarehouseTransactionDTO getWarehouseTransactionByIdx(Long idx);

	// 창고 정보 정보 수정
	public void updateWarehouseTransaction(WarehouseTransactionDTO warehouseTransactionDTO);

}
