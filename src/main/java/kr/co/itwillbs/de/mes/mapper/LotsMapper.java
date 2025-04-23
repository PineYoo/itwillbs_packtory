package kr.co.itwillbs.de.mes.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import kr.co.itwillbs.de.mes.dto.LotsDTO;
import kr.co.itwillbs.de.mes.dto.LotsSearchDTO;

public interface LotsMapper {

	/**
	 * INSERT INTO t_lots VALUES lotsDTO
	 * @param RecipeMaterialDTO
	 * @return insert key
	 */
	int registerLots(LotsDTO lotsDTO);

	/**
	 * SELECT count(*) FROM t_lots WHERE lotsSearchDTO (페이징용)
	 * @param lotsSearchDTO
	 * @return count
	 */
	int getLotsCountBySearchDTO(LotsSearchDTO lotsSearchDTO);
	
	/**
	 * SELECT FROM t_lots WHERE recipeMaterialSearchDTO
	 * @param lotsSearchDTO
	 * @return List<LotsDTO>
	 */
	List<LotsDTO> getLotsBySearchDTO(LotsSearchDTO lotsSearchDTO);

	/**
	 * SELECT FROM t_lots WHERE idx = #{idx}
	 * @param idx
	 * @return RecipeMaterialDTO
	 */
	LotsDTO getLotsByIdx(@Param("idx")String idx);

	/**
	 * UPDATE t_lots SET lotsDTO
	 * @param lotsDTO
	 * @return affectedRow
	 */
	int modifyLots(LotsDTO lotsDTO);


}
