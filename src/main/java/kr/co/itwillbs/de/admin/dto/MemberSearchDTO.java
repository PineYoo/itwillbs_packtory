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
public class MemberSearchDTO {

	private String memberId;
	private String memberName;
	private String role;
	private String status;
	private String isDeleted;
	private String regId;
	private String regDate;
	
	// 페이징용 DTO composition
	private PageDTO pageDTO = new PageDTO();
}
