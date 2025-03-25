package kr.co.itwillbs.de.orders.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OrderDetailDTO {
    private Long idx; 				// 테이블_인덱스
    
    private String documentNumber; 	// 문서번호
    private String departmentCode; 	// 담당부서_코드
    private String clientName; 		// 담당자_이름
    private String clientPhone; 	// 담당자_연락처
    private String lotNumber; 		// LOT
    private String invoiceIssue; 	// 세금계산서발행
    private String paymentStatus; 	// 수금처리
    private String unissuedStatus; 	// 미발행
    private String supplyAmount; 	// 공급가액
    private String vatAmount; 		// 부가세액
    private String totalAmount; 	// 합계금액
    private String issueCode; 		// 이슈_코드
    private String issueRemarks; 	// 이슈_비고
    private String isDeleted; 		// 삭제유무
    private String regId; 			// 작성자
    private LocalDateTime regDate; 	// 작성일자시간
    private String modId; 			// 최종 작성자
    private LocalDateTime modDate; 	// 최종작성일자
}
