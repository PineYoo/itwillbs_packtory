package kr.co.itwillbs.de.orders.dto;

import kr.co.itwillbs.de.common.vo.PageDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientSearchDTO {
	private String companyNumber;
	private String companyName;
	private String ownerName;
	private String isDeleted;
	
    private PageDTO pageDTO = new PageDTO();
    
    private String searchKeyword;			// 키워드
}
