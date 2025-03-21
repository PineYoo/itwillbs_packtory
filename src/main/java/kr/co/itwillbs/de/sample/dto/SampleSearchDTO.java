package kr.co.itwillbs.de.sample.dto;

import kr.co.itwillbs.de.sample.constant.IsDeleted;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SampleSearchDTO {

	private String type;
	
	private String id;
	
	private String name;
	
	private IsDeleted isDeleted;
}
