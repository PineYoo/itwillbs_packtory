package kr.co.itwillbs.de.commute.dto;

import java.util.List;

import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.human.dto.DepartmentInfoDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CommuteSearchDTO {
	
	private String employeeId; // 사원번호
	private String workStartDate; // 날짜(시작일)
	private String workEndDate; // 날짜(종료일)
	private String department; // 부서
	private String name; // 이름
	
	// 검색 키워드
	private String searchKeyword; // 키워드
	
	private List<DepartmentInfoDTO> departmentInfo;	// 부서

}
