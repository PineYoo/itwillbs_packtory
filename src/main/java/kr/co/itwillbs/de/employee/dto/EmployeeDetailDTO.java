package kr.co.itwillbs.de.employee.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class EmployeeDetailDTO {

    private String id; // 사원번호

    private String phoneNumber; // 전화번호

    private String email; // 이메일

    private String gender; // 성별

    private String address1; // 주소1

    private String address2; // 주소2

    private String fileIdxs; // 프로필 사진

    @NotBlank(message = "급여은행코드는 필수 입력 값입니다.")
    private String salaryBankCode; // 급여은행코드

    @NotBlank(message = "급여은행이름은 필수 입력 값입니다.")
    private String salaryBankName; // 급여은행이름

    @NotBlank(message = "급여계좌번호는 필수 입력 값입니다.")
    private String salaryAccountNumber; // 급여계좌번호

    @NotBlank(message = "급여계좌예금주는 필수 입력 값입니다.")
    private String salaryAccountHolder; // 급여계좌예금주

    @NotBlank(message = "직원차량유무는 필수 입력 값입니다.")
    private String hasVehicle; // 직원차량유무 (Yes/No)

    @NotBlank(message = "직원상태코드는 필수 입력 값입니다.")
    private String employeeStatusCode; // 직원상태코드

    @NotNull(message = "직원상태시작일자는 필수 입력 값입니다.")
    private LocalDateTime statusStartDate; // 직원상태시작일자

    private LocalDateTime statusEndDate; // 직원상태종료일자

    private String regId; // 작성자

    private LocalDateTime regDate; // 작성일자시간

    private String modId; // 최종 작성자

    private LocalDateTime modDate; // 최종작성일자
    
    // EmployeeDetail 객체를 받아서 DTO로 변환하는 생성자 추가
    public EmployeeDetailDTO(EmployeeDetail employeeDetail) {
        this.id = employeeDetail.getId();
        this.phoneNumber = employeeDetail.getPhoneNumber();
        this.email = employeeDetail.getEmail();
        // 필요한 다른 필드들도 초기화
    }

    @Builder
    public EmployeeDetailDTO(String id, String phoneNumber, String email, String gender, String address1, String address2, 
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

    // EmployeeDetailDTO -> EmployeeDetail 엔티티 변환 메서드
    public EmployeeDetail toEntity() {
        return EmployeeDetail.builder()
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
}
