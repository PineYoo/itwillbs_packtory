package kr.co.itwillbs.de.human.service;

import kr.co.itwillbs.de.common.service.CommonService;
import kr.co.itwillbs.de.human.dto.EmployeeDTO;
import kr.co.itwillbs.de.human.dto.EmployeeDetailDTO;
import kr.co.itwillbs.de.human.dto.EmployeeSearchDTO;
import kr.co.itwillbs.de.human.entity.Employee;
import kr.co.itwillbs.de.human.entity.EmployeeDetail;
import kr.co.itwillbs.de.human.repository.EmployeeDetailRepository;
import kr.co.itwillbs.de.human.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeDetailRepository employeeDetailRepository;
    
    @Autowired
    private CommonService commonService;

    // 사원 목록 조회 (전체 목록)
    public List<EmployeeDTO> getEmployeeList() {
        return employeeRepository.findAll().stream()
                .map(EmployeeDTO::new)
                .collect(Collectors.toList());
    }

    // 사원 검색 기능
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

    // 사원 등록 처리
    @Transactional
    public void registerEmployee(EmployeeDTO employeeDTO) {
        if (employeeDTO.getId() == null || employeeDTO.getId().isEmpty()) {
            employeeDTO.setId(generateEmployeeId());
        }
        
        if (employeeDTO.getId() == null || employeeDTO.getId().isEmpty()) {
            employeeDTO.setId(generateEmployeeId());
        }

        if (employeeDTO.getHireDate() == null) {
            employeeDTO.setHireDate(LocalDateTime.now());
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

    // 사원 기본 정보 수정 (리스트 페이지에서 수정)
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

    // 사원 삭제 처리
    @Transactional
    public void deleteEmployee(String id) {
    	employeeDetailRepository.deleteById(id); // 상세정보 삭제
    	employeeRepository.deleteById(id); // 기본정보 삭제
    }

    // 사원 상세정보 조회 (사원번호(id)로 조회)
    public EmployeeDetailDTO getEmployeeDetail(String id) {
        // 사원 기본 정보 조회
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사원 정보를 찾을 수 없습니다: " + id));

        // 사원 상세 정보 조회 (없으면 빈 객체 반환)
        EmployeeDetail employeeDetail = employeeDetailRepository.findByEmployeeId(id)
                .orElse(new EmployeeDetail());

        // Employee 객체도 전달하여 EmployeeDetailDTO 생성
        return new EmployeeDetailDTO(employee.getId(), employeeDetail, employee);  // Employee 객체도 전달
    }
    
    // 사원 상세정보 업데이트
    public void updateEmployeeDetailAndSsn(EmployeeDTO employeeDTO, EmployeeDetailDTO employeeDetailDTO) {
        log.info("Updating employee details for ID: {}", employeeDTO.getId());

        // 1. 사원 기본 정보에서 주민번호를 업데이트 (t_employee 테이블)
        Employee employee = employeeRepository.findById(employeeDTO.getId())
                .orElseThrow(() -> {
                    log.error("Employee not found with ID: {}", employeeDTO.getId());
                    return new RuntimeException("사원 정보를 찾을 수 없습니다: " + employeeDTO.getId());
                });
        
        // 주민번호만 업데이트
        if (employeeDTO.getSsn() != null) {
            employee.setSsn(employeeDTO.getSsn());
        }

        // 2. t_employee 테이블에 변경 사항을 저장 (주민번호)
        employeeRepository.save(employee);

        // 3. 사원 상세정보 업데이트 (t_employee_detail 테이블)
        EmployeeDetail employeeDetail = employeeDetailRepository.findByEmployeeId(employeeDetailDTO.getId())
                .orElseThrow(() -> {
                    log.error("EmployeeDetail not found with ID: {}", employeeDetailDTO.getId());
                    return new RuntimeException("사원 상세 정보를 찾을 수 없습니다: " + employeeDetailDTO.getId());
                });

        // 상세 정보 수정
        employeeDetail.changeEmployeeDetail(employeeDetailDTO);

        // t_employee_detail 테이블에 저장
        employeeDetailRepository.save(employeeDetail);
    }

    // ✅ 자동 생성 ID 메서드
    private String generateEmployeeId() {
        return commonService.createSeqEmpIdfromMysql();
        
    }
}
