package kr.co.itwillbs.de.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.common.mapper.EmployeeSearchMapper;
import kr.co.itwillbs.de.human.dto.EmployeeCodeDTO;
import kr.co.itwillbs.de.human.dto.EmployeeDTO;
import kr.co.itwillbs.de.human.dto.EmployeeSearchDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeSearchService {
	@Autowired
	private EmployeeSearchMapper employeeSearchMapper;
	
	/**
	 *  리스트 조회
	 * @param employeeSearchDTO
	 * @return List<EmployeeDTO>
	 */
	public List<EmployeeDTO> getEmployeeList(EmployeeSearchDTO employeeSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return employeeSearchMapper.getEmployeeList(employeeSearchDTO);
	}
	
	/**
	 * 검색조건에 따른 사원 카운트 가져오기 페이징용
	 * @param employeeSearchDTO
	 * @return
	 */
	public int getEmployeeCountForPaging(EmployeeSearchDTO employeeSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return employeeSearchMapper.getEmployeeCountForPaging(employeeSearchDTO);
	}

	/**
	 * 부서 리스트 조회
	 * @return List<EmployeeCodeDTO>
	 */
	public List<EmployeeCodeDTO> getDepartmentList() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return employeeSearchMapper.getDepartmentList();
	}

	/**
	 * 하위 부서 리스트 조회
	 * @return List<EmployeeCodeDTO>
	 */
	public List<EmployeeCodeDTO> getSubDepartmentList(String departmentCode) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return employeeSearchMapper.getSubDepartmentList(departmentCode);
	}

	/**
	 * 직급 리스트 조회
	 * @return List<EmployeeCodeDTO>
	 */
	public List<EmployeeCodeDTO> getPositionList() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return employeeSearchMapper.getPositionList();
	}

}
