package kr.co.itwillbs.de.mes.dto;

import java.math.BigDecimal;

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
	private String type; 			// 타입
	private String name; 			// 이름

	private Integer minQuantity;   	// 최소 수량
	private Integer maxQuantity;   	// 최대 수량
	
	private BigDecimal minPrice; 	// 최소 가격
	private BigDecimal maxPrice; 	// 최대 가격

	private PageDTO pageDTO = new PageDTO();
	
	private String searchKeyword;
}
