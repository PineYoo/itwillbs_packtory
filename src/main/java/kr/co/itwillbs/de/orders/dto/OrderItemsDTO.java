package kr.co.itwillbs.de.orders.dto;

import java.time.LocalDateTime;

import kr.co.itwillbs.de.common.aop.annotation.RequiredSessionIds;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredSessionIds(fields = {"regId", "modId"})
public class OrderItemsDTO {
	private Long idx; 					// 테이블 인덱스
	
	private String documentNumber; // 주문번호(orderDocumentNumber)
	private String materialIdx; // 자재_idx
	private String productIdx; // 상품_idx
	private String Name; // 아이템명
	private Integer quantity; // 개수
	private Integer unitPrice; // 개수별 금액
	private Integer totalPrice; // 합계액
	private String isDeleted; // 삭제유무
	private String regId; // 작성자
	private LocalDateTime regDate; // 작성일자시간
	private String modId; // 최종 작성자
	private LocalDateTime modDate; // 최종 작성일자
	
	// 25.05.05 TrasnferService에서 사용할 필드 추가
	private Long orderIdx;
	private String tradeCode;
	private String statusCode;
	private String unit;
	private String orderDocumentNumber;
	
	// 25.05.04 변경사항?
//	private String productNumber; // 상품_idx
//	private String productName; // 아이템명
//	private String productValue; // 개수
}
