package kr.co.itwillbs.de.human.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EmployeeSearchDTO {
    private String name;					// 이름
    private String departmentCode;			// 대표 부서 코드
    private String subDepartmentCode;		// 하위 부서 코드
    private String positionCode;			// 직급 코드
    private LocalDate hireDateFrom;  		// 입사일 범위 시작
    private LocalDate hireDateTo;    		// 입사일 범위 끝
    private LocalDate resignationDateFrom;	// 퇴사일 범위 시작
    private LocalDate resignationDateTo;	// 퇴사일 범위 끝
}
