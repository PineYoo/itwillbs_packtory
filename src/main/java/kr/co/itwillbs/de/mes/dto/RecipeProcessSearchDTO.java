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
public class RecipeProcessSearchDTO {

	// TODO 25.04.21 레시피 테이블 완성 후 JOIN 걸어서 가져올 이름
	private String recipeMasterName;
	
	private String masterIdx;
	private String name;
	private String code;
	private String codeSeq;
	private String standardTime;
	private String maxTime;
	private String isDeleted;
	
	private String regStartDate;
	private String regEndDate;
	
	private String searchKeyword;
	
	
	private String productIdx;
	private int quantity;
	
	private List<CodeItemDTO> typeList;
	
	// 페이징용 DTO composition
	private PageDTO pageDTO = new PageDTO();
	
}
