package kr.co.itwillbs.de.mes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class BomDTO {

	private String idx;
	@NotBlank(message = "타입은 필수 입력 값입니다.")
	private String type;
	private String parentsIdx;
	//@NotBlank(message = "상품은 필수 입력 값입니다.")
	private String productIdx;
	private String code;
	@NotBlank(message = "이름은 필수 입력 값입니다.")
	private String name;
	private String description;
	@NotBlank(message = "스펙은 필수 입력 값입니다.")
	private String spec;
	@NotBlank(message = "단위는 필수 입력 값입니다.")
	private String unit;
	@PositiveOrZero(message = "수량은 0개 이상만 입력 가능합니다.")
	private int quantity;
	@Pattern(regexp = "^[YN]$")
	private String isDeleted;
	
	private String regId;
	private String regDate;
	private String modId;
	private String modDate;
}
