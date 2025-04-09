package kr.co.itwillbs.de.admin.dto;

import kr.co.itwillbs.de.common.vo.PageDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class EmployeeSearchDTO {

	private String id;
	private String name;
	private String ssn;
	private String departmentCode;
	private String departmentName;
	private String subDepartmentCode;
	private String subDepartmentName;
	private String positionCode;
	private String positionName;
	private String hireDate;
	private String hireStartDate;
	private String hireEndDate;
	private String resignationDate;
	
	// 페이징용 DTO composition
	private PageDTO pageDTO = new PageDTO();
}
