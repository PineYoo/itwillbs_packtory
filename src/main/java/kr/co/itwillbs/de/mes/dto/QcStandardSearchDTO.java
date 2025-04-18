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
public class QcStandardSearchDTO {
	
	private String name; // 품질 검사 이름
	private String memo; // 메모
	private String isDeleted; // 삭제 여부
    
    private PageDTO pageDTO = new PageDTO();  	// 페이징 정보
}
