package kr.co.itwillbs.de.groupware.dto;

import kr.co.itwillbs.de.common.vo.PageDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ScheduleSearchDTO {

	private String title;
	private String content;
	private String startDate;
	private String endDate;
	private String departmentCode;
	private String subDepartmentCode;
	private String isDeleted;
	private String tableName;

	// 페이징용 DTO composition
	private PageDTO pageDTO = new PageDTO();
}
