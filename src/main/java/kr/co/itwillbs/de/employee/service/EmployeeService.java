package kr.co.itwillbs.de.employee.service;

import kr.co.itwillbs.de.employee.dto.EmployeeDTO;
import kr.co.itwillbs.de.employee.dto.EmployeeDetailDTO;
import kr.co.itwillbs.de.employee.dto.EmployeeSearchDTO;
import kr.co.itwillbs.de.employee.entity.Employee;
import kr.co.itwillbs.de.employee.entity.EmployeeDetail;
import kr.co.itwillbs.de.employee.repository.EmployeeDetailRepository;
import kr.co.itwillbs.de.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeDetailRepository employeeDetailRepository;

    // ✅ 사원 목록 조회 (전체 목록)
    public List<EmployeeDTO> getEmployeeList() {
        return employeeRepository.findAll().stream()
                .map(EmployeeDTO::new)
                .collect(Collectors.toList());
    }

    // ✅ 사원 검색 기능 추가
    public List<EmployeeDTO> searchEmployees(EmployeeSearchDTO searchDTO) {
        List<Employee> employees;

        if ("name".equals(searchDTO.getType())) {
            employees = employeeRepository.findByNameContaining(searchDTO.getSearchValue());
        } else if ("dept".equals(searchDTO.getType())) {
            employees = employeeRepository.findByDepartmentCodeContaining(searchDTO.getSearchValue());
        } else {
            employees = employeeRepository.findAll();
        }

        return employees.stream()
                .map(EmployeeDTO::new)
                .collect(Collectors.toList());
    }

    // ✅ 사원 등록 처리
    @Transactional
    public void registerEmployee(EmployeeDTO employeeDTO) {
        if (employeeDTO.getId() == null || employeeDTO.getId().isEmpty()) {
            employeeDTO.setId(generateEmployeeId());
        }

        Employee employee = employeeDTO.toEntity();
        employeeRepository.save(employee);

        // T_EMPLOYEE_DETAIL에도 별도의 추가 정보가 있다면 저장
        if (employeeDTO.getEmployeeDetailDTO() != null) {
            EmployeeDetail employeeDetail = employeeDTO.getEmployeeDetailDTO().toEntity();
            employeeDetail.setEmployee(employee);
            employeeDetailRepository.save(employeeDetail);
        }
    }

    // ✅ 사원 상세정보 조회 (사원번호(id)만 반환)
    public EmployeeDetailDTO getEmployeeDetail(String id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        // T_EMPLOYEE_DETAIL은 조회하지 않고, 기본 정보에서 사원번호(id)만 설정
        EmployeeDetailDTO detailDTO = new EmployeeDetailDTO();
        detailDTO.setId(employee.getId());
        return detailDTO;
    }

    // ✅ 사원 수정 처리 (T_EMPLOYEE_DETAIL이 있을 경우에만 수정)
    @Transactional
    public void updateEmployeeDetail(EmployeeDetailDTO employeeDetailDTO) {
        // 여기서는 사원번호만 표시하므로, 수정할 정보가 있다면 필요한 필드만 업데이트
        // 기본적으로 T_EMPLOYEE_DETAIL이 없다면 예외를 발생시킬 수도 있음
        EmployeeDetail employeeDetail = employeeDetailRepository.findByEmployeeId(employeeDetailDTO.getId())
                .orElseThrow(() -> new RuntimeException("EmployeeDetail not found with employeeId: " + employeeDetailDTO.getId()));

        employeeDetail.changeEmployeeDetail(employeeDetailDTO);
        employeeDetailRepository.save(employeeDetail);
    }

    // ✅ 사원 삭제 처리
    @Transactional
    public void deleteEmployee(String id) {
        employeeDetailRepository.deleteById(id);
        employeeRepository.deleteById(id);
    }

    // ✅ 자동 생성 ID 메서드
    private String generateEmployeeId() {
        return "EMP" + UUID.randomUUID().toString().substring(0, 8);
    }
}
