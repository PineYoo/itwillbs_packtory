package kr.co.itwillbs.de.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.common.mapper.SearchMapper;
import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.human.dto.EmployeeCodeDTO;
import kr.co.itwillbs.de.human.dto.EmployeeDTO;
import kr.co.itwillbs.de.human.dto.EmployeeSearchDTO;
import kr.co.itwillbs.de.mes.dto.LocationInfoDTO;
import kr.co.itwillbs.de.mes.dto.LocationInfoSearchDTO;
import kr.co.itwillbs.de.mes.dto.ProductDTO;
import kr.co.itwillbs.de.mes.dto.ProductSearchDTO;
import kr.co.itwillbs.de.mes.dto.RawMaterialDTO;
import kr.co.itwillbs.de.mes.dto.RawMaterialSearchDTO;
import kr.co.itwillbs.de.mes.dto.RawMaterialStockDTO;
import kr.co.itwillbs.de.mes.dto.RawMaterialStockSearchDTO;
import kr.co.itwillbs.de.mes.dto.RecipeDTO;
import kr.co.itwillbs.de.mes.dto.RecipeProcessDTO;
import kr.co.itwillbs.de.mes.dto.RecipeProcessSearchDTO;
import kr.co.itwillbs.de.mes.dto.RecipeSearchDTO;
import kr.co.itwillbs.de.mes.dto.WorkerMetricsDTO;
import kr.co.itwillbs.de.mes.dto.WorkerMetricsSearchDTO;
import kr.co.itwillbs.de.orders.dto.ClientDTO;
import kr.co.itwillbs.de.orders.dto.ClientSearchDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SearchService {
	@Autowired
	private SearchMapper searchMapper;
	
	/**
	 *  사원 정보 리스트 조회
	 * @param employeeSearchDTO
	 * @return List<EmployeeDTO>
	 */
	public List<EmployeeDTO> getEmployeeList(EmployeeSearchDTO employeeSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return searchMapper.getEmployeeList(employeeSearchDTO);
	}
	
	/**
	 * 검색조건에 따른 사원 count 가져오기 페이징용
	 * @param employeeSearchDTO
	 * @return
	 */
	public int getEmployeeCountForPaging(EmployeeSearchDTO employeeSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return searchMapper.getEmployeeCountForPaging(employeeSearchDTO);
	}

	// --------------------------------------------------------------------------------------
	/**
	 * 부서 리스트 조회
	 * @return List<EmployeeCodeDTO>
	 */
	public List<EmployeeCodeDTO> getDepartmentList() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return searchMapper.getDepartmentList();
	}

	/**
	 * 하위 부서 리스트 조회
	 * @return List<EmployeeCodeDTO>
	 */
	public List<EmployeeCodeDTO> getSubDepartmentList(String departmentCode) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return searchMapper.getSubDepartmentList(departmentCode);
	}

	/**
	 * 직급 리스트 조회
	 * @return List<EmployeeCodeDTO>
	 */
	public List<EmployeeCodeDTO> getPositionList() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return searchMapper.getPositionList();
	}

	// ======================================================================================
	/**
	 *  거래처 정보 리스트 조회
	 * @param clientSearchDTO
	 * @return List<ClientDTO>
	 */
	public List<ClientDTO> getClientList(ClientSearchDTO clientSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return searchMapper.getClientList(clientSearchDTO);
	}

	/**
	 * 검색조건에 따른 거래처 count 가져오기 페이징용
	 * @param clientSearchDTO
	 * @return
	 */
	public int getClientCountForPaging(ClientSearchDTO clientSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return searchMapper.getClientCountForPaging(clientSearchDTO);
	}
	
	// ======================================================================================
	/**
	 *  상품 정보 리스트 조회
	 * @param productSearchDTO
	 * @return List<ProductDTO>
	 */
	public List<ProductDTO> getProductList(ProductSearchDTO productSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return searchMapper.getProductList(productSearchDTO);
	}
	
	/**
	 * 검색조건에 따른 상품 count 가져오기 페이징용
	 * @param productSearchDTO
	 * @return
	 */
	public int getProductCountForPaging(ProductSearchDTO productSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return searchMapper.getProductCountForPaging(productSearchDTO);
	}

	
	// ======================================================================================
	/**
	 *  레시피 정보 리스트 조회
	 * @param recipeSearchDTO
	 * @return List<RecipeMasterDTO>
	 */
	public List<RecipeDTO> getRecipeList(RecipeSearchDTO recipeSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return searchMapper.getRecipeList(recipeSearchDTO);
	}
	
	/**
	 * 검색조건에 따른 레시피 count 가져오기 페이징용
	 * @param recipeSearchDTO
	 * @return
	 */
	public int getRecipeCountForPaging(RecipeSearchDTO recipeSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return searchMapper.getRecipeCountForPaging(recipeSearchDTO);
	}
	// ======================================================================================
	// 타입이 공통코드로 자재창고, 생산라인 등 정해지면 필터링 처리 예정
	/**
	 *  라인 정보 리스트 조회
	 * @param recipeSearchDTO
	 * @return List<LocationInfoDTO>
	 */
	public List<LocationInfoDTO> getLocationInfoList(LocationInfoSearchDTO locationInfoSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return searchMapper.getLocationInfoList(locationInfoSearchDTO);
	}
	
	/**
	 * 검색조건에 따른 라인 count 가져오기 페이징용
	 * @param locationInfoSearchDTO
	 * @return
	 */
	public int getLocationInfoCountForPaging(LocationInfoSearchDTO locationInfoSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return searchMapper.getLocationInfoCountForPaging(locationInfoSearchDTO);
	}

	/**
	 * 검색조건에 따른 라인 count 가져오기 페이징용
	 * @param searchDTO
	 * @return
	 */
	public int getWorkerMetricCountForPaging(WorkerMetricsSearchDTO searchDTO) {
		LogUtil.logStart(log);
		
		return searchMapper.getWorkerMetricCountForPaging(searchDTO);
	}
	
	/**
	 * 작업자 스킬 리스트 조회
	 * @param searchDTO
	 * @return
	 */
	public List<WorkerMetricsDTO> getWorkerMetricList(WorkerMetricsSearchDTO searchDTO) {
		LogUtil.logStart(log);
		
		return searchMapper.getWorkerMetricList(searchDTO);
	}

	// ======================================================================================
	/**
	 *  자재 정보 리스트 조회
	 * @param materialSearchDTO
	 * @return List<ClientDTO>
	 */
	public List<RawMaterialDTO> getMaterialList(RawMaterialSearchDTO materialSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return searchMapper.getMaterialList(materialSearchDTO);
	}

	/**
	 * 검색조건에 따른 자재 count 가져오기 페이징용
	 * @param clientSearchDTO
	 * @return
	 */
	public int getMaterialCountForPaging(RawMaterialSearchDTO materialSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return searchMapper.getMaterialCountForPaging(materialSearchDTO);
	}

	// ======================================================================================
	/**
	 *  구매할 자재 정보 리스트 조회
	 * @param materialSearchDTO
	 * @return List<ClientDTO>
	 */
	public List<RawMaterialDTO> getMaterialOrderList(RawMaterialSearchDTO materialSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return searchMapper.getMaterialOrderList(materialSearchDTO);
	}
	
	/**
	 * 검색조건에 따른 자재 count 가져오기 페이징용
	 * @param clientSearchDTO
	 * @return
	 */
	public int getMaterialOrderCountForPaging(RawMaterialSearchDTO materialSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return searchMapper.getMaterialOrderCountForPaging(materialSearchDTO);
	}
	
	// ======================================================================================
	/**
	 *  자재 재고 정보 리스트 조회
	 * @param materialStockSearchDTO
	 * @return List<RawMaterialStockDTO>
	 */
	public List<RawMaterialStockDTO> getMaterialStockList(RawMaterialStockSearchDTO materialStockSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return searchMapper.getMaterialStockList(materialStockSearchDTO);
	}
	
	/**
	 * 검색조건에 따른 자재 재고 count 가져오기 페이징용
	 * @param materialStockSearchDTO
	 * @return
	 */
	public int getMaterialStockCountForPaging(RawMaterialStockSearchDTO materialStockSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return searchMapper.getMaterialStockCountForPaging(materialStockSearchDTO);
	}
	
	// ======================================================================================
	/**
	 *  공정 정보 리스트 조회
	 * @param recipeProcessSearchDTO
	 * @return List<RecipeProcessDTO>
	 */
	public List<RecipeProcessDTO> getRecipeProcessList(RecipeProcessSearchDTO recipeProcessSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return searchMapper.getRecipeProcessList(recipeProcessSearchDTO);
	}
	
	/**
	 * 검색조건에 따른 공정 count 가져오기 페이징용
	 * @param recipeProcessSearchDTO
	 * @return
	 */
	public int getRecipeProcessCountForPaging(RecipeProcessSearchDTO recipeProcessSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return searchMapper.getRecipeProcessCountForPaging(recipeProcessSearchDTO);
	}

	
	public List<ProductSearchDTO> getProductList2() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return searchMapper.getProductList2();
	}


}
