package kr.co.itwillbs.de.approval.dto;

import java.util.Date;

import kr.co.itwillbs.de.approval.constant.ApprovalStatus;
import kr.co.itwillbs.de.approval.constant.ApprovalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ApprovalDTO {
	
	//	일단 임시로 String(방세현)
//	private String approvalStatus; // 진행상태 (일단 enum으로! 나중에 공통코드로 뺄지 고민)
//	
//	private String drafterId;		// 기안자ID(사원번호)
//	private String drafterName;	// 기안자명(작성자)
//	private String drafterDepartment;// 기안자 부서
//	private String drafterPosition;// 기안자 직급
//
//	private String approval_type;	// 결재유형 (일단 enum으로! 나중에 공통코드로 빼기)
//	
//	private String docNo;			// 문서양식번호	(공통코드)
//	private String title;			// 제목		
//	private String content;			// 내용
//	private String uploadFile;		// 첨부파일
//	
//	private String draftDate;		// 기안일자
//	private String dueDate;			// 마감일자
	
	//	결재 번호
	private String approvalNo;
	//	결재 유형
	private String approvalType;
	//	문서양식번호
	private String docNo;
	//	제목
	private String title;
	//	내용
	private String content;
	//	첨부파일(?)
	private String uploadFile;
	//	진행상태
	private String progressStatus;
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
	//	결재자2
	private String approver2;
	//	결재자3
	private String approver3;
	
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
	//	직급
	private String positionCode;
	//	부서이름
	private String childName;
	
	
	
	
	
}
