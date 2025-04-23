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
public class WorkerMetricsSearchDTO {

	private String employeeId; // 사원번호
	private String memo; // 메모
	private String isDeleted; // 삭제유무

	private PageDTO pageDTO = new PageDTO(); // 페이징 정보
}
