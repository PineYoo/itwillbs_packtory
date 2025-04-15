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
public class BomSearchDTO {

	private String type;
	private String code;
	private String name;
	private String description;
	private String spec;
	private String unit;
	private String quantity;
	private String isDeleted;
	
	private String regStartDate;
	private String regEndDate;
	
	private List<CodeItemDTO> typeList;
	
	// 페이징용 DTO composition
	private PageDTO pageDTO = new PageDTO();
}
