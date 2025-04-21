package kr.co.itwillbs.de.groupware.dto;

import jakarta.validation.constraints.NotBlank;
import kr.co.itwillbs.de.mes.dto.RecipeMasterDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ApprovalDTO {
	
	private String approvalNo;			// 결재 번호
	private String approvalType;		// 결재 유형 코드
	private String approvalTypeName;	// 결재 유형 이름
	private String docNo;				// 문서 양식 코드
	private String docName;				// 문서 양식 이름
	

    @NotBlank(message = "제목은 필수 입력 값입니다.")
	private String title;				// 제목
	private String content;				// 내용
	private String uploadFile;			// 첨부파일(?)
	private String progressStatus;		// 진행상태코드
	private String progressName;		// 진행상태이름
	private String drafterId;			// 기안자
	private String draftDate;			// 기안일자
	private String dueDate;				// 마감일자
	private String modId;				// 최종 작성자
	private String modDate;				// 최종 작성 일자
	
	// 결재자1
	private String approver1;			// id
	private String approver1Name;		// 이름
	private String approver1Status;		// 승인여부
	private String approver1Date;		// 승인날짜
	private String approver1Signature;	// 서명
	// 결재자2
	private String approver2;
	private String approver2Name;
	private String approver2Status;
	private String approver2Date;
	private String approver2Signature;
	// 결재자3
	private String approver3;
	private String approver3Name;
	private String approver3Status;
	private String approver3Date;
	private String approver3Signature;
	

	private String name;				// employee 이름
	private String departmentCode;		// 부서코드
	private String departmentName;		// 부서이름
	private String subDepartmentCode;	// 팀코드
	private String subDepartmentName;	// 팀이름
	private String positionCode;		// 직급코드
	private String positionName;		// 직급이름
	
	
    private RecipeMasterDTO recipeMaster;

	
}
