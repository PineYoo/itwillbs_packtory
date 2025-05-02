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
	public int WarehouseTransactionCount(WarehouseTransactionSearchDTO searchDTO);

	// 창고 정보 목록 + 검색
	public List<WarehouseTransactionDTO> WarehouseTransaction(WarehouseTransactionSearchDTO searchDTO);

	// 창고 정보 상세 조회
	public WarehouseTransactionDTO getWarehouseTransactionByIdx(Long idx);

	// 창고 정보 정보 수정
	public void updateWarehouseTransaction(WarehouseTransactionDTO warehouseTransactionDTO);

	/**
	 * 창고 품질 검사 대기 목록 카운트 - 페이징용
	 * 
	 * @param status 1 입고 검수 대기, 5 출고 검수 대기 조회용
	 * @return int count
	 */
	int getRequiredQCCountBySearchDTO(WarehouseTransactionSearchDTO searchDTO);

	/**
	 * 창고 품질 검사 대기 목록 조회
	 * 
	 * @param status 1 입고 검수 대기, 5 출고 검수 대기 조회용
	 * @return list List<WarehouseTransactionDTO>
	 */
	List<WarehouseTransactionDTO> getRequiredQCListBySearchDTO(WarehouseTransactionSearchDTO searchDTO);
}
