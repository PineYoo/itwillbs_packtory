package kr.co.itwillbs.de.mes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class RecipeProcessDTO {

	private Long idx;
	private String masterIdx;
	@NotBlank(message = "이름은 필수 입력 값입니다.")
	private String name;
	@NotBlank(message = "코드는 필수 입력 값입니다.")
	private String code;
	@NotBlank(message = "작업순서는 필수 입력 값입니다.")
	private String codeSeq;
	private String qcType;
	@NotBlank(message = "표준 소요시간은 필수 입력 값입니다.")
	private String standardTime;
	@NotBlank(message = "최대 소요시간은 필수 입력 값입니다.")
	private String maxTime;
	
	@Pattern(regexp = "^[YN]$", message = "사용 유무는 필수 입력 값입니다.")
	private String isDeleted;	// 삭제여부
	
	private String regId;			// 작성자
	private String regDate;	// 작성일시
	private String modId;			// 최종작성자
	private String modDate;	// 최종작성일시
	
	// 페이징 변수들
	private int totalCount;
	private int rowAsc;
	private int rowNum;
	
	
	
}
