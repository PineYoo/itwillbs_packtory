package kr.co.itwillbs.de.mes.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import kr.co.itwillbs.de.mes.dto.RecipeDTO;
import kr.co.itwillbs.de.mes.dto.RecipeSearchDTO;

public interface RecipeMapper {

	/**
	 * INSERT INTO t_bom values bomDTO
	 * @param bomDTO
	 * @return insert key
	 */
	int registerRecipe(RecipeDTO bomDTO);

	/**
	 * SELECT count(*) from t_bom where bomSearchDTO (페이징용)
	 * @param bomSearchDTO
	 * @return count
	 */
	int getRecipesCountBySearchDTO(RecipeSearchDTO bomSearchDTO);
	
	/**
	 * SELECT FROM t_bom where bomSearchDTO
	 * @param bomSearchDTO
	 * @return List<RecipeDTO>
	 */
	List<RecipeDTO> getRecipesBySearchDTO(RecipeSearchDTO bomSearchDTO);

	/**
	 * SELECT FROM t_bom where idx = #{idx}
	 * @param idx
	 * @return RecipeDTO
	 */
	RecipeDTO getRecipeByIdx(@Param("idx")String idx);

	/**
	 * UPDATE t_bom SET bomDTO
	 * @param bomDTO
	 * @return affectedRow
	 */
	int modifyRecipe(RecipeDTO bomDTO);


}
