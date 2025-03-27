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

        // 사원 엔티티 저장
        Employee employee = employeeDTO.toEntity();
        employeeRepository.save(employee);

        // 사원 상세 정보가 있다면 별도로 저장
        if (employeeDTO.getEmployeeDetailDTO() != null) {
            EmployeeDetail employeeDetail = employeeDTO.getEmployeeDetailDTO().toEntity();
            employeeDetail.setEmployee(employee);  // 연관된 사원 엔티티 설정
            employeeDetailRepository.save(employeeDetail);
        }
    }
    
    // ✅ 사원 상세정보 조회 (사원번호(id)로 조회)
    public EmployeeDetailDTO getEmployeeDetail(String id) {
        // 사원 조회 (사원번호로 조회)
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        // 사원 상세 조회 (사원번호로 상세정보 조회, 없으면 빈 객체 반환)
        EmployeeDetail employeeDetail = employeeDetailRepository.findByEmployeeId(id)
                .orElse(new EmployeeDetail());  // 디테일 정보가 없다면 빈 객체로 처리

        // EmployeeDetailDTO로 변환
        return new EmployeeDetailDTO(employee.getId(), employeeDetail); // 수정된 생성자 호출
    }


    // ✅ 사원 수정 처리
    @Transactional
    public void updateEmployeeDetail(EmployeeDetailDTO employeeDetailDTO) {
        // 사원번호로 기존 상세 정보를 조회
        EmployeeDetail employeeDetail = employeeDetailRepository.findByEmployeeId(employeeDetailDTO.getId())
                .orElseThrow(() -> new RuntimeException("EmployeeDetail not found with employeeId: " + employeeDetailDTO.getId()));

        // DTO에서 받은 값으로 수정
        employeeDetail.changeEmployeeDetail(employeeDetailDTO);  // EmployeeDetail에 맞는 수정 메서드 호출
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
