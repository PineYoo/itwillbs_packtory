package kr.co.itwillbs.de.mes.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.mes.dto.RawMaterialStockDTO;
import kr.co.itwillbs.de.mes.dto.RawMaterialStockSearchDTO;

@Mapper
public interface RawMaterialStockMapper {

	/**
	 * 자재 재고 조회
	 * @param searchDTO
	 * @return List<RawMaterialStockDTO>
	 */
	List<RawMaterialStockDTO> getMaterialStockList(RawMaterialStockSearchDTO searchDTO);

	/**
	 * 페이징처리용
	 * @param searchDTO
	 * @return int
	 */
	int searchMaterialStockCount(RawMaterialStockSearchDTO searchDTO);


}
