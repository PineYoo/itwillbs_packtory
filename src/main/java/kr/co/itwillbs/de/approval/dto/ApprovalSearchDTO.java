package kr.co.itwillbs.de.approval.dto;

import java.util.List;

import groovy.transform.ToString;
import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ApprovalSearchDTO {
	
	private String documentNumber;
	private String dueDate;
	private String requestStartDate;
	private String requestEndDate;
	private String dueStartDate;
	private String dueEndDate;
	private String isDeleted;
	
	private List<CodeItemDTO> codeItemList;
	
}

















