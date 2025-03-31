package kr.co.itwillbs.de.info.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;
import kr.co.itwillbs.de.info.dto.DepartmentInfoDTO;
import kr.co.itwillbs.de.info.dto.DepartmentInfoSearchDTO;
import kr.co.itwillbs.de.info.service.DepartmentInfoService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/department") // 부서 관련 URL
public class DepartmentInfoController {

    @Autowired
    private DepartmentInfoService departmentInfoService;

    // 부서 등록 폼 페이지
    @GetMapping("/new")
    public String departmentRegisterForm(Model model) {
        log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
        model.addAttribute("departmentInfoDTO", new DepartmentInfoDTO()); // 빈 DTO 객체 전달
        return "info/department/form"; // form.html 뷰 페이지로 이동
    }

	// 부서 등록 요청
    @PostMapping("")
    public String registerDepartment(@ModelAttribute("departmentInfoDTO") @Valid DepartmentInfoDTO departmentInfoDTO, BindingResult bindingResult) {
        log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
        if (bindingResult.hasErrors()) {
            return "info/department/form"; // 오류 발생 시 form.html 페이지로 이동
        }
        departmentInfoService.registerDepartment(departmentInfoDTO); // 부서 등록
        return "redirect:/department"; // 부서 목록 페이지로 리다이렉트
    }

    // 부서 목록 조회
    @GetMapping("")
    public String getDepartmentList(Model model) {
        log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
        List<DepartmentInfoDTO> departmentList = departmentInfoService.getDepartmentList();
        model.addAttribute("departmentInfoDTOList", departmentList); // 부서 목록 추가
        DepartmentInfoSearchDTO departmentSearchDto = new DepartmentInfoSearchDTO();
        model.addAttribute("departmentSearchDto", departmentSearchDto); // 검색 필터 DTO 추가
        return "info/department/list"; // list.html 뷰 페이지로 이동
    }

    // 부서 검색 기능
    @GetMapping("/search")
    public String searchDepartments(@ModelAttribute DepartmentInfoSearchDTO departmentSearchDto, Model model) {
        log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
        List<DepartmentInfoDTO> departmentList = departmentInfoService.searchDepartments(departmentSearchDto);
        model.addAttribute("departmentInfoDTOList", departmentList); // 검색된 부서 목록 추가
        model.addAttribute("departmentSearchDto", departmentSearchDto); // 검색 DTO 추가
        return "info/department/list"; // list.html 뷰 페이지로 이동
    }

    // 단일 부서 조회 (idx로 조회)
    @GetMapping("/{idx}")
    public String getDepartment(@PathVariable("idx") Long idx, Model model) {
        log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
        DepartmentInfoDTO departmentInfoDTO = departmentInfoService.getDepartmentByIdx(idx); // idx로 조회
        model.addAttribute("departmentInfoDTO", departmentInfoDTO); // 부서 상세 정보 추가
        return "info/department/detail"; // detail.html 뷰 페이지로 이동
    }

    // 부서 수정 요청 (디테일 페이지에서 처리)
    @PutMapping("/{idx}")
    public String updateDepartment(@PathVariable("idx") Long idx,
                                   @ModelAttribute("departmentInfoDTO") DepartmentInfoDTO departmentInfoDTO,
                                   BindingResult bindingResult) {
        log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
        if (bindingResult.hasErrors()) {
            return "info/department/detail"; // 오류 발생 시 detail.html 페이지로 이동
        }
        departmentInfoService.updateDepartment(departmentInfoDTO); // 부서 수정
        return "redirect:/department/" + idx; // 수정된 부서의 상세 페이지로 리다이렉트
    }

    // 부서 삭제 요청 (idx로 삭제, AJAX)
    @DeleteMapping("/{idx}")
    @ResponseBody
    public String deleteDepartment(@PathVariable("idx") Long idx) {
        try {
            departmentInfoService.deleteDepartment(idx); // idx로 부서 삭제
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage(); // 오류 메시지 반환
        }
        return "success"; // 성공 메시지 반환
    }
}

