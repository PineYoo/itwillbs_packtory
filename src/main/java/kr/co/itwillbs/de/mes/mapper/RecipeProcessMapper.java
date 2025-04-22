package kr.co.itwillbs.de.mes.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import kr.co.itwillbs.de.mes.dto.RecipeProcessDTO;
import kr.co.itwillbs.de.mes.dto.RecipeProcessSearchDTO;
import kr.co.itwillbs.de.mes.dto.RecipeMasterSearchDTO;

public interface RecipeProcessMapper {

	/**
	 * INSERT INTO t_recipe_process_code VALUES recipeProcessDTO
	 * @param recipeProcessDTO
	 * @return insert key
	 */
	int registerRecipeProcess(RecipeProcessDTO recipeProcessDTO);

	/**
	 * SELECT count(*) from t_recipe_process_code WHERE processSearchDTO (페이징용)
	 * @param processSearchDTO
	 * @return count
	 */
	int getRecipesProcessCountBySearchDTO(RecipeProcessSearchDTO processSearchDTO);
	
	/**
	 * SELECT FROM t_recipe_process_code where processSearchDTO
	 * @param processSearchDTO
	 * @return List<RecipeProcessDTO>
	 */
	List<RecipeProcessDTO> getRecipesProcessBySearchDTO(RecipeProcessSearchDTO processSearchDTO);

	/**
	 * SELECT FROM t_recipe_process_code WHERE idx = #{idx}
	 * @param idx
	 * @return RecipeProcessDTO
	 */
	RecipeProcessDTO getRecipeProcessByIdx(@Param("idx")String idx);

	/**
	 * UPDATE t_recipe_process_code SET recipeProcessDTO
	 * @param recipeProcessDTO
	 * @return affectedRow
	 */
	int modifyRecipeProcess(RecipeProcessDTO recipeProcessDTO);


}
