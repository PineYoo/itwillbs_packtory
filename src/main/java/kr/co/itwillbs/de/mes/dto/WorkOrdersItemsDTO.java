package kr.co.itwillbs.de.mes.dto;

import kr.co.itwillbs.de.common.aop.annotation.RequiredSessionIds;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@RequiredSessionIds(fields = {"regId", "modId"})
public class WorkOrdersItemsDTO {
	
	private String idx;
	// 문서번호
	private String documentNumber;
	// 생산라인(t_location_info.idx 참조)
	private String productLinesIdx;
	// 생산라인 이름
	private String productLinesName;
	
	// 라인관리자(t_employee.id+t_position_info => is_manager='Y' 참조)
	private String linesManagerId;
	// 라인관리자 이름
	private String linesManagerName;
	// 라인관리자 번호
	private String linesManagerPhone;
	
	// 실제시작일자
	private String actualStartDate;
	// 실제종료일자
	private String actualEndDate;
	// 생산된수량
	private String producedQuantity;
	
	// 단위
	private String unit;
	// 상태(1:준비 2:진행 3:완료 4:취소)
	private String status;
	
	private String memo;
	private String isDeleted;
	private String regId;
	private String regDate;
	private String modId;
	private String modDate;

}
