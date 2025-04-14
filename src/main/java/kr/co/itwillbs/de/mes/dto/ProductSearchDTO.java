package kr.co.itwillbs.de.mes.dto;

import java.time.LocalDate;

import kr.co.itwillbs.de.common.vo.PageDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProductSearchDTO {
	private String type; // 타입
	private String name; // 이름
	private String unit; // 단위
	private String positionCode; // 직급 코드

	private LocalDate hireDateFrom; // 입사일 범위 시작
	private LocalDate hireDateTo; // 입사일 범위 끝
	private LocalDate resignationDateFrom; // 퇴사일 범위 시작
	private LocalDate resignationDateTo; // 퇴사일 범위 끝

	private PageDTO pageDTO = new PageDTO();
}
