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
public class ProductSearchDTO {

	private String name; // 이름
	private Integer minPrice; // 최소 가격
	private Integer maxPrice; // 최대 가격
	private String isDeleted; // 삭제 유무

	private String searchKeyword;
	
	private PageDTO pageDTO = new PageDTO();

}
