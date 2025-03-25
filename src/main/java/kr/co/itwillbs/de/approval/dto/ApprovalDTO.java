package kr.co.itwillbs.de.approval.dto;

import java.io.File;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ApprovalDTO {
	private Long idx;
	
	
	private String approval_type;		
	
	private String doc_code;
	
	private String title;
	
	private String content;
	
	
	private File file;
	
}
