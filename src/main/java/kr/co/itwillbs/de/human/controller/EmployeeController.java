package kr.co.itwillbs.de.human.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.itwillbs.de.common.service.CommonService;
import kr.co.itwillbs.de.common.service.CustomUserDetails;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.common.vo.LoginVO;
import kr.co.itwillbs.de.human.dto.DepartmentCodeDTO;
import kr.co.itwillbs.de.human.dto.EmployeeDTO;
import kr.co.itwillbs.de.human.dto.EmployeeDetailDTO;
import kr.co.itwillbs.de.human.dto.EmployeeSearchDTO;
import kr.co.itwillbs.de.human.service.DepartmentInfoService;
import kr.co.itwillbs.de.human.service.EmployeeService;
import kr.co.itwillbs.de.human.service.PositionInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/human/employee")
public class EmployeeController {

	private final EmployeeService employeeService;
	private final DepartmentInfoService departmentInfoService;
	private final PositionInfoService positionInfoService;

	// 공통코드 주입
	private final CommonCodeUtil commonCodeUtil;
	private final CommonService commonService;

	// 사원 등록 폼
	@GetMapping("/new")
	public String employeeRegisterForm(Model model) {
		log.info("employeeRegisterForm --- 시작");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			LoginVO loginVO = userDetails.getLoginVO();
			String memberId = userDetails.getUsername(); // 이렇게 자유롭게 사용 가능!
			log.info("userDetails is {}", userDetails);
			log.info("loginVO is {}", loginVO);
			model.addAttribute("userDetails", userDetails);
			model.addAttribute("loginVO", loginVO);
		}

		EmployeeDTO employeeDTO = EmployeeDTO.builder().departmentCode("").build();
		model.addAttribute("employeeDTO", employeeDTO);

		model.addAttribute("validDepartments", commonCodeUtil.getCodeItems("DEPARTMENT_CODE"));
		model.addAttribute("SubDepartments", Collections.emptyList());
		model.addAttribute("validPositions", positionInfoService.getValidPositions());

		return "human/employee/form";
	}

	// 사원 등록 처리
	@PostMapping("/new")
	public String register(@ModelAttribute EmployeeDTO employeeDTO) {
		log.info("register --- 사원 등록 시작");

		// 이 시점에 사번 생성
		employeeDTO.setId(commonService.createSeqEmpIdfromMysql());

		employeeService.registerEmployee(employeeDTO);

		log.info("register --- 사원 등록 완료: {}", employeeDTO.getId());

		return "redirect:/human/employee";
	}

	// 하위 부서 코드 조회
	@GetMapping("/subDepartments")
	@ResponseBody
	public List<DepartmentCodeDTO> getSubDepartments(@RequestParam("departmentCode") String departmentCode) {
		log.info("하위 부서 코드 조회 요청: {}", departmentCode);

		List<DepartmentCodeDTO> result = departmentInfoService.getSubDepartmentCodes(departmentCode);
		if (result == null) {
			log.warn("하위 부서 결과가 null입니다.");
			return Collections.emptyList();
		}

		log.info("하위 부서 개수: {}", result.size());
		return result;
	}

	// 사원 목록 조회
	@GetMapping("")
	public String getEmployeeList(@ModelAttribute EmployeeSearchDTO searchDTO, Model model) {
		log.info("getEmployeeList --- 시작");

		// 사원 목록 조회
		List<EmployeeDTO> employeeList = employeeService.getEmployeeList(searchDTO);
		model.addAttribute("employeeList", employeeList);
		searchDTO.getPageDTO().setTotalCount(employeeService.searchEmployeesCount(searchDTO));
		model.addAttribute("searchDTO", searchDTO); // 검색조건 유지용

		// 부서 코드 (공통코드)
		model.addAttribute("validDepartments", commonCodeUtil.getCodeItems("DEPARTMENT_CODE"));
		model.addAttribute("validPositions", positionInfoService.getValidPositions());

		// 하위 부서 코드 (선택된 부서 기준)
		if (searchDTO.getDepartmentCode() != null && !searchDTO.getDepartmentCode().isEmpty()) {
			List<DepartmentCodeDTO> subDepartments = departmentInfoService
					.getSubDepartmentCodes(searchDTO.getDepartmentCode());
			model.addAttribute("subDepartments", subDepartments);
		} else {
			model.addAttribute("subDepartments", Collections.emptyList());
		}

		return "human/employee/list";
	}

	// 사원 상세 조회
	@GetMapping("/{id}")
	public String getEmployeeDetail(@PathVariable("id") String id, Model model) {
		log.info("getEmployeeDetail --- 시작, id: {}", id);

		// 사원 기본정보 + 상세정보 조회
		EmployeeDTO employeeDTO = employeeService.getEmployeeByIdWithDetail(id);
		model.addAttribute("employeeDTO", employeeDTO);

		// 이메일 분리
		String email = employeeDTO.getDetail() != null ? employeeDTO.getDetail().getEmail() : "";
		if (email != null && email.contains("@")) {
			String[] split = email.split("@", 2);
			employeeDTO.setEmailId(split[0]);
			employeeDTO.setEmailDomain(split[1]);
		} else {
			employeeDTO.setEmailId("");
			employeeDTO.setEmailDomain("");
		}

		// 대표부서, 하위부서, 직급 조회
		model.addAttribute("validDepartments", commonCodeUtil.getCodeItems("DEPARTMENT_CODE"));
		model.addAttribute("SubDepartments", Collections.emptyList()); // JS로 동적 처리
		model.addAttribute("validPositions", positionInfoService.getValidPositions());

		return "human/employee/detail";
	}

	// 사원 수정 처리
	@PostMapping("/{id}")
	public String updateEmployee(@PathVariable("id") String id, @ModelAttribute EmployeeDTO employeeDTO,
			@ModelAttribute EmployeeDetailDTO employeeDetailDTO) {
		log.info("updateEmployee --- 시작, id: {}", id);

		// 전달된 idx 값을 employeeDTO에 설정
		employeeDTO.setId(id);

		// EmployeeDTO에 세부사항(detail) 필드 세팅
		if (employeeDetailDTO != null) {
			employeeDTO.setDetail(employeeDetailDTO);
		}

		// 업데이트 처리 전 로그
		log.info("Updating EmployeeDTO: {}", employeeDTO);

		// 사원 정보와 상세 정보를 업데이트
		employeeService.updateEmployeeWithDetail(employeeDTO);

		return "redirect:/human/employee/" + id;
	}

	// 사원 삭제 처리
	@PostMapping("/delete/{id}")
	public String deleteEmployee(@PathVariable("id") String id) {
		log.info("deleteEmployee --- 시작, id: {}", id);
		employeeService.deleteEmployee(id);
		return "redirect:/human/employee";
	}

}