package kr.co.itwillbs.de.orders.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OrderSearchDTO {
    private String statusCode;		// 주문상태_코드
    private String regDate;			// 작성일자
    
    // 검색 키워드
    private String searchKeyword;	// 키워드
}
