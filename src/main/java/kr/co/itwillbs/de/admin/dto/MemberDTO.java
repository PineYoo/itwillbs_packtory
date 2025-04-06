package kr.co.itwillbs.de.admin.dto;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberDTO {
	
	// t_member
	private String idx;
	private String memberId;
	private String password;
	private String role;
	private String status;
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
	
	// 이게 맞아?
	private List<CodeItemDTO> roleItemList;
	private List<CodeItemDTO> statusItemList;
	
	/**
	 * Object 안에 null이 아닌 필드 값 출력
	 * @param obj
	 * @return StringBuffer.toString()
	 */
	public String toStr(Object obj) {
		Class<?> cls = this.getClass();
		Method[] arrMethod = cls.getMethods();
		StringBuffer sb = new StringBuffer(/*this.getClass().toString() 유틸에서는 여기 클래스가 찍히니 패스!*/);
		sb.append("\n");
		try {
			for (Method m : arrMethod) {
				if(m.getName().startsWith("get") && !m.getName().equals("getClass") && m.invoke(this) != null) {
					sb.append(m.getName());
					sb.append(" : ");
					sb.append(m.invoke(this));
					sb.append("\n ");
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
}
