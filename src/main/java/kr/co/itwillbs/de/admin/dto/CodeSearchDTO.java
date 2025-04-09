package kr.co.itwillbs.de.admin.dto;

import kr.co.itwillbs.de.common.vo.PageDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class CodeSearchDTO {

	private String idx;
	private String majorCode;
	private String name;
	private String description;
	private String isDeleted;
	private String regId;
	private String minDate;
	private String maxDate;
	
	// 페이징용 DTO composition
	private PageDTO pageDTO = new PageDTO();
}
