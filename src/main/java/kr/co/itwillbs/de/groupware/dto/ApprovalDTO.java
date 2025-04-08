package kr.co.itwillbs.de.groupware.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ApprovalDTO {
	//	결재 번호
	private String approvalNo;
	
	//	결재 유형 코드
	private String approvalType;
	//	결재 유형 이름
	private String approvalTypeName;
	
	//	문서 양식 코드
	private String docNo;
	//	문서 양식 이름
	private String docName;
	
	//	제목
	private String title;
	//	내용
	private String content;
	//	첨부파일(?)
	private String uploadFile;
	
	//	진행상태코드
	private String progressStatus;
	//	진행상태이름
	private String progressName;
	
	//	기안자
	private String drafterId;
	//	기안일자
	private String draftDate;
	//	마감일자
	private String dueDate;
	//	최종 작성자
	private String modId;
	//	최종 작성 일자
	private String modDate;
	
	//	결재자1
	private String approver1;
	private String approver1Name;
	//	결재자2
	private String approver2;
	private String approver2Name;
	//	결재자3
	private String approver3;
	private String approver3Name;
	
 	//	결재자1 승인여부
	private String approver1Status;
 	//	결재자2 승인여부
	private String approver2Status;
 	//	결재자3 승인여부
	private String approver3Status;
	
	//	결재자1 승인날짜
	private String approver1Date;
 	//	결재자2 승인날짜
	private String approver2Date;
	//	결재자3 승인날짜
	private String approver3Date;
	
	//	결재자1 서명
	private String approver1Signature;
	//	결재자2 서명
	private String approver2Signature;
	//	결재자3 서명
	private String approver3Signature;
	
	//	employee 이름
	private String name;
	//	부서코드
	private String departmentCode;
	//	부서이름
	private String departmentName;
	//	팀코드
	private String subDepartmentCode;
	//	팀이름
	private String subDepartmentName;
	//	직급코드
	private String positionCode;
	//	직급이름
	private String positionName;
	
	
	
	
	
}
