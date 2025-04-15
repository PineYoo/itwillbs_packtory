package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.mes.dto.RecipeDTO;
import kr.co.itwillbs.de.mes.dto.RecipeSearchDTO;
import kr.co.itwillbs.de.mes.mapper.RecipeMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RecipeService {

	private final RecipeMapper recipeMapper;
	public RecipeService(RecipeMapper recipeMapper) {
		this.recipeMapper = recipeMapper;
	}
	
	/**
	 * BOM 등록
	 * @param bomDTO
	 * @throws Exception
	 */
	public void registerRecipe(RecipeDTO bomDTO) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		recipeMapper.registerRecipe(bomDTO);
	}

	/**
	 * BOM 리스트 페이지 페이징용 카운트
	 * @param bomSearchDTO
	 * @return
	 */
	public int getRecipesCountBySearchDTO(RecipeSearchDTO bomSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return recipeMapper.getRecipesCountBySearchDTO(bomSearchDTO);
	}
	
	/**
	 * BOM 리스트 검색 조건 조회
	 * @param bomSearchDTO
	 * @return
	 */
	public List<RecipeDTO> getRecipesBySearchDTO(RecipeSearchDTO bomSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return recipeMapper.getRecipesBySearchDTO(bomSearchDTO);
	}

	/**
	 * BOM 상세 단건 조회
	 * @param idx
	 * @return
	 */
	public RecipeDTO getRecipeByIdx(String idx) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return recipeMapper.getRecipeByIdx(idx);
	}

	/**
	 * BOM 수정
	 * @param bomDTO
	 */
	public void modifyRecipe(RecipeDTO bomDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		recipeMapper.modifyRecipe(bomDTO);
	}

}
