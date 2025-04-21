package kr.co.itwillbs.de.mes.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    private Long idx; 						// Idx (PK) 
	
    @NotBlank(message = "상품_Idx는 필수 입력 값입니다.")
	private String productIdx;				// 상품_Idx
    
	@NotBlank(message = "Lot_Idx는 필수 입력 값입니다.")
	private String lotIdx;					// Lot_Idx
	
	@NotBlank(message = "타입은 필수 입력 값입니다.")
	private String type; 					// 타입
	
	@NotBlank(message = "입출고 상태는 필수 입력 값입니다.")
	private String directionCode;			// 입출고 상태
	
	@NotNull(message = "총개수는 필수 입력 값입니다.")
	private Integer quantity; 				// 총 수량
	
	@NotNull(message = "사용개수는 필수 입력 값입니다.")
	private Integer usedQuantity;			// 사용 수량
	
	@NotBlank(message = "단위는 필수 입력 값입니다.")
	private String unit; 					// 단위
	
	@NotNull(message = "제조일자는 필수 입력 값입니다.")
	private LocalDate manufacturingDate; 	// 제조일자
	
	@NotNull(message = "사용기한은 필수 입력 값입니다.")
	private LocalDate expiryDate; 			// 사용기한
	
	@NotBlank(message = "이름은 필수 입력 값입니다.")
	private String name; 					// 창고이름
	
	@NotBlank(message = "메모는 필수 입력 값입니다.")
	private String memo; 					// 메모
	
	@NotBlank(message = "상태는 필수 입력 값입니다.")
	private String status; 					// 상태
	
	@Pattern(regexp = "^[YN]$", message = "삭제여부는 'Y' 또는 'N'이어야 합니다.")
	private String is_deleted; 				// 삭제 여부
	
	private String regId; 					// 최초 등록자
	private LocalDateTime regDate; 			// 최초 등록일
	private String modId; 					// 최종 수정자
	private LocalDateTime modDate; 			// 최종 수정일
	
	// 한글 변환 (뷰페이지)
	private String productName;
}

