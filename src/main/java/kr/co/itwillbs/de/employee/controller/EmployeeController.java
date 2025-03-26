package kr.co.itwillbs.de.employee.controller;

import kr.co.itwillbs.de.employee.dto.EmployeeDTO;
import kr.co.itwillbs.de.employee.dto.EmployeeDetailDTO;
import kr.co.itwillbs.de.employee.dto.EmployeeSearchDTO;
import kr.co.itwillbs.de.employee.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public String showEmployeeList(@ModelAttribute EmployeeSearchDTO searchDTO, Model model) {
        log.info("{} --- start", Thread.currentThread().getStackTrace()[1].getMethodName());

        // 검색 조건이 없으면 전체 목록 조회, 있으면 검색 결과 조회
        List<EmployeeDTO> employeeList;
        if (searchDTO.getType() == null || searchDTO.getSearchValue() == null || searchDTO.getSearchValue().isEmpty()) {
            employeeList = employeeService.getEmployeeList();
        } else {
            employeeList = employeeService.searchEmployees(searchDTO);
        }

        // null 방지
        if (employeeList == null) {
            employeeList = new ArrayList<>();
        }

        // 모델에 데이터 추가
        model.addAttribute("employeeList", employeeList);
        model.addAttribute("searchDTO", searchDTO); // 검색 값을 유지

        // 사원 목록 페이지 반환
        return "employee/list";
    }

    // 사원 등록 페이지 (폼 페이지)
    @GetMapping("/new")
    public String showEmployeeForm(Model model) {
        log.info("{} --- start", Thread.currentThread().getStackTrace()[1].getMethodName());
        model.addAttribute("employee", new EmployeeDTO());  // 빈 DTO 객체를 넘겨서 폼에서 입력 가능하게 처리
        return "employee/form";  // Thymeleaf를 사용하여 등록 폼으로 이동
    }

    // 사원 등록 처리
    @PostMapping("/new")
    public String registerEmployee(EmployeeDTO employeeDTO) {
        log.info("{} --- start", Thread.currentThread().getStackTrace()[1].getMethodName());

        // 자동으로 ID 생성
        if (employeeDTO.getId() == null || employeeDTO.getId().isEmpty()) {
            employeeDTO.setId(generateEmployeeId()); // ID가 비어있다면 자동으로 생성
        }

        // hireDate가 null인 경우 기본값으로 설정
        if (employeeDTO.getHireDate() == null) {
            employeeDTO.setHireDate(LocalDateTime.now());  // 입사일 기본값 설정
        }

        // 사원 등록 처리
        employeeService.registerEmployee(employeeDTO);  // 등록된 사원 정보를 서비스에서 처리
        return "redirect:/employee";  // 등록 후 사원 목록 페이지로 리다이렉트
    }

    // 사원 상세정보 페이지
    @GetMapping("/detail/{id}")
    public String showEmployeeDetail(@PathVariable("id") String id, Model model) {
        log.info("{} --- start", Thread.currentThread().getStackTrace()[1].getMethodName());
        EmployeeDetailDTO employeeDetailDTO = employeeService.getEmployeeDetail(id);
        model.addAttribute("employeeDetailDTO", employeeDetailDTO);
        return "employee/list_detail";
    }

    // 사원 수정 처리
    @PostMapping("/detail/{id}")
    public String updateEmployeeDetail(@PathVariable("id") String id, @ModelAttribute EmployeeDetailDTO employeeDetailDTO) {
        log.info("{} --- start", Thread.currentThread().getStackTrace()[1].getMethodName());
        employeeDetailDTO.setId(id);
        employeeService.updateEmployeeDetail(employeeDetailDTO);
        return "redirect:/employee/list_detail/" + id;
    }

    // 사원 삭제 처리
    @PostMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable("id") String id) {
        log.info("{} --- start", Thread.currentThread().getStackTrace()[1].getMethodName());
        employeeService.deleteEmployee(id);
        return "redirect:/employee";
    }

    // 자동 생성 ID 메서드
    private String generateEmployeeId() {
        log.info("{} --- start", Thread.currentThread().getStackTrace()[1].getMethodName());
        // UUID를 사용하여 고유한 ID 생성 (예: EMP12345678)
        return "EMP" + UUID.randomUUID().toString().substring(0, 8);
    }
}
