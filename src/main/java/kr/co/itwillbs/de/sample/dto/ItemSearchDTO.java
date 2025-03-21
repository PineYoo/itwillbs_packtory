package kr.co.itwillbs.de.sample.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ItemSearchDTO {

	private String type;
	
	private String itemNm;
	
	//@Min(value = 0, message = "최소가격은 0 이상 입력 가능합니다.") // 최소 100부터 넣어야 할 때와 같은 경우에 사용하면 좋다.
	//@PositiveOrZero(message = "최소가격은 0 이상 입력 가능합니다.")
	private Integer minPrice;
	
	private Integer maxPrice;
}
