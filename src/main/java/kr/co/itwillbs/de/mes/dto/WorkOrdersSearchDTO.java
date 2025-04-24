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
public class WorkOrdersSearchDTO {
	
	private String idx;
	// 문서번호
	private String documentNumber;
	
	// 상품idx (t_product.idx 참조)
	private String productIdx;
	// 상품이름(검색용)
	private String productName;
	
	// 목표 수량
	private String targetQuantity;
	
	// 작업시작일자
	private String workStartDate;
	// 작업종료일자
	private String workEndDate;
	
	// 작업관리자(t_employee.id+t_worker_metrics.id => work_is_supervisor='Y' 참조)
	private String supervisorId;
	// 작업관리자 이름
	private String supervisorName;
	
	// 중요도(1 ~ 5) => 숫자가 높을수록 중요도 낮음
	private String priority;
	
	// 상태(1:준비 2:진행 3:완료 4:취소)
	private String status;
	
	private String memo;
	private String isDeleted;

	private List<CodeItemDTO> typeList;
	
	// 페이징용 DTO composition
	private PageDTO pageDTO = new PageDTO();
	
	
}
