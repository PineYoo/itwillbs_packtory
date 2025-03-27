package kr.co.itwillbs.de.employee.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import kr.co.itwillbs.de.employee.dto.EmployeeDetailDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_employee_detail")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class EmployeeDetail {
	
	@OneToOne
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	private Employee employee; // 연관된 Employee 객체

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idx")
    private Long idx; // 테이블 인덱스

    @Column(name = "id", length = 50, nullable = false)
    private String id; // 사원번호 (기본 테이블의 id와 동일하게 설정)

    @Column(length = 50, nullable = false)
    private String phoneNumber; // 전화번호

    @Column(length = 50, nullable = false)
    private String email; // 이메일

    @Column(length = 50, nullable = false)
    private String gender; // 성별

    @Column(length = 50, nullable = false)
    private String address1; // 주소1

    @Column(length = 50, nullable = true)
    private String address2; // 주소2

    @Column(length = 50, nullable = true)
    private String fileIdxs; // 프로필 사진

    @Column(length = 50, nullable = false)
    private String salaryBankCode; // 급여은행코드

    @Column(length = 50, nullable = false)
    private String salaryBankName; // 급여은행이름

    @Column(length = 50, nullable = false)
    private String salaryAccountNumber; // 급여계좌번호

    @Column(length = 50, nullable = false)
    private String salaryAccountHolder; // 급여계좌예금주

    @Column(length = 2, nullable = false)
    private String hasVehicle; // 직원차량유무 (Yes/No)

    @Column(length = 50, nullable = false)
    private String employeeStatusCode; // 직원상태코드

    @Column(nullable = false)
    private LocalDateTime statusStartDate; // 직원상태시작일자

    @Column(nullable = true)
    private LocalDateTime statusEndDate; // 직원상태종료일자

    @Column(length = 50, nullable = false)
    private String regId; // 작성자

    @CreatedDate
    private LocalDateTime regDate; // 작성일자시간

    @Column(length = 50, nullable = true)
    private String modId; // 최종 작성자

    @LastModifiedDate
    private LocalDateTime modDate; // 최종작성일자


    // 빌더 패턴
    @Builder
    public EmployeeDetail(String id, String phoneNumber, String email, String gender, String address1, String address2, 
                          String fileIdxs, String salaryBankCode, String salaryBankName, String salaryAccountNumber, 
                          String salaryAccountHolder, String hasVehicle, String employeeStatusCode, 
                          LocalDateTime statusStartDate, LocalDateTime statusEndDate, String regId, LocalDateTime regDate, 
                          String modId, LocalDateTime modDate) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.gender = gender;
        this.address1 = address1;
        this.address2 = address2;
        this.fileIdxs = fileIdxs;
        this.salaryBankCode = salaryBankCode;
        this.salaryBankName = salaryBankName;
        this.salaryAccountNumber = salaryAccountNumber;
        this.salaryAccountHolder = salaryAccountHolder;
        this.hasVehicle = hasVehicle;
        this.employeeStatusCode = employeeStatusCode;
        this.statusStartDate = statusStartDate;
        this.statusEndDate = statusEndDate;
        this.regId = regId;
        this.regDate = regDate;
        this.modId = modId;
        this.modDate = modDate;
    }

    // EmployeeDetail -> EmployeeDetailDTO 변환 메서드
    public EmployeeDetailDTO toDto() {
        return EmployeeDetailDTO.builder()
                .id(id)
                .phoneNumber(phoneNumber)
                .email(email)
                .gender(gender)
                .address1(address1)
                .address2(address2)
                .fileIdxs(fileIdxs)
                .salaryBankCode(salaryBankCode)
                .salaryBankName(salaryBankName)
                .salaryAccountNumber(salaryAccountNumber)
                .salaryAccountHolder(salaryAccountHolder)
                .hasVehicle(hasVehicle)
                .employeeStatusCode(employeeStatusCode)
                .statusStartDate(statusStartDate)
                .statusEndDate(statusEndDate)
                .regId(regId)
                .regDate(regDate)
                .modId(modId)
                .modDate(modDate)
                .build();
    }

    // UPDATE 수행을 위한 메서드
    public void changeEmployeeDetail(EmployeeDetailDTO employeeDetailDTO) {
        this.id = employeeDetailDTO.getId();
        this.phoneNumber = employeeDetailDTO.getPhoneNumber();
        this.email = employeeDetailDTO.getEmail();
        this.gender = employeeDetailDTO.getGender();
        this.address1 = employeeDetailDTO.getAddress1();
        this.address2 = employeeDetailDTO.getAddress2();
        this.fileIdxs = employeeDetailDTO.getFileIdxs();
        this.salaryBankCode = employeeDetailDTO.getSalaryBankCode();
        this.salaryBankName = employeeDetailDTO.getSalaryBankName();
        this.salaryAccountNumber = employeeDetailDTO.getSalaryAccountNumber();
        this.salaryAccountHolder = employeeDetailDTO.getSalaryAccountHolder();
        this.hasVehicle = employeeDetailDTO.getHasVehicle();
        this.employeeStatusCode = employeeDetailDTO.getEmployeeStatusCode();
        this.statusStartDate = employeeDetailDTO.getStatusStartDate();
        this.statusEndDate = employeeDetailDTO.getStatusEndDate();
        this.regId = employeeDetailDTO.getRegId();
        this.regDate = employeeDetailDTO.getRegDate();
        this.modId = employeeDetailDTO.getModId();
        this.modDate = employeeDetailDTO.getModDate();
    }
}
