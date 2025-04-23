package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.mes.dto.RecipeProcessDTO;
import kr.co.itwillbs.de.mes.dto.RecipeProcessSearchDTO;
import kr.co.itwillbs.de.mes.mapper.RecipeProcessMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RecipeProcessService {

	private final RecipeProcessMapper recipeProcessMapper;
	public RecipeProcessService(RecipeProcessMapper recipeProcessMapper) {
		this.recipeProcessMapper = recipeProcessMapper;
	}
	
	/**
	 * 레시피 공정 등록
	 * @param bomDTO
	 * @throws Exception
	 */
	public void registerRecipeProcess(RecipeProcessDTO recipeProcessDTO) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		recipeProcessMapper.registerRecipeProcess(recipeProcessDTO);
	}

	/**
	 * 레시피 공정 페이지 페이징용 카운트
	 * @param recipeProcessSearchDTO
	 * @return int
	 */
	public int getRecipesCountBySearchDTO(RecipeProcessSearchDTO recipeProcessSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return recipeProcessMapper.getRecipesProcessCountBySearchDTO(recipeProcessSearchDTO);
	}
	
	/**
	 * 레시피 공정 리스트 검색 조건 조회
	 * @param bomSearchDTO
	 * @return List<RecipeProcessDTO>
	 */
	public List<RecipeProcessDTO> getRecipesProcessBySearchDTO(RecipeProcessSearchDTO recipeProcessSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return recipeProcessMapper.getRecipesProcessBySearchDTO(recipeProcessSearchDTO);
	}

	/**
	 * 레시피 공정 상세 단건 조회
	 * @param idx
	 * @return RecipeProcessDTO
	 */
	public RecipeProcessDTO getRecipeProcessByIdx(String idx) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return recipeProcessMapper.getRecipeProcessByIdx(idx);
	}

	/**
	 * 레시피 공정 수정
	 * @param bomDTO
	 */
	public void modifyRecipeProcess(RecipeProcessDTO recipeProcessDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		recipeProcessMapper.modifyRecipeProcess(recipeProcessDTO);
	}

}
