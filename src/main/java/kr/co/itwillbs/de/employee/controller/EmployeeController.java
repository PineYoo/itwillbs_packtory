package kr.co.itwillbs.de.employee.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.itwillbs.de.employee.dto.EmployeeDTO;
import kr.co.itwillbs.de.employee.dto.EmployeeDetailDTO;
import kr.co.itwillbs.de.employee.dto.EmployeeSearchDTO;
import kr.co.itwillbs.de.employee.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // 사원 목록 조회
    @GetMapping
    public String showEmployeeList(@ModelAttribute EmployeeSearchDTO searchDTO, Model model) {
        log.info("showEmployeeList --- start");

        List<EmployeeDTO> employeeList;
        if (searchDTO.getType() == null || searchDTO.getSearchValue() == null || searchDTO.getSearchValue().isEmpty()) {
            employeeList = employeeService.getEmployeeList();
        } else {
            employeeList = employeeService.searchEmployees(searchDTO);
        }

        if (employeeList == null) {
            employeeList = new ArrayList<>();
        }

        model.addAttribute("employeeList", employeeList);
        model.addAttribute("searchDTO", searchDTO);

        return "employee/list";
    }

    // 사원 등록 페이지 (폼 페이지)
    @GetMapping("/new")
    public String showEmployeeForm(Model model) {
        log.info("showEmployeeForm --- start");
        model.addAttribute("employee", new EmployeeDTO());
        return "employee/form";
    }

    // 사원 등록 처리
    @PostMapping("/new")
    public String registerEmployee(EmployeeDTO employeeDTO) {
        log.info("registerEmployee --- start");

        if (employeeDTO.getId() == null || employeeDTO.getId().isEmpty()) {
            employeeDTO.setId(generateEmployeeId());
        }

        if (employeeDTO.getHireDate() == null) {
            employeeDTO.setHireDate(LocalDateTime.now());
        }

        employeeService.registerEmployee(employeeDTO);
        return "redirect:/employee";
    }

    // 사원 기본 정보 수정 (리스트 페이지에서 수정)
    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<String> updateEmployee(@PathVariable("id") String id, @RequestBody EmployeeDTO employeeDTO) {
        log.info("updateEmployee --- start for id: {}", id);
        
        // 받은 id로 EmployeeDTO에 값 설정
        employeeDTO.setId(id);
        
        try {
            // 서비스에서 사원 정보 수정
            employeeService.updateEmployee(employeeDTO); // t_employee 테이블 수정
            return ResponseEntity.ok("사원 정보가 수정되었습니다.");
        } catch (Exception e) {
            log.error("수정 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("수정 중 오류가 발생했습니다.");
        }
    }

    // 사원 삭제 처리 (리스트 페이지에서만 삭제 가능)
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") String id) {
        log.info("deleteEmployee --- start for id: {}", id);
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok("삭제되었습니다.");
    }

    // 사원 상세정보 페이지
    @GetMapping("/detail/{id}")
    public String showEmployeeDetail(@PathVariable("id") String id, Model model) {
        log.info("showEmployeeDetail --- start for id: {}", id);
        EmployeeDetailDTO employeeDetailDTO = employeeService.getEmployeeDetail(id);
        model.addAttribute("employeeDetailDTO", employeeDetailDTO);
        return "employee/list_detail";
    }

    // 사원 상세 정보 수정 (list_detail 페이지에서 수정)
    @PostMapping("/detail/{id}")
    public String updateEmployeeDetail(@PathVariable("id") String id, @ModelAttribute EmployeeDetailDTO employeeDetailDTO) {
        log.info("updateEmployeeDetail --- start for id: {}", id);
        employeeDetailDTO.setId(id);
        employeeService.updateEmployeeDetail(employeeDetailDTO); // t_employee_detail 테이블 수정
        return "redirect:/employee/detail/" + id;
    }

    // 자동 생성 ID 메서드
    private String generateEmployeeId() {
        log.info("generateEmployeeId --- start");
        return "EMP" + UUID.randomUUID().toString().substring(0, 8);
    }
}
