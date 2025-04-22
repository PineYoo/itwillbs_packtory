package kr.co.itwillbs.de.mes.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import kr.co.itwillbs.de.mes.dto.RecipeProcessDTO;
import kr.co.itwillbs.de.mes.dto.RecipeMaterialDTO;
import kr.co.itwillbs.de.mes.dto.RecipeMaterialSearchDTO;
import kr.co.itwillbs.de.mes.dto.RecipeMasterSearchDTO;

public interface RecipeMaterialMapper {

	/**
	 * INSERT INTO t_recipe_material VALUES recipeMaterialDTO
	 * @param RecipeMaterialDTO
	 * @return insert key
	 */
	int registerRecipeMaterial(RecipeMaterialDTO recipeMaterialDTO);

	/**
	 * SELECT count(*) from t_recipe_material WHERE recipeMaterialSearchDTO (페이징용)
	 * @param recipeMaterialSearchDTO
	 * @return count
	 */
	int getRecipeMaterialCountBySearchDTO(RecipeMaterialSearchDTO recipeMaterialSearchDTO);
	
	/**
	 * SELECT FROM t_recipe_material WHERE recipeMaterialSearchDTO
	 * @param recipeMaterialSearchDTO
	 * @return List<RecipeDTO>
	 */
	List<RecipeMaterialDTO> getRecipeMaterialBySearchDTO(RecipeMaterialSearchDTO recipeMaterialSearchDTO);

	/**
	 * SELECT FROM t_recipe_material where idx = #{idx}
	 * @param idx
	 * @return RecipeMaterialDTO
	 */
	RecipeMaterialDTO getRecipeMaterialByIdx(@Param("idx")String idx);

	/**
	 * UPDATE t_recipe_material SET recipeMaterialDTO
	 * @param recipeMaterialDTO
	 * @return affectedRow
	 */
	int modifyRecipeMaterial(RecipeMaterialDTO recipeMaterialDTO);


}
