package kr.co.itwillbs.de.mes.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import kr.co.itwillbs.de.common.aop.annotation.RequiredSessionIds;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@RequiredSessionIds(fields = { "regId", "modId" })
public class WorkerMetricsDTO {

	private Long idx; // 테이블 인덱스

	@NotBlank(message = "사원번호는 필수 입력 값입니다.")
	private String employeeId; // 사원번호

	private String isSupervisor; // 생산_감독관
	private String isLinesManager; // 라인_관리자
	private String isForkliftOperator; // 지게차_자격
	private String isDriver; // 운전면허_자격

	private String memo; // 메모
	private String isDeleted; // 삭제유무

	private String regId; // 작성자
	private LocalDate regDate; // 작성일자시간
	private String modId; // 최종 작성자
	private LocalDate modDate; // 최종작성일자

	// 페이징 변수들
	private int totalCount;
	private int rowAsc;
	private int rowNum;

	// workermetric/search-popup에서 사용
	private String employeeName;
	private String departmentName;
	private String subDepartmentName;
	private String positionName;
}
