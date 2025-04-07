package kr.co.itwillbs.de.admin.dto;

import java.util.List;

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
	
	private List<CodeItemDTO> accessTypeItemList;
	private List<CodeItemDTO> accessDeviceItemList;
}
