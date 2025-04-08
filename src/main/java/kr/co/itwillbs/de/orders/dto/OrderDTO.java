package kr.co.itwillbs.de.orders.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
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
public class OrderDTO {
    private Long idx; 				// 테이블_인덱스
    
    private String documentNumber; 	// 문서번호
    private String tradeCode; 		// 거래종류_코드
    private String statusCode; 		// 주문상태_코드
    private String companyName; 	// 거래처명
    private String companyNumber; 	// 사업자번호
    private String requestDate; 	// 요청일자
    private String expectedDate; 	// 예정일
    private String dueDate; 		// 마감일자
    private String destinationName;	// 목적지명
    private String receiver; 		// 받는사람
    private String phone1; 			// 전화번호1
    private String phone2; 			// 전화번호2
    private String postCode; 		// 우편번호
    private String address1; 		// 목적지주소1
    private String address2; 		// 목적지주소2
    private String specialIssue; 	// 특이사항
    private String isDeleted; 		// 삭제유무
    private String regId; 			// 작성자
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDate; 	// 작성일자시간
    private String modId; 			// 최종 작성자
    private LocalDateTime modDate;	// 최종작성일자
    
    //OderDetail
    private String departmentCode;	// 담당자 부서코드
    private String clientName;		// 담당자 이름
    private String clientPhone;		// 담당자 연락처
    private String lotNumber;		// LOT
    private String invoiceIssue;	// 세금계산서 발행
    private String paymentStatus;	// 수금처리
    private String unissuedStatus;	// 미발행
    private String supplyAmount;	// 공급가액
    private String vatAmount;		// 부가세액
    private String totalAmount;		// 합계금액
    private String issueCode;		// 이슈코드
    private String issueRemaks;		// 이슈비고
    
    // 진짜 이게 맞나 모르겠다 타임리프 form 안에 object를 쓰기 위해..
    private List<CodeItemDTO> codeItemList;
}
