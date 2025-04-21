package kr.co.itwillbs.de.human.controller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.human.dto.DepartmentCodeDTO;
import kr.co.itwillbs.de.human.dto.DepartmentInfoDTO;
import kr.co.itwillbs.de.human.dto.DepartmentInfoSearchDTO;
import kr.co.itwillbs.de.human.service.DepartmentInfoService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/human/department") // 부서 관련 URL
public class DepartmentInfoController {

	@Autowired
	private DepartmentInfoService departmentInfoService;
	
	@Autowired
	private CommonCodeUtil commonCodeUtil; // 공통 코드 유틸 사용

	// 부서 등록 폼 페이지
	@GetMapping("/new")
	public String departmentRegisterForm(Model model) {
		log.info("departmentRegisterForm --- start");
		model.addAttribute("departmentInfoDTO", new DepartmentInfoDTO());

		// 공통 코드 유틸을 활용하여 major_code가 "DEPARTMENT_CODE"인 코드 아이템 목록 조회
		List<CodeItemDTO> codeItems = commonCodeUtil.getCodeItems("DEPARTMENT_CODE");
		model.addAttribute("codeItems", codeItems);

		return "human/info/department/form"; // form.html 뷰 페이지로 이동
	}

	// 부서 등록 처리
	@PostMapping("/new")
	public String departmentRegister(@ModelAttribute DepartmentInfoDTO departmentInfoDTO) {
		log.info("departmentRegister --- start");
		departmentInfoService.registerDepartment(departmentInfoDTO); // 부서 등록 처리
		return "redirect:/human/department"; // 등록 후 부서 목록 페이지로 리다이렉트
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

	// 부서 목록 조회 (검색 + 페이징)
	@GetMapping("")
	public String getDepartmentList(@ModelAttribute DepartmentInfoSearchDTO searchDTO, Model model) {
		log.info("getDepartmentList --- start");

		// 검색 조건에 따라 페이징 처리된 부서 목록 조회
		Page<DepartmentInfoDTO> result = departmentInfoService.getDepartmentsBySearchDTO(searchDTO);
		searchDTO.getPageDTO().setTotalCount((int) result.getTotalElements());
		model.addAttribute("departmentInfoDTOList", result.getContent());
		
		// 역순 번호 계산
		int startNo = searchDTO.getPageDTO().getTotalCount() - searchDTO.getPageDTO().getOffset();
		model.addAttribute("startNo", startNo);

		// 공통 코드 리스트 (부서 코드 선택용)
		List<CodeItemDTO> codeItems = commonCodeUtil.getCodeItems("DEPARTMENT_CODE");
		model.addAttribute("codeItems", codeItems);

		// 검색 조건 유지
		model.addAttribute("searchDTO", searchDTO);

		return "human/info/department/list";
	}

	// 단일 부서 조회 (idx로 조회)
	@GetMapping("/detail/{idx}")
	public String getDepartment(@PathVariable("idx") Long idx, Model model) {
		log.info("getDepartment --- start");

		// 부서 정보 조회
		DepartmentInfoDTO departmentInfoDTO = departmentInfoService.getDepartmentByIdx(idx);

		// 코드 아이템 목록 조회 (major_code가 "DEPARTMENT_CODE"인 코드 아이템 목록)
		List<CodeItemDTO> codeItems = commonCodeUtil.getCodeItems("DEPARTMENT_CODE");

		// 모델에 부서 정보와 코드 아이템 목록 추가
		model.addAttribute("departmentInfoDTO", departmentInfoDTO);
		model.addAttribute("codeItems", codeItems);

		return "/human/info/department/detail"; // detail.html 뷰 페이지로 이동
	}

	// 부서 수정 요청 (디테일 페이지에서 처리)
	@PostMapping("/detail/{idx}")
	public String updateDepartment(@PathVariable("idx") Long idx, @ModelAttribute DepartmentInfoDTO departmentInfoDTO) {
		try {
			// 수정할 부서 정보가 존재하는지 확인
			DepartmentInfoDTO existingDepartment = departmentInfoService.getDepartmentByIdx(idx);
			if (existingDepartment == null) {
				throw new IllegalArgumentException("부서가 존재하지 않습니다.");
			}
			if (departmentInfoDTO.getIsDeleted() == null) {
				departmentInfoDTO.setIsDeleted("N"); // 기본값 N 설정
			}

			departmentInfoDTO.setModDate(LocalDateTime.now());
			departmentInfoService.updateDepartment(departmentInfoDTO);
			return "redirect:/human/department/detail/" + idx; // 수정 후 상세 페이지로 리디렉션
		} catch (Exception e) {
			log.error("부서 수정 실패: {}", e.getMessage());
			return "errorPage"; // 에러 페이지로 리디렉션 또는 에러 메시지 표시
		}
	}

	// 부서 삭제 요청 (idx로 삭제, AJAX)
	@DeleteMapping("/detail/{idx}")
	@ResponseBody
	public String deleteDepartment(@PathVariable("idx") Long idx) {
		try {
			departmentInfoService.softDeleteDepartment(idx); // 서비스 호출
			return "success";
		} catch (Exception e) {
			log.error("부서 삭제 실패: {}", e.getMessage());
			return "error";
		}
	}
}
