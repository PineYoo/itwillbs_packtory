package kr.co.itwillbs.de.mes.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class WorkOrdersMasterDTO {
	
	private String idx;
	// 문서번호
	private String documentNumber;
	// 상품idx (t_product.idx 참조)
	private String productIdx;
	// 레시피idx (t_recipe_master.idx 참조)
	private String recipeMasterIdx;
	// 목표 수량
	private String targetQuantity;
	// 단위
	private String unit;
	// 작업시작일자
	private String workStartDate;
	// 작업종료일자
	private String workEndDate;
	// 작업관리자(t_employee.id+t_worker_metrics.id => work_is_supervisor='Y' 참조)
	private String supervisorId;
	// 중요도(1 ~ 5) => 숫자가 높을수록 중요도 낮음
	private String priority;
	// 상태(1:준비 2:진행 3:완료 4:취소)
	private String status;
	
	private String memo;
	private String isDeleted;
	private String regId;
	private String regDate;
	private String modId;
	private String modDate;

}
