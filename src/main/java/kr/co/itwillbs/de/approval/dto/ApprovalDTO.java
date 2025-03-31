package kr.co.itwillbs.de.approval.dto;

import java.util.Date;

import groovy.transform.ToString;
import kr.co.itwillbs.de.approval.constant.ApprovalStatus;
import kr.co.itwillbs.de.approval.constant.ApprovalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ApprovalDTO {
	
	private ApprovalStatus approvalStatus; // 진행상태 (일단 enum으로! 나중에 공통코드로 뺄지 고민)
	
	private String drafter_id;		// 기안자ID(사원번호)
	private String drafter_name;	// 기안자명(작성자)
	private String drafter_department;// 기안자 부서
	private String drafter_position;// 기안자 직급

	private ApprovalType approval_type;	// 결재유형 (일단 enum으로! 나중에 공통코드로 빼기)
	
	private String doc_no;			// 문서양식번호	(공통코드)
	private String title;			// 제목		
	private String content;			// 내용
//	private String file;			// 첨부파일
	
	private Date draft_date;		// 기안일자
	private Date due_date;			// 마감일자
	
	private String approver1;		// 결재자1
	private String approver2;		// 결재자2
	private String approver3;		// 결재자3
	
	private String approver1_status; 	// 결재자1 승인여부
	private String approver2_status; 	// 결재자2 승인여부
	private String approver3_status; 	// 결재자3 승인여부
	
	private Date approver1_date; 		// 결재자1 승인날짜
	private Date approver2_date;	 	// 결재자2 승인날짜
	private Date approver3_date; 		// 결재자3 승인날짜
	
//	private String approver1_signature;	// 결재자1 서명
//	private String approver2_signature;	// 결재자2 서명
//	private String approver3_signature;	// 결재자3 서명
	
	
}
