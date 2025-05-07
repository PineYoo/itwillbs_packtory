package kr.co.itwillbs.de.common.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.itwillbs.de.common.service.SearchService;
import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.common.vo.PageDTO;
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
@Controller
public class SearchController {
/**
 * <pre>
 * 제가 만든 코드들이 Model을 적어놨었군요! 죄송...ㄱ-)
 * @ResponseBody는 return 에 있는 값만! Body에 담아서 응답하기에 @PostMapping 메서드는 Model이 필요가 없습니다!
 * 스프링MVC에서 String 반환 값을 가진 메서드들만 viewResolver를 거치면서 Model 에 담긴 값들이 전달 됩니다.
 * </pre>
 */
	@Autowired
	private SearchService searchService;

	// 사원 조회 팝업창으로 이동
	@GetMapping(value = { "/employee/search-popup", "/employee/search-popup/" })
	public String getEmployeeList(Model model, @ModelAttribute EmployeeSearchDTO employeeSearchDTO) {
		LogUtil.logStart(log);
		// 사원 리스트 조회 요청(SELECT)
		List<EmployeeDTO> employeeList = searchService.getEmployeeList(employeeSearchDTO);
		model.addAttribute("employeeList", employeeList);
		
		//페이징용 totalCount
		employeeSearchDTO.getPageDTO().setTotalCount(searchService.getEmployeeCountForPaging(employeeSearchDTO));
		model.addAttribute("employeeSearchDTO", employeeSearchDTO);
		
		return "/common/employee_search_form";
	}

	// 검색조건에 따른 사원 목록
	@PostMapping("/employee/search-popup")
	@ResponseBody
	public List<EmployeeDTO> searchEmployeeList(Model model, @ModelAttribute EmployeeSearchDTO employeeSearchDTO) {
		LogUtil.logStart(log);

		//페이징용 totalCount
		employeeSearchDTO.getPageDTO().setTotalCount(searchService.getEmployeeCountForPaging(employeeSearchDTO));
		model.addAttribute("employeeSearchDTO", employeeSearchDTO);
		
		return searchService.getEmployeeList(employeeSearchDTO);
	}
	
	// ----------------------------------------------------------------------------------
	// 부서 리스트 조회
	@GetMapping("/api/getDepartments")
	@ResponseBody
	public List<EmployeeCodeDTO> getDepartmentList() {
		LogUtil.logStart(log);
		return searchService.getDepartmentList(); // major_code 기준
	}
	
	// 하위 부서 리스트 조회
	@GetMapping("/api/getSubDepartments")
	@ResponseBody
	public List<EmployeeCodeDTO> getSubDepartmentList(@RequestParam("departmentCode") String departmentCode) {
		LogUtil.logStart(log);
		return searchService.getSubDepartmentList(departmentCode); // 하위 minor_code
	}

	// 직급 리스트 조회
	@GetMapping("/api/getPositions")
	@ResponseBody
	public List<EmployeeCodeDTO> getPositionList() {
		LogUtil.logStart(log);
		return searchService.getPositionList(); // major_code 기준
	}
	
	// ==================================================================================
	// 거래처 조회 팝업창으로 이동
	@GetMapping(value = { "/client/search-popup", "/client/search-popup/" })
	public String getClientList(Model model, @ModelAttribute ClientSearchDTO clientSearchDTO) {
		LogUtil.logStart(log);
		// 사원 리스트 조회 요청(SELECT)
		List<ClientDTO> clientList = searchService.getClientList(clientSearchDTO);
		model.addAttribute("clientList", clientList);
		
//		//페이징용 totalCount
		clientSearchDTO.getPageDTO().setTotalCount(searchService.getClientCountForPaging(clientSearchDTO));
		model.addAttribute("clientSearchDTO", clientSearchDTO);
		
		return "/common/client_search_form";
	}
	
	// 검색조건에 따른 거래처 목록
	@PostMapping("/client/search-popup")
	@ResponseBody
	public List<ClientDTO> searchClientList(Model model, @ModelAttribute ClientSearchDTO clientSearchDTO) {
		LogUtil.logStart(log);
		
		//페이징용 totalCount
		clientSearchDTO.getPageDTO().setTotalCount(searchService.getClientCountForPaging(clientSearchDTO));
		model.addAttribute("clientSearchDTO", clientSearchDTO);
		
		return searchService.getClientList(clientSearchDTO);
	}

	// ==================================================================================
	// 상품 조회 팝업창으로 이동
	@GetMapping(value = { "/product/search-popup", "/product/search-popup/" })
	public String getProductList(Model model, @ModelAttribute ProductSearchDTO productSearchDTO) {
		LogUtil.logStart(log);
		// 상품 리스트 조회 요청(SELECT)
		List<ProductDTO> productList = searchService.getProductList(productSearchDTO);
		model.addAttribute("productList", productList);
		
//		//페이징용 totalCount
		productSearchDTO.getPageDTO().setTotalCount(searchService.getProductCountForPaging(productSearchDTO));
		model.addAttribute("productSearchDTO", productSearchDTO);
		
		return "/common/product_search_form";
	}
	
	// 검색조건에 따른 상품 목록
	@PostMapping("/product/search-popup")
	@ResponseBody
	public List<ProductDTO> searchProductList(Model model, @ModelAttribute ProductSearchDTO productSearchDTO) {
		LogUtil.logStart(log);
		
		//페이징용 totalCount
		productSearchDTO.getPageDTO().setTotalCount(searchService.getProductCountForPaging(productSearchDTO));
		model.addAttribute("productSearchDTO", productSearchDTO);
		
		return searchService.getProductList(productSearchDTO);
	}
	
	// ==================================================================================
	// 레시피 조회 팝업창으로 이동
	@GetMapping(value = { "/recipe/search-popup", "/recipe/search-popup/" })
	public String getRecipeList(Model model, @ModelAttribute RecipeSearchDTO recipeSearchDTO) {
		LogUtil.logStart(log);
		// 레시피 리스트 조회 요청(SELECT)
		List<RecipeDTO> recipeList = searchService.getRecipeList(recipeSearchDTO);
		model.addAttribute("recipeList", recipeList);
		
//		//페이징용 totalCount
		recipeSearchDTO.getPageDTO().setTotalCount(searchService.getRecipeCountForPaging(recipeSearchDTO));
		model.addAttribute("recipeSearchDTO", recipeSearchDTO);
		
		return "/common/recipe_search_form";
	}
	
	// 검색조건에 따른 레시피 목록
	@PostMapping("/recipe/search-popup")
	@ResponseBody
	public List<RecipeDTO> searchRecipeList(Model model, @ModelAttribute RecipeSearchDTO recipeSearchDTO) {
		LogUtil.logStart(log);
		
		//페이징용 totalCount
		recipeSearchDTO.getPageDTO().setTotalCount(searchService.getRecipeCountForPaging(recipeSearchDTO));
		model.addAttribute("recipeSearchDTO", recipeSearchDTO);
		
		return searchService.getRecipeList(recipeSearchDTO);
	}
	
	// ==================================================================================
	// 타입이 공통코드로 자재창고, 생산라인 등 정해지면 필터링 처리 예정
	// 라인 조회 팝업창 이동
	@GetMapping(value = { "/location/search-popup", "/location/search-popup/" })
	public String getRecipeList(Model model, @ModelAttribute LocationInfoSearchDTO locationInfoSearchDTO) {
		LogUtil.logStart(log);
		// 라인 리스트 조회 요청(SELECT)
		List<LocationInfoDTO> locationInfoList = searchService.getLocationInfoList(locationInfoSearchDTO);
		model.addAttribute("locationInfoList", locationInfoList);
		
//		//페이징용 totalCount
		locationInfoSearchDTO.getPageDTO().setTotalCount(searchService.getLocationInfoCountForPaging(locationInfoSearchDTO));
		model.addAttribute("locationInfoSearchDTO", locationInfoSearchDTO);
		
		return "/common/location_search_form";
	}
	
	// 검색조건에 따른 라인 목록
	@PostMapping("/location/search-popup")
	@ResponseBody
	public List<LocationInfoDTO> searchRecipeList(Model model, @ModelAttribute LocationInfoSearchDTO locationInfoSearchDTO) {
		LogUtil.logStart(log);
		
		//페이징용 totalCount
		locationInfoSearchDTO.getPageDTO().setTotalCount(searchService.getLocationInfoCountForPaging(locationInfoSearchDTO));
		model.addAttribute("locationInfoSearchDTO", locationInfoSearchDTO);
		
		return searchService.getLocationInfoList(locationInfoSearchDTO);
	}
	
	/**
	 * 작업자 스킬 리스트 팝업
	 * @param model
	 * @param searchDTO
	 * @return
	 */
	@GetMapping(value = {"/workermetric/search-popup", "/workermetric/search-popup/"})
	public String getWorkerMetricList(Model model, @ModelAttribute WorkerMetricsSearchDTO searchDTO) {
		LogUtil.logStart(log);
		
		List<WorkerMetricsDTO> infoList = getWorkerMetricListInternal(searchDTO);
		
		model.addAttribute("searchDTO", searchDTO);
		model.addAttribute("infoList", infoList);
		return "/common/worker_search_form";
	}
	
	/**
	 * 작업자 스킬 조건 검색 JSON응답
	 * @param searchDTO
	 * @return
	 */
	@PostMapping(value = {"/workermetric/search-popup", "/workermetric/search-popup/"})
	@ResponseBody
	public SearchResponse<WorkerMetricsDTO> getWorkerMetricListBySearchDTO(@ModelAttribute WorkerMetricsSearchDTO searchDTO) {
		LogUtil.logStart(log);
		
		// 응답 객체로 변환하여 반환
		List<WorkerMetricsDTO> infoList = getWorkerMetricListInternal(searchDTO);
		
		return new SearchResponse<>(infoList, searchDTO.getPageDTO());
	}
	
	private List<WorkerMetricsDTO> getWorkerMetricListInternal(WorkerMetricsSearchDTO searchDTO) {
		int totalCount = searchService.getWorkerMetricCountForPaging(searchDTO);
		searchDTO.getPageDTO().setTotalCount(totalCount);
		
		return totalCount > 0 ? searchService.getWorkerMetricList(searchDTO) : List.of();
	}
	
	// 공통 응답 레코드 정의
	public record SearchResponse<T>(
		List<T> list,
		PageDTO pageInfo
	) {}
	
	// ==================================================================================
	// 자재 조회 팝업창으로 이동
	@GetMapping(value = { "/material/search-popup", "/material/search-popup/" })
	public String getMaterialList(Model model, @ModelAttribute RawMaterialSearchDTO materialSearchDTO) {
		LogUtil.logStart(log);
		// 자재 리스트 조회 요청(SELECT)
		List<RawMaterialDTO> materialList = searchService.getMaterialList(materialSearchDTO);
		model.addAttribute("materialList", materialList);
		
//		//페이징용 totalCount
		materialSearchDTO.getPageDTO().setTotalCount(searchService.getMaterialCountForPaging(materialSearchDTO));
		model.addAttribute("materialSearchDTO", materialSearchDTO);
		
		return "/common/material_search_form";
	}
	
	// 검색조건에 따른 자재 목록
	@PostMapping("/material/search-popup")
	@ResponseBody
	public List<RawMaterialDTO> searchMaterialList(Model model, @ModelAttribute RawMaterialSearchDTO materialSearchDTO) {
		LogUtil.logStart(log);
		
		//페이징용 totalCount
		materialSearchDTO.getPageDTO().setTotalCount(searchService.getMaterialCountForPaging(materialSearchDTO));
		model.addAttribute("materialSearchDTO", materialSearchDTO);
		
		return searchService.getMaterialList(materialSearchDTO);
	}

	// ==================================================================================
	// 주문할 자재 조회 팝업창으로 이동
	@GetMapping(value = { "/material/order/search-popup", "/material/order/search-popup/" })
	public String getMaterialOrderList(Model model, @ModelAttribute RawMaterialSearchDTO materialSearchDTO) {
		LogUtil.logStart(log);
		// 자재 리스트 조회 요청(SELECT)
		List<RawMaterialDTO> materialList = searchService.getMaterialOrderList(materialSearchDTO);
		model.addAttribute("materialList", materialList);
		
//		//페이징용 totalCount
		materialSearchDTO.getPageDTO().setTotalCount(searchService.getMaterialOrderCountForPaging(materialSearchDTO));
		model.addAttribute("materialSearchDTO", materialSearchDTO);
		
		return "/common/material_order_search_form";
	}
	
	// 검색조건에 따른 자재 목록
	@PostMapping("/material/order/search-popup")
	@ResponseBody
	public List<RawMaterialDTO> searchMaterialOrderList(Model model, @ModelAttribute RawMaterialSearchDTO materialSearchDTO) {
		LogUtil.logStart(log);
		
		//페이징용 totalCount
		materialSearchDTO.getPageDTO().setTotalCount(searchService.getMaterialOrderCountForPaging(materialSearchDTO));
		model.addAttribute("materialSearchDTO", materialSearchDTO);
		
		return searchService.getMaterialOrderList(materialSearchDTO);
	}
	
	// ==================================================================================
	// 자재 재고 조회 팝업창으로 이동
	@GetMapping(value = { "/material/stock/search-popup", "/material/stock/search-popup/" })
	public String getMaterialStockList(Model model, @ModelAttribute RawMaterialStockSearchDTO materialStockSearchDTO) {
		LogUtil.logStart(log);
		// 자재 리스트 조회 요청(SELECT)
		List<RawMaterialStockDTO> materialStockList = searchService.getMaterialStockList(materialStockSearchDTO);
		model.addAttribute("materialStockList", materialStockList);
		
		// 검색창에 띄울 상품 리스트 조회
		List<ProductSearchDTO> productList = searchService.getProductList2();
		model.addAttribute("productList", productList);
		
//		//페이징용 totalCount
		materialStockSearchDTO.getPageDTO().setTotalCount(searchService.getMaterialStockCountForPaging(materialStockSearchDTO));
		model.addAttribute("materialStockSearchDTO", materialStockSearchDTO);
		
		return "/common/material_stock_search_form";
	}
	
	// 검색조건에 따른 자재 재고 목록
	@PostMapping("/material/stock/search-popup")
	@ResponseBody
	public List<RawMaterialStockDTO> searchMateriaStockList(Model model, @ModelAttribute RawMaterialStockSearchDTO materialStockSearchDTO) {
		LogUtil.logStart(log);
		
		//페이징용 totalCount
		materialStockSearchDTO.getPageDTO().setTotalCount(searchService.getMaterialStockCountForPaging(materialStockSearchDTO));
		model.addAttribute("materialStockSearchDTO", materialStockSearchDTO);
		
		return searchService.getMaterialStockList(materialStockSearchDTO);
	}
	
	// ==================================================================================
	// 공정 조회 팝업창으로 이동
	@GetMapping(value = { "/recipeProcess/search-popup", "/recipeProcess/search-popup/" })
	public String getRecipeProcessList(Model model, @ModelAttribute RecipeProcessSearchDTO recipeProcessSearchDTO) {
		LogUtil.logStart(log);
		// 공정 리스트 조회 요청(SELECT)
		List<RecipeProcessDTO> recipeProcessList = searchService.getRecipeProcessList(recipeProcessSearchDTO);
		model.addAttribute("recipeProcessList", recipeProcessList);
		
//		//페이징용 totalCount
		recipeProcessSearchDTO.getPageDTO().setTotalCount(searchService.getRecipeProcessCountForPaging(recipeProcessSearchDTO));
		model.addAttribute("recipeProcessSearchDTO", recipeProcessSearchDTO);
		
		return "/common/recipe_process_search_form";
	}
	
	// 검색조건에 따른 공정 목록
	@PostMapping("/recipeProcess/search-popup")
	@ResponseBody
	public List<RecipeProcessDTO> searchRecipeProcessList(Model model, @ModelAttribute RecipeProcessSearchDTO recipeProcessSearchDTO) {
		LogUtil.logStart(log);
		
		//페이징용 totalCount
		recipeProcessSearchDTO.getPageDTO().setTotalCount(searchService.getRecipeProcessCountForPaging(recipeProcessSearchDTO));
		model.addAttribute("recipeProcessSearchDTO", recipeProcessSearchDTO);
		
		return searchService.getRecipeProcessList(recipeProcessSearchDTO);
	}
	
	
}
