package kr.co.itwillbs.de.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.human.dto.EmployeeCodeDTO;
import kr.co.itwillbs.de.human.dto.EmployeeDTO;
import kr.co.itwillbs.de.human.dto.EmployeeSearchDTO;
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


}
