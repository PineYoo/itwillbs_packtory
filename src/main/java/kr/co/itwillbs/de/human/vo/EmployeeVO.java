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
public class EmployeeVO {
    private Long idx;                  // 인덱스 (PK)
    private String id;                 // 사원번호
    private String name;               // 이름
    private String ssn;                // 주민번호

    private String departmentCode;     // 대표부서코드
    private String subDepartmentCode;  // 하위부서코드
    private String positionCode;       // 직급코드

    private LocalDate hireDate;        // 입사일
    private LocalDate resignationDate; // 퇴사일
    private String workExperience;	   // 경력

    private String regId;              // 최초등록자 ID
    private LocalDateTime regDate;     // 최초등록일
    private String modId;              // 최종수정자 ID
    private LocalDateTime modDate;     // 최종수정일
}
