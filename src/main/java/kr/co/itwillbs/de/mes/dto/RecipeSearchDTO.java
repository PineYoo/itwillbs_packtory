package kr.co.itwillbs.de.mes.dto;

import java.util.List;

import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.common.vo.PageDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class RecipeSearchDTO {

	private String type;		// 타입
	private String name;		// 이름
	private String unit;		// 단위
	private String jobTime;		// 작업시간
	private String isDeleted;	// 삭제여부
	
	private String regStartDate;	// 작성 시작일
	private String regEndDate;		// 작성 종료일
	
	private List<CodeItemDTO> typeList;
	
	// 페이징용 DTO composition
	private PageDTO pageDTO = new PageDTO();
	
	private String searchKeyword; // 키워드

	
}
