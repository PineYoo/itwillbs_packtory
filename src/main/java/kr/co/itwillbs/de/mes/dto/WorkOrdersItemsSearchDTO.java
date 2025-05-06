package kr.co.itwillbs.de.mes.dto;

import java.util.List;

import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.common.aop.annotation.RequiredSessionIds;
import kr.co.itwillbs.de.common.vo.PageDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class WorkOrdersItemsSearchDTO {
	
	private String idx;
	// 문서번호
	private String documentNumber;
	
	// 상품idx (t_product.idx 참조)
	private String productLinesIdx;
	// 상품이름(검색용)
	private String productLinesName;
	
	private String linesManagerName;
	
	private String targetQuantity;
	
	private String actualStartDate;
	// 실제종료일자
	private String actualEndDate;
	
	// 상태(1:준비 2:진행 3:완료 4:취소)
	private String status;
	
	private String memo;
	private String isDeleted;

	// 페이징용 DTO composition
	private PageDTO pageDTO = new PageDTO();
	
	
}
