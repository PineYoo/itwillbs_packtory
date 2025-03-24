package kr.co.itwillbs.de.admin.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LogSearchDTO {

	private String type;
	
	private String accessId;
	
	private String accessType;
	
	private String accessDevice;
	
	private String ip;
	
	//accessDate 조회용
	private String minDate;
	private String maxDate;
	
	private String url;
	
}
