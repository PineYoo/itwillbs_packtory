package kr.co.itwillbs.de.human.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class EmployeeDetailDTO {
    private Long idx;                    // 인덱스
    private String id;                   // 사원번호

    private String phoneNumber;          // 전화번호
    private String email;                // 이메일
    private String gender;               // 성별
    private String address1;             // 주소1
    private String address2;             // 주소2

    private String fileIdxs;             // 첨부파일 id들

    private String salaryBankCode;       // 급여은행코드
    private String salaryBankName;       // 급여은행이름
    private String salaryAccountNumber;  // 급여계좌번호
    private String salaryAccountHolder;  // 급여계좌예금주

    private String hasVehicle;           // 차량유무 Y/N
    private String employeeStatusCode;   // 직원상태코드
    private LocalDate statusStartDate;   // 직원상태 시작일
    private LocalDate statusEndDate;     // 직원상태 종료일

    private String regId;                // 작성자
    private LocalDateTime regDate;       // 작성일자
    private String modId;                // 수정자
    private LocalDateTime modDate;       // 수정일자

    private String isDeleted;            // 삭제 여부
}
