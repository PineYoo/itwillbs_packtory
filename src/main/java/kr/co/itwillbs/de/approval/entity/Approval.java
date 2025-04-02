package kr.co.itwillbs.de.approval.entity;

import java.sql.Date;

import kr.co.itwillbs.de.approval.constant.ApprovalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//@Entity
//@Table(name="t_approval")
//MyBatis - VO 역할을 할꺼기 때문에 엔티티 X
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Approval {
	
//	private Long idx;				// 테이블 인덱스
	private String approval_no;		// 결재번호
	private String approval_type;	// 결재유형 (휴가/발령/지출결의/...) - 공통코드
	private String doc_no;			// 문서양식번호 (휴가신청서, 지출결의서,..) 
	private String title; 			// 제목
	private String content;			// 내용
	private String file;			// 첨부파일
	private ApprovalType progress_status; // 진행상태 (진행중/반려/결제요청/결재완료)
	
	private String drafter_id;		// 기안자 ID
	private Date draft_date; 		// 기안일자
	private Date due_date;			// 마감일자
	
	private String approver1;		// 결재자1
	private String approver2;		// 결재자2
	private String approver3;		// 결재자3
									
	private String approver1_status; // 결재자1 승인여부(Y/N)
	private String approver2_status; // 결재자2 승인여부
	private String approver3_status; // 결재자3 승인여부
	
	private Date approver1_date;	// 결재자1 승인날짜
	private Date approver2_date;	// 결재자1 승인날짜
	private Date approver3_date;	// 결재자1 승인날짜
	
	private String approver1_signature; // 결재자1 서명
	private String approver2_signature; // 결재자2 서명
	private String approver3_signature; // 결재자3 서명
	
	
}
