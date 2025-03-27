package kr.co.itwillbs.de.approval.dto;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import groovy.transform.ToString;
import groovy.transform.builder.Builder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import kr.co.itwillbs.de.approval.constant.ApprovalStatus;
import kr.co.itwillbs.de.approval.constant.ApprovalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DraftDTO {
	private String approval_no;		// 결재번호
	
	private String drafter_id;		// 기안자ID(사원번호)
	private String drafter_name;	// 기안자명(작성자)
	private String drafter_position;// 기안자 직급

	private ApprovalType approval_type;	// 결재유형 (일단 enum으로! 나중에 공통코드로 빼기)
	
	private String doc_no;			// 문서양식번호	(공통코드)
	
	private String approver1;		// 결재자1
	private String approver2;		// 결재자2
	private String approver3;		// 결재자3
	
	private String title;			// 제목		
	private String content;			// 내용
	
//	private String file;			// 첨부파일
	
	private Date draft_date;		// 기안일자
	private Date due_date;			// 마감일자
	
	private ApprovalStatus approvalStatus; // 진행상태 (일단 enum으로! 나중에 공통코드로 뺄지 고민)
	
	
}
