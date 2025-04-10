package kr.co.itwillbs.de.groupware.dto;

import java.util.List;

import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.common.vo.PageDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ApprovalSearchDTO {
	
	private String documentNumber;
	
	private String drafterId;
	private String drafterName;
	private String progressStatus;
	private String approvalType;
	private String title;
	
	//	기안일자
	private String requestStartDate;
	private String requestEndDate;
	
	//	마감일자
	private String dueStartDate;
	private String dueEndDate;
	
	private String isDeleted;
	
	//	--------- 공통코드 -------------
	//	결재진행상태
	private List<CodeItemDTO> progressStatusList;
	//	문서타입
	private List<CodeItemDTO> approvalTypeList;
	
	
	// 페이징용 DTO composition
	private PageDTO pageDTO = new PageDTO();
	
	
}

















