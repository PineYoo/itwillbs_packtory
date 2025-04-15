package kr.co.itwillbs.de.mes.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import kr.co.itwillbs.de.mes.dto.BomDTO;
import kr.co.itwillbs.de.mes.dto.BomSearchDTO;

public interface BomMapper {

	/**
	 * INSERT INTO t_bom values bomDTO
	 * @param bomDTO
	 * @return insert key
	 */
	int registerBom(BomDTO bomDTO);

	/**
	 * SELECT count(*) from t_bom where bomSearchDTO (페이징용)
	 * @param bomSearchDTO
	 * @return count
	 */
	int getBomsCountBySearchDTO(BomSearchDTO bomSearchDTO);
	
	/**
	 * SELECT FROM t_bom where bomSearchDTO
	 * @param bomSearchDTO
	 * @return List<BomDTO>
	 */
	List<BomDTO> getBomsBySearchDTO(BomSearchDTO bomSearchDTO);

	/**
	 * SELECT FROM t_bom where idx = #{idx}
	 * @param idx
	 * @return BomDTO
	 */
	BomDTO getBomByIdx(@Param("idx")String idx);

	/**
	 * UPDATE t_bom SET bomDTO
	 * @param bomDTO
	 * @return affectedRow
	 */
	int modifyBom(BomDTO bomDTO);


}
