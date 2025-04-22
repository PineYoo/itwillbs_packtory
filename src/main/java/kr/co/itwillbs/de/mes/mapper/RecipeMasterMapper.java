package kr.co.itwillbs.de.mes.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import kr.co.itwillbs.de.mes.dto.RecipeProcessDTO;
import kr.co.itwillbs.de.mes.dto.RecipeMaterialDTO;
import kr.co.itwillbs.de.mes.dto.RecipeMaterialSearchDTO;
import kr.co.itwillbs.de.mes.dto.RecipeMasterDTO;
import kr.co.itwillbs.de.mes.dto.RecipeMasterSearchDTO;

public interface RecipeMasterMapper {

	/**
	 * INSERT INTO t_recipe_master VALUES recipeMasterDTO
	 * @param RecipeMasterDTO
	 * @return insert key
	 */
	int registerRecipeMaster(RecipeMasterDTO recipeMasterDTO);

	/**
	 * SELECT count(*) FROM t_recipe_master WHERE recipeMasterSearchDTO (페이징용)
	 * @param recipeMasterSearchDTO
	 * @return count
	 */
	int getRecipeMasterCountBySearchDTO(RecipeMasterSearchDTO recipeMasterSearchDTO);
	
	/**
	 * SELECT FROM t_recipe_master WHERE recipeMasterSearchDTO
	 * @param recipeMasterSearchDTO
	 * @return List<RecipeMasterDTO>
	 */
	List<RecipeMasterDTO> getRecipeMasterBySearchDTO(RecipeMasterSearchDTO recipeMasterSearchDTO);

	/**
	 * SELECT FROM t_recipe_master WHERE idx = #{idx}
	 * @param idx
	 * @return RecipeMasterDTO
	 */
	RecipeMasterDTO getRecipeMasterByIdx(@Param("idx")String idx);

	/**
	 * UPDATE t_recipe_master SET recipeMasterDTO
	 * @param recipeMasterDTO
	 * @return affectedRow
	 */
	int modifyRecipeMaster(RecipeMasterDTO recipeMasterDTO);


}
