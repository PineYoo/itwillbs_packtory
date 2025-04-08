package kr.co.itwillbs.de.human.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Setter
@Getter
@NoArgsConstructor
@ToString
public class EmployeeDetailVO {
	private Long idx;					// 인덱스
	private String id;					// 사원번호

	private String phoneNumber;			// 전화번호
	private String email;				// 이메일
	private String gender;				// 성별
	private String address1;			// 주소1
	private String address2;			// 주소2

	private String fileIdxs;			// 첨부파일 id들

	private String salaryBankCode;		// 급여은행코드
	private String salaryBankName;		// 급여은행이름
	private String salaryAccountNumber;	// 급여계좌번호
	private String salaryAccountHolder;	// 급여계좌예금주

	private String hasVehicle;			// 직원차량유무 Y/N
	private String employeeStatusCode;	// 직원상태코드
	private LocalDate statusStartDate;	// 직원상태시작일자
	private LocalDate statusEndDate;	// 직원상태종료일자

	private String regId;				// 작성자
	private LocalDateTime regDate;		// 작성일자
	private String modId;				// 최종작성자
	private LocalDateTime modDate;		// 최종작성일자
	
	private String isDeleted;           // 삭제유무 Y/N
}
