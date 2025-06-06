package kr.co.itwillbs.de.mes.dto;

import kr.co.itwillbs.de.common.vo.PageDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class RawMaterialSearchDTO {

	private String type; 
	private String name; 
	private String isDeleted; 

	private String searchKeyword;
	
	private PageDTO pageDTO = new PageDTO(); // 페이징 정보
}
