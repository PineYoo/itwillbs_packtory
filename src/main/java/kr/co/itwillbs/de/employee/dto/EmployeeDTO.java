package kr.co.itwillbs.de.employee.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.co.itwillbs.de.employee.entity.Employee;
import kr.co.itwillbs.de.employee.entity.EmployeeDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EmployeeDTO {

    private Long idx; // 테이블 인덱스

    @NotBlank(message = "사원번호는 필수 입력 값입니다.")
    private String id; // 사원번호

    @NotBlank(message = "사원명은 필수 입력 값입니다.")
    private String name; // 사원명

    @NotBlank(message = "주민등록번호는 필수 입력 값입니다.")
    private String ssn; // 주민등록번호

    @NotBlank(message = "부서 코드는 필수 입력 값입니다.")
    private String departmentCode; // 부서코드

    @NotBlank(message = "하위 부서 코드는 필수 입력 값입니다.")
    private String subDepartmentCode; // 하위부서코드

    @NotBlank(message = "직급 코드는 필수 입력 값입니다.")
    private String positionCode; // 직급코드

    @NotNull(message = "입사일은 필수 입력 값입니다.")
    private LocalDateTime hireDate = LocalDateTime.now(); // 입사일
    
    private LocalDateTime resignationDate; // 퇴사일

    @Size(max = 50, message = "경력은 50자 이내로 입력해주세요.")
    private String workExperience; // 경력

    private String regId; // 작성자

    private LocalDateTime regDate; // 작성일자시간

    private String modId; // 최종 작성자

    private LocalDateTime modDate; // 최종작성일자

    private EmployeeDetailDTO employeeDetailDTO; // EmployeeDetailDTO 추가
    
    // Employee 객체를 받아서 DTO로 변환하는 생성자 추가
    public EmployeeDTO(Employee employee) {
        this.idx = employee.getIdx();
        this.id = employee.getId();
        this.name = employee.getName();
        this.departmentCode = employee.getDepartmentCode();
        this.subDepartmentCode = employee.getSubDepartmentCode();
        this.positionCode = employee.getPositionCode();
        this.hireDate = employee.getHireDate();
        this.resignationDate = employee.getResignationDate();
        this.workExperience = employee.getWorkExperience();
        this.regId = employee.getRegId();
        this.regDate = employee.getRegDate();
        this.modId = employee.getModId();
        this.modDate = employee.getModDate();
        
        // EmployeeDTO 생성 부분
        if (employee.getEmployeeDetail() != null) {
            this.employeeDetailDTO = new EmployeeDetailDTO(employee.getId(), employee.getEmployeeDetail(), employee);
        }
    }

    @Builder
    public EmployeeDTO(Long idx, String id, String name, String ssn, String departmentCode, String subDepartmentCode,
                       String positionCode, LocalDateTime hireDate, LocalDateTime resignationDate, String workExperience,
                       String regId, LocalDateTime regDate, String modId, LocalDateTime modDate, EmployeeDetailDTO employeeDetailDTO,
                       String employeeStatus) {
        this.idx = idx;
        this.id = id;
        this.name = name;
        this.ssn = ssn;
        this.departmentCode = departmentCode;
        this.subDepartmentCode = subDepartmentCode;
        this.positionCode = positionCode;
        this.hireDate = hireDate;
        this.resignationDate = resignationDate;
        this.workExperience = workExperience;
        this.regId = regId;
        this.regDate = regDate;
        this.modId = modId;
        this.modDate = modDate;
        this.employeeDetailDTO = employeeDetailDTO;
    }

    // EmployeeDTO -> Employee 엔티티 변환 메서드
    public Employee toEntity() {
        // EmployeeDTO -> Employee 변환
        Employee employee = Employee.builder()
                .id(id)
                .name(name)
                .ssn(ssn)
                .departmentCode(departmentCode)
                .subDepartmentCode(subDepartmentCode)
                .positionCode(positionCode)
                .hireDate(hireDate)
                .resignationDate(resignationDate)
                .workExperience(workExperience)
                .regId(regId)
                .regDate(regDate)
                .modId(modId)
                .modDate(modDate)
                .build();

        // EmployeeDetailDTO를 EmployeeDetail로 변환하여 추가
        if (employeeDetailDTO != null) {
            EmployeeDetail employeeDetail = employeeDetailDTO.toEntity();
            employee.setEmployeeDetail(employeeDetail); // Employee에 EmployeeDetail 설정
        }

        return employee;
    }
}
