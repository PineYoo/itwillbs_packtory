package kr.co.itwillbs.de.mes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import kr.co.itwillbs.de.common.aop.annotation.RequiredSessionIds;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@RequiredSessionIds(fields = {"regId", "modId"})
public class RecipeDTO {

	private String idx;
	@NotBlank(message = "타입은 필수 입력 값입니다.")
	private String type;		// 타입
	@NotBlank(message = "상품타입을 선택해주세요.")
	private String productIdx;	// 상품_idx
	@NotBlank(message = "BOM타입을 선택해주세요.")
	private String bomIdx;		// bom_idx
	@NotBlank(message = "이름은 필수 입력 값입니다.")
	private String name;		// 이름
	@NotBlank(message = "단위는 필수 입력 값입니다.")
	private String unit;		// 단위
	@PositiveOrZero(message = "수량은 0개 이상만 입력 가능합니다.")
	private int quantity;		// 개수
	@NotBlank(message = "작업시간은 필수 입력 값입니다.")
	private String jobTime;		// 작업시간
	private String memo;		// 메모
	@Pattern(regexp = "^[YN]$", message = "사용 유무는 필수 입력 값입니다.")
	private String isDeleted;	// 삭제여부
	
	private String regId;		// 작성자
	private String regDate;		// 작성일시
	private String modId;		// 최종작성자
	private String modDate;		// 최종작성일시
}
