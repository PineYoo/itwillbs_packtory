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
public class RecipeMasterDTO {

	private Long idx;				// 테이블 인덱스
	
	@NotBlank(message = "상품코드는 필수 입력 값입니다.")
	private String productIdx;		// 상품_idx
	
	@NotBlank(message = "BOM코드는 필수 입력 값입니다.")
	private String bomIdx;		// bom_idx
	
	@NotBlank(message = "이름은 필수 입력 값입니다.")
	private String name;			// 이름
	
	@NotBlank(message = "버전은 필수 입력 값입니다.")
	private String version;			// 버전
	
	@NotBlank(message = "레시피 단위는 필수 입력 값입니다.")
	private String batchSize;		// 레시피_단위
	private String status;			// 상태
	private String approvalStatus;	// 결재_상태
	private String approvalStatusName;
	
	@NotBlank(message = "유효기간 시작일은 필수 입력 값입니다.")
	private String validFrom;		// 유효기간 시작일
	
	@NotBlank(message = "유효기간 종료일은 필수 입력 값입니다.")
	private String validTo;			// 유효기간 종료일
	
	@Pattern(regexp = "^[YN]$", message = "사용 유무는 필수 입력 값입니다.")
	private String isDeleted;		// 삭제여부
	private String regId;			// 작성자
	private String regDate;			// 작성일시
	private String modId;			// 최종작성자
	private String modDate;			// 최종작성일시
	
	//product_idx로 테이블을 걸었을 때 가져올 상품 이름
	private String productName;
	
	

}
