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
public class RecipeMaterialDTO {

	private String idx;
	private String processIdx;	// 레시피 IDX
	private String materialIdx;	// 자재 IDX
	@NotBlank(message = "이름은 필수 입력 값입니다.")
	private String name;		// 이름
	@NotBlank(message = "단위는 필수 입력 값입니다.")
	private String unit;		// 단위
	@PositiveOrZero(message = "수량은 0개 이상만 입력 가능합니다.")
	private String quantity;	// 개수
	@NotBlank(message = "작업시간은 필수 입력 값입니다.")
	private String mixSeq;		// 혼합순서
	
	@Pattern(regexp = "^[YN]$", message = "사용 유무는 필수 입력 값입니다.")
	private String isDeleted;	// 삭제여부
	
	private String regId;		// 작성자
	private String regDate;		// 작성일시
	private String modId;		// 최종작성자
	private String modDate;		// 최종작성일시
}
