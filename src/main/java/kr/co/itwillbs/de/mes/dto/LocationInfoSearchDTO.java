package kr.co.itwillbs.de.mes.dto;

import kr.co.itwillbs.de.common.vo.PageDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LocationInfoSearchDTO {

	private String name; // 공장 이름
	private String address; // 주소
	private String isDeleted; // 삭제 유무
	
	private PageDTO pageDTO = new PageDTO(); // 페이징 정보
}
