package kr.co.itwillbs.de.common.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class LoginVO {

	//t_member
	private String memberId;
	private String password;
	private String role;
	private String roleName;
	private String roleDescription;
	private String status;
	private String statusName;
	private String statusDescription;
	private String isDeleted;
	
	//t_employee
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
	private String resignationDate;
	
	//t_employee_detail
	private String phoneNumber;
	private String email;
	private String fileIdxs;
	private String employeeStatusCode;
}
