package kr.co.itwillbs.de.mes.dto;

import kr.co.itwillbs.de.common.vo.PageDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QcRequiredSearchDTO {

	private String idx;
	private String status;
	private String materialName;
	private String qcName;
	
	private PageDTO pageDTO = new PageDTO(); // 페이징 정보
}
