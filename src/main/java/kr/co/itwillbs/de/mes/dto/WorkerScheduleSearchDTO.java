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
public class WorkerScheduleSearchDTO {

	private String employeeId; // 사원번호

	private String locationIdx; // 장소 인덱스
	private String locationName; // 장소 이름

	private String startDateFrom; // 시작일자 범위 시작
	private String startDateTo; // 시작일자 범위 끝

	private String endDateFrom; // 종료일자 범위 시작
	private String endDateTo; // 종료일자 범위 끝

	private String memo; // 메모
	private String isDeleted; // 삭제유무

	private PageDTO pageDTO = new PageDTO(); // 페이징 정보
}