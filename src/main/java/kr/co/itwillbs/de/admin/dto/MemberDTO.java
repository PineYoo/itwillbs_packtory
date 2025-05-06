package kr.co.itwillbs.de.admin.dto;

import kr.co.itwillbs.de.common.aop.annotation.RequiredSessionIds;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredSessionIds(fields = {"regId", "modId"})
public class MemberDTO {
	
	// t_member
	private String idx;
	private String memberId;
	private String password;
	private String role;
	private String roleName;
	private String roleDescription;
	private String status;
	private String statusName;
	private String statusDescription;
	private String isDeleted;
	private String regId;
	private String regDate;
	private String modId;
	private String modDate;
	
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
	
	// 페이징 변수들
	private int totalCount;
	private int rowAsc;
	private int rowNum;
}
