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

import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.common.service.EmployeeSearchService;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.human.dto.EmployeeCodeDTO;
import kr.co.itwillbs.de.human.dto.EmployeeDTO;
import kr.co.itwillbs.de.human.dto.EmployeeSearchDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class EmployeeSearchController {
	
	@Autowired
	private EmployeeSearchService employeeSearchService;
	@Autowired
	private CommonCodeUtil commonCodeUtil;

	// 사원 검색 팝업창으로 이동
	@GetMapping(value = { "/employee/search-popup", "/employee/search-popup/" })
	public String getEmployeeList(Model model, @ModelAttribute EmployeeSearchDTO employeeSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		// 사원 리스트 조회 요청(SELECT)
		List<EmployeeDTO> employeeList = employeeSearchService.getEmployeeList(employeeSearchDTO);
		model.addAttribute("employeeList", employeeList);
		model.addAttribute("employeeSearchDTO", employeeSearchDTO);
		
		//페이징용 totalCount
		employeeSearchDTO.getPageDTO().setTotalCount(employeeSearchService.getEmployeeCountForPaging(employeeSearchDTO));
		
		return "/common/employee_search_form";
	}

	// 검색조건에 따른 사원 목록
	@PostMapping("/employee/search-popup")
	@ResponseBody
	public List<EmployeeDTO> searchEmployeeList(Model model, @ModelAttribute EmployeeSearchDTO employeeSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		System.out.println(">>>>>>>>>>>>>>>>>>" + employeeSearchDTO);

		//페이징용 totalCount
		employeeSearchDTO.getPageDTO().setTotalCount(employeeSearchService.getEmployeeCountForPaging(employeeSearchDTO));
		model.addAttribute("employeeSearchDTO", employeeSearchDTO);
		
	    return employeeSearchService.getEmployeeList(employeeSearchDTO);
	}
	
	// ==================================================================================
	// 부서 리스트 조회
	@GetMapping("/api/getDepartments")
	@ResponseBody
	public List<EmployeeCodeDTO> getDepartmentList() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return employeeSearchService.getDepartmentList(); // major_code 기준
	}
	
	// 하위 부서 리스트 조회
	@GetMapping("/api/getSubDepartments")
	@ResponseBody
	public List<EmployeeCodeDTO> getSubDepartmentList(@RequestParam("departmentCode") String departmentCode) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
	    return employeeSearchService.getSubDepartmentList(departmentCode); // 하위 minor_code
	}

	// 직급 리스트 조회
	@GetMapping("/api/getPositions")
	@ResponseBody
	public List<EmployeeCodeDTO> getPositionList() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return employeeSearchService.getPositionList(); // major_code 기준
	}
	
	// ===================================================================
	/**
	 * 특정 minorName을 가진 minorCode를 반환하는 유틸 메서드
	 * @param codeList
	 * @param targetName(한글명)
	 * @return
	 */
	private String getMinorCodeByMinorName(List<CodeItemDTO> codeList, String targetName) {
		for (CodeItemDTO item : codeList) {
			if (targetName.equals(item.getMinorName())) {
				return item.getMinorCode();
			}
		}
		return null;
	}
}
