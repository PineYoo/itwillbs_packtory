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
	private Long idx;				// 기안서ID
	
	private String drafter_id;		// 기안자ID(사원번호)
	private String drafter_name;	// 기안자명(작성자)
	private String drafter_department;// 기안자 부서
	private String drafter_position;// 기안자 직급

	//	@NotBlank(message = "결재유형을 선택해주세요.")
	private ApprovalType approval_type;	// 결재유형 (일단 enum으로! 나중에 공통코드로 빼기)
	
//	@NotBlank(message = "기안서 양식을 선택해주세요.")
	private String doc_code;		// 문서코드	(공통코드)
	
//	@NotEmpty(message = "결재자1 선택 필수")
	private String approver1;		// 결재자1
//	@NotEmpty(message = "결재자2 선택 필수")
	private String approver2;		// 결재자2
//	@NotEmpty(message = "결재자3 선택 필수")
	private String approver3;		// 결재자3
	
//	@NotEmpty(message = "제목을 입력해주세요.")
	private String title;			// 제목		
//	@NotEmpty(message = "내용을 입력해주세요.")
	private String content;			// 내용
	
//	private String file;			// 첨부파일
	
	private Date draft_date;		// 기안일자
	private Date due_date;			// 마감일자
	
	private ApprovalStatus approvalStatus; // 진행상태 (일단 enum으로! 나중에 공통코드로 뺄지 고민)
	
	
//	@Builder
//	public DraftDTO(Long id, String drafter_id, String drafter_name, String drafter_position,
//			String approval_type, String doc_code, String approver1, String approver2, String approver3,
//			String title, String content, String file, MultipartFile file_get, Date draft_date, Date due_date) {
//		this.id = id;
//		this.drafter_id = drafter_id;
//		this.drafter_name = drafter_name;
//		this.drafter_position = drafter_position;
//		this.approval_type = approval_type;
//		this.doc_code = doc_code;
//		this.approver1 = approver1;
//		this.approver2 = approver2;
//		this.approver3 = approver3;
//		this.title = title;
//		this.content = content;
//		this.file = file;
//		this.draft_date = draft_date;
//		this.due_date = due_date;
//	}
	
	// DrafterDTO 객체 > ApprovalEntity로 변환하여 리턴하는 메서드
	
	
}
