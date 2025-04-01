package kr.co.itwillbs.de.orders.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
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
}
