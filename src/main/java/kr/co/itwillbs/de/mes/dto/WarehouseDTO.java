package kr.co.itwillbs.de.mes.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import kr.co.itwillbs.de.common.aop.annotation.RequiredSessionIds;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@RequiredSessionIds(fields = { "regId", "modId" })
public class WarehouseDTO {

	private Long idx; // Idx (PK)

	@NotBlank(message = "상품_Idx는 필수 입력 값입니다.")
	private String productIdx; // 상품_Idx

	@NotBlank(message = "Lot_Idx는 필수 입력 값입니다.")
	private String lotIdx; // Lot_Idx

	private String type; // 타입

	private String directionCode; // 입출고 상태

	private Integer quantity; // 총 수량

	private Integer usedQuantity; // 사용 수량

	private String unit; // 단위
	
	private String manufacturingDate; // 제조일자
	
	private String expiryDate; // 사용기한

	@NotBlank(message = "이름은 필수 입력 값입니다.")
	private String name; // 창고이름

	private String memo; // 메모

	private String status; // 상태

	private String isDeleted; // 삭제 여부

	private String regId; // 최초 등록자
	private LocalDateTime regDate; // 최초 등록일
	private String modId; // 최종 수정자
	private LocalDateTime modDate; // 최종 수정일

	// 한글 변환 (뷰페이지)
	private String productName;
}
