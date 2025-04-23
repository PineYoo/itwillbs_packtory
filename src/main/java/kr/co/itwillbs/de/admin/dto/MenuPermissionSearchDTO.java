package kr.co.itwillbs.de.admin.dto;

import kr.co.itwillbs.de.common.vo.PageDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MenuPermissionSearchDTO {

	private String idx;
	private String menuName;
	private String ownerMemberName;
	private String groupName;
	private String permissionCode;
	private String regId;
	private String regDate;
	private String isDeleted;
	
	// 페이징용 DTO composition
	private PageDTO pageDTO = new PageDTO();
}
