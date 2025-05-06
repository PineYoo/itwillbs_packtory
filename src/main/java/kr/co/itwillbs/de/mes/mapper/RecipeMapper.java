package kr.co.itwillbs.de.mes.mapper;

import java.util.List;

import org.springframework.data.repository.query.Param;

import kr.co.itwillbs.de.mes.dto.RawMaterialStockDTO;
import kr.co.itwillbs.de.mes.dto.RecipeMasterDTO;
import kr.co.itwillbs.de.orders.dto.OrderItemsDTO;

public interface RecipeMapper {

	/**
	 * 상품_idx에 따른 레시피 조회
	 * @param productIdx
	 * @return List<RecipeMasterDTO>
	 */
	List<RecipeMasterDTO> selectRecipeByProductIdx(String productIdx);

//	List<RawMaterialStockDTO> selectMaterialStockByProductIdx(@Param("productIdx") String productIdx, @Param("quantity") int quantity);
	List<RawMaterialStockDTO> selectMaterialStockByProductIdx(OrderItemsDTO item);



}
