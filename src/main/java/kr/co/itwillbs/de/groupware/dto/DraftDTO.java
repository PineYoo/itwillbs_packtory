package kr.co.itwillbs.de.groupware.dto;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class DraftDTO {
	//	==============================================================================================
	//	보라씨 작업
//	private String approval_no;		// 결재번호
	
//	private String drafter_id;		// 기안자ID(사원번호)
//	private String drafter_name;	// 기안자명(작성자)
//	private String drafter_department;// 기안자 부서
//	private String drafter_position;// 기안자 직급
	
//	private ApprovalType approval_type;	// 결재유형 (일단 enum으로! 나중에 공통코드로 빼기)
	
//	private String doc_no;			// 문서양식번호	(공통코드)
	
//	private String approver1;		// 결재자1
//	private String approver2;		// 결재자2
//	private String approver3;		// 결재자3
	
//	@NotEmpty(message = "제목 입력 필수")
//	private String title;			// 제목		
//	@NotEmpty(message = "내용 입력 필수")
//	private String content;			// 내용
//	private String uploadFile;		// 첨부파일 => 따로 DTO 할것
	
//	private Date draft_date;		// 기안일자
//	private Date due_date;			// 마감일자
//	
//	private ApprovalStatus approvalStatus; // 진행상태 (일단 enum으로! 나중에 공통코드로 뺄지 고민)
	//	==============================================================================================
	
	//	결재번호
	private String approvalNo;
	//	기안자ID(사원번호)
	private String drafterId;
	//	기안자명(작성자)
	private String drafterName;
	//	기안자 부서
	private String drafterDepartment;
	//	기안자 직급
	private String drafterPosition;
	//	결재유형
	private String approvalType;
	//	문서양식번호(공통코드)
	private String docNo;
	//	결재자1
	private String approver1;
	//	결재자2
	private String approver2;
	//	결재자3
	private String approver3;
	//	제목
	private String title;
	//	내용
	private String content;
	//	첨부파일(?)
	private String uploadFile;
	//	기안일자
	private String draftDate;
	//	마감일자
	private String dueDate;
	//	진행상태
	private String approvalStatus;
	
	
	
	
	
	
	
}
