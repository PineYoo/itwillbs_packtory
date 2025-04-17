package kr.co.itwillbs.de.mes.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Setter
@Getter
@NoArgsConstructor
@ToString
public class WarehouseVO {

	private Long idx; 						// Idx (PK)
	
	private String productIdx;				// 상품_Idx
	private String lotIdx;					// Lot_Idx
	
	private String type; 					// 타입
	private String directionCode;			// 입출고 상태
	private Integer quantity; 				// 총 개수
	private Integer usedQuantity;			// 사용 개수
	private String unit; 					// 단위
	
	private LocalDate manufacturingDate; 	// 제조일자
	private LocalDate expiryDate; 			// 사용기한
	
	private String name; 					// 창고이름
	private String memo; 					// 메모
	private String status; 					// 상태
	
	private String is_deleted; 				// 삭제 여부
	
	private String regId; 					// 최초 등록자
	private LocalDateTime regDate; 			// 최초 등록일
	private String modId; 					// 최종 수정자
	private LocalDateTime modDate; 			// 최종 수정일
}

