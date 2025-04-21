package kr.co.itwillbs.de.human.dto;

import kr.co.itwillbs.de.common.vo.PageDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PositionSearchDTO {
	
	private String positionCode;
	private String positionName;
	private String isManager;
	private String isDeleted;
	
	// 페이징용 DTO composition
	private PageDTO pageDTO = new PageDTO();
}
