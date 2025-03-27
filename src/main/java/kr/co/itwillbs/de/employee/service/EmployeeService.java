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

    // ✅ 사원 검색 기능
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

        // 사원 상세 정보 저장 (필요할 경우)
        if (employeeDTO.getEmployeeDetailDTO() != null) {
            EmployeeDetail employeeDetail = employeeDTO.getEmployeeDetailDTO().toEntity();
            employeeDetail.setEmployee(employee);
            employeeDetailRepository.save(employeeDetail);
        }
    }

    // ✅ 사원 기본 정보 수정 (리스트 페이지에서 수정)
    @Transactional
    public void updateEmployee(EmployeeDTO employeeDTO) {
        // 기존 사원 정보 조회 (사원번호로 찾기)
        Employee employee = employeeRepository.findById(employeeDTO.getId())
                .orElseThrow(() -> new RuntimeException("사원 정보를 찾을 수 없습니다: " + employeeDTO.getId()));

        // 엔티티의 update 메서드 호출하여 사원 정보 수정
        employee.updateEmployee(employeeDTO);

        // 명시적으로 save 호출하여 변경된 내용 저장
        // 변경된 내용이 엔티티에 반영되었고, JPA가 더티체킹을 통해 자동으로 DB에 반영하긴 하지만, save() 호출을 통해 명확하게 처리
        employeeRepository.save(employee);
    }

    // ✅ 사원 삭제 처리
    @Transactional
    public void deleteEmployee(String id) {
    	employeeDetailRepository.deleteById(id); // 상세정보 삭제
    	employeeRepository.deleteById(id); // 기본정보 삭제
    }

    // ✅ 사원 상세정보 조회 (사원번호(id)로 조회)
    public EmployeeDetailDTO getEmployeeDetail(String id) {
        // 사원 기본 정보 조회
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사원 정보를 찾을 수 없습니다: " + id));

        // 사원 상세 정보 조회 (없으면 빈 객체 반환)
        EmployeeDetail employeeDetail = employeeDetailRepository.findByEmployeeId(id)
                .orElse(new EmployeeDetail());

        return new EmployeeDetailDTO(employee.getId(), employeeDetail);
    }

    // ✅ 사원 상세 정보 수정 (list_detail 페이지에서 수정)
    @Transactional
    public void updateEmployeeDetail(EmployeeDetailDTO employeeDetailDTO) {
        // 기존 상세 정보를 조회 (없으면 새로 생성)
        EmployeeDetail employeeDetail = employeeDetailRepository.findByEmployeeId(employeeDetailDTO.getId())
                .orElse(new EmployeeDetail());

        // DTO에서 받은 값으로 기존 데이터 수정
        employeeDetail.changeEmployeeDetail(employeeDetailDTO);
        employeeDetailRepository.save(employeeDetail);
    }

    // ✅ 자동 생성 ID 메서드
    private String generateEmployeeId() {
        return "EMP" + UUID.randomUUID().toString().substring(0, 8);
    }
}
