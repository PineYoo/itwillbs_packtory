package kr.co.itwillbs.de.orders.dto;

import java.util.List;

import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.common.vo.PageDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OrderSearchDTO {
	
	private String documentNumber; // 문서 번호
	private String tradeCode; // 문서 종류 코드(1:수주, 2:발주, 3:구매) *25.04.16 이 필드가 없어지면 안됨
	private String statusCode; // 주문상태_코드
	private String companyName; // 거래처명
	private String dueDate; // 마감일자
	private String requestStartDate; // 요청일자(시작일)
	private String requestEndDate; // 요청일자(종료일)
	private String dueStartDate; // 요청마감일자(시작일)
	private String dueEndDate; // 요청마감일자(종료일)
	private String isDeleted; // 삭제유무
	// 검색 키워드
	private String searchKeyword; // 키워드

	private PageDTO pageDTO = new PageDTO();

	private List<CodeItemDTO> statusList;

}
