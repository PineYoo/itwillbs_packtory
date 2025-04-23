package kr.co.itwillbs.de.common.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MenuPermissionVO {
	
	private Long menuIdx; // t_menu.idx
	private Long ownerUserId; // t_employee.id
	private Long groupId; // t_employee.department_code
	private String permissionCode; // 퍼미션 비트 예: "750"
	
}
