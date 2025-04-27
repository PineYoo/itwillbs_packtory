package kr.co.itwillbs.de.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.human.dto.EmployeeCodeDTO;
import kr.co.itwillbs.de.human.dto.EmployeeDTO;
import kr.co.itwillbs.de.human.dto.EmployeeSearchDTO;
import kr.co.itwillbs.de.mes.dto.LocationInfoDTO;
import kr.co.itwillbs.de.mes.dto.LocationInfoSearchDTO;
import kr.co.itwillbs.de.mes.dto.ProductDTO;
import kr.co.itwillbs.de.mes.dto.ProductSearchDTO;
import kr.co.itwillbs.de.mes.dto.RawMaterialDTO;
import kr.co.itwillbs.de.mes.dto.RawMaterialSearchDTO;
import kr.co.itwillbs.de.mes.dto.RecipeDTO;
import kr.co.itwillbs.de.mes.dto.RecipeProcessDTO;
import kr.co.itwillbs.de.mes.dto.RecipeProcessSearchDTO;
import kr.co.itwillbs.de.mes.dto.RecipeSearchDTO;
import kr.co.itwillbs.de.mes.dto.WorkerMetricsDTO;
import kr.co.itwillbs.de.mes.dto.WorkerMetricsSearchDTO;
import kr.co.itwillbs.de.orders.dto.ClientDTO;
import kr.co.itwillbs.de.orders.dto.ClientSearchDTO;

@Mapper
public interface SearchMapper {

	/**
	 * 검색조건에 따른 사원 리스트 조회
	 * @param employeeSearchDTO
	 * @return List<EmployeeDTO>
	 */
	List<EmployeeDTO> getEmployeeList(EmployeeSearchDTO employeeSearchDTO);

	/**
	 * 페이징용 카운트
	 */
	int getEmployeeCountForPaging(EmployeeSearchDTO employeeSearchDTO);

	// --------------------------------------------------------------------
	/**
	 * 부서 리스트 조회
	 * @return List<EmployeeCodeDTO>
	 */
	List<EmployeeCodeDTO> getDepartmentList();

	/**
	 * 하위 부서 리스트 조회
	 * @return List<EmployeeCodeDTO>
	 */
	List<EmployeeCodeDTO> getSubDepartmentList(String departmentCode);
	
	/**
	 * 직급 리스트 조회
	 * @return List<EmployeeCodeDTO>
	 */
	List<EmployeeCodeDTO> getPositionList();

	// ====================================================================
	/**
	 * 검색조건에 따른 거래처 리스트 조회
	 * @param clientSearchDTO
	 * @return List<ClientDTO>
	 */
	List<ClientDTO> getClientList(ClientSearchDTO clientSearchDTO);

	/**
	 * 페이징용 카운트
	 */
	int getClientCountForPaging(ClientSearchDTO clientSearchDTO);
	
	// ====================================================================
	/**
	 * 검색조건에 따른 상품 리스트 조회
	 * @param productSearchDTO
	 * @return List<ProductDTO>
	 */
	List<ProductDTO> getProductList(ProductSearchDTO productSearchDTO);
	
	/**
	 * 페이징용 카운트
	 */
	int getProductCountForPaging(ProductSearchDTO productSearchDTO);
	
	// ====================================================================
	/**
	 * 검색조건에 따른 레시피 리스트 조회
	 * @param recipeSearchDTO
	 * @return List<RecipeMasterDTO>
	 */
	List<RecipeDTO> getRecipeList(RecipeSearchDTO recipeSearchDTO);
	
	/**
	 * 페이징용 카운트
	 */
	int getRecipeCountForPaging(RecipeSearchDTO recipeSearchDTO);
	
	// ====================================================================
	// 타입이 공통코드로 자재창고, 생산라인 등 정해지면 필터링 처리 예정
	/**
	 * 검색조건에 따른 라인 리스트 조회
	 * @param locationInfoSearchDTO
	 * @return List<LocationInfoDTO>
	 */
	List<LocationInfoDTO> getLocationInfoList(LocationInfoSearchDTO locationInfoSearchDTO);
	
	/**
	 * 페이징용 카운트
	 */
	int getLocationInfoCountForPaging(LocationInfoSearchDTO locationInfoSearchDTO);

	// ====================================================================
	// 작업자 스킬목록을 조회한다
	
	/**
	 * 페이징용 카운트
	 * @param searchDTO
	 * @return
	 */
	int getWorkerMetricCountForPaging(WorkerMetricsSearchDTO searchDTO);
	
	/**
	 * 작업자 스킬 조회
	 * @param searchDTO
	 * @return
	 */
	List<WorkerMetricsDTO> getWorkerMetricList(WorkerMetricsSearchDTO searchDTO);

	// ====================================================================
	/**
	 * 검색조건에 따른 자재 리스트 조회
	 * @param materialSearchDTO
	 * @return List<RawMaterialDTO>
	 */
	List<RawMaterialDTO> getMaterialList(RawMaterialSearchDTO materialSearchDTO);

	/**
	 * 페이징용 카운트
	 */
	int getMaterialCountForPaging(RawMaterialSearchDTO materialSearchDTO);
	
	// ====================================================================
	/**
	 * 검색조건에 따른 공정 리스트 조회
	 * @param recipeProcessSearchDTO
	 * @return List<RecipeProcessDTO>
	 */
	List<RecipeProcessDTO> getRecipeProcessList(RecipeProcessSearchDTO recipeProcessSearchDTO);
	
	/**
	 * 페이징용 카운트
	 */
	int getRecipeProcessCountForPaging(RecipeProcessSearchDTO recipeProcessSearchDTO);
	

}
