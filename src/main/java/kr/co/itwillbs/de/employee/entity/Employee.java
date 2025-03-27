package kr.co.itwillbs.de.employee.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import kr.co.itwillbs.de.employee.dto.EmployeeDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_employee")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Employee {
	
	@OneToOne(mappedBy = "employee") // EmployeeDetail과의 1:1 관계 설정
	private EmployeeDetail employeeDetail; // EmployeeDetail 엔티티와 관계 설정
	
	// getEmployeeDetail 메소드 추가
    public EmployeeDetail getEmployeeDetail() {
        return employeeDetail;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idx")
    private Long idx; // 테이블 인덱스

    @Column(length = 50, nullable = false)
    private String id; // 사원번호

    @Column(length = 50, nullable = false)
    private String name; // 이름

    @Column(length = 50, nullable = false)
    private String ssn; // 주민등록번호

    @Column(length = 50, nullable = false)
    private String departmentCode; // 부서코드

    @Column(length = 50, nullable = false)
    private String subDepartmentCode; // 하위부서코드

    @Column(length = 50, nullable = false)
    private String positionCode; // 직급코드

    @Column(nullable = false)
    private LocalDateTime hireDate = LocalDateTime.now(); // 입사일

    @Column(nullable = true)
    private LocalDateTime resignationDate; // 퇴사일

    @Column(length = 50, nullable = true)
    private String workExperience; // 경력

    @Column(length = 50, nullable = true)
    private String regId; // 작성자

    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDate; // 작성일자시간

    @Column(length = 50, nullable = true)
    private String modId; // 최종 작성자

    @LastModifiedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modDate; // 최종작성일자

    // 빌더 패턴
    @Builder
    public Employee(String id, String name, String ssn, String departmentCode, String subDepartmentCode,
                    String positionCode, LocalDateTime hireDate, LocalDateTime resignationDate, String workExperience,
                    String regId, LocalDateTime regDate, String modId, LocalDateTime modDate) {
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
    }

    // Employee -> EmployeeDTO 변환 메서드
    public EmployeeDTO toDto() {
        return EmployeeDTO.builder()
                .idx(idx)
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
    }

    // UPDATE 수행을 위한 메서드
    public void changeEmployee(EmployeeDTO employeeDTO) {
        this.name = employeeDTO.getName();
        this.ssn = employeeDTO.getSsn();
        this.departmentCode = employeeDTO.getDepartmentCode();
        this.subDepartmentCode = employeeDTO.getSubDepartmentCode();
        this.positionCode = employeeDTO.getPositionCode();
        this.hireDate = employeeDTO.getHireDate();
        this.resignationDate = employeeDTO.getResignationDate();
        this.workExperience = employeeDTO.getWorkExperience();

        // modId, modDate는 수정 시에만 업데이트
        if (employeeDTO.getModId() != null) {
            this.modId = employeeDTO.getModId();
        }
        if (employeeDTO.getModDate() != null) {
            this.modDate = employeeDTO.getModDate();
        }
    }
}
