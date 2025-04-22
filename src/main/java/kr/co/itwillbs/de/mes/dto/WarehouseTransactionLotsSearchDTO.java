package kr.co.itwillbs.de.mes.dto;

import kr.co.itwillbs.de.common.vo.PageDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class WarehouseTransactionLotsSearchDTO {

	private String lotIdx; // 롯 인덱스
	private String transactionIdx; // 트랜잭션 인덱스
	
	private Integer minQuantity; // 최소 총개수
	private Integer maxQuantity; // 최대 총개수
	
	private String isDeleted; // 삭제 유무

	private PageDTO pageDTO = new PageDTO(); // 페이징 정보
}
