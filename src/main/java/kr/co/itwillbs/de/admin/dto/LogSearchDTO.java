package kr.co.itwillbs.de.admin.dto;

import kr.co.itwillbs.de.common.vo.PageDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LogSearchDTO {

	private String accessId;
	
	private String accessType;
	
	private String accessDevice;
	
	private String ip;
	
	private String parameters;
	
	//accessDate 조회용
	private String accessStartDate;
	private String accessEndDate;
	
	private String url;
	
	// 페이징용 DTO composition
	private PageDTO pageDTO = new PageDTO();
}
