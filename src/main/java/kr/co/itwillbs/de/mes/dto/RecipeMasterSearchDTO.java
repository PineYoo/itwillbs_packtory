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
public class RecipeMasterSearchDTO {
	
	private String name;
	private String version;
	private String status;
	private String approvalStatus;
	private String isDeleted;
	
	private String validFrom;	// 유효기간 시작일
	private String validTo;		// 유효기간 종료일
	
	private List<CodeItemDTO> typeList;
	
	// 페이징용 DTO composition
	private PageDTO pageDTO = new PageDTO();
	
	

}
