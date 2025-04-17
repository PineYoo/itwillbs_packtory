package kr.co.itwillbs.de.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.common.mapper.SearchMapper;
import kr.co.itwillbs.de.human.dto.EmployeeCodeDTO;
import kr.co.itwillbs.de.human.dto.EmployeeDTO;
import kr.co.itwillbs.de.human.dto.EmployeeSearchDTO;
import kr.co.itwillbs.de.mes.dto.ProductDTO;
import kr.co.itwillbs.de.mes.dto.ProductSearchDTO;
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
}
