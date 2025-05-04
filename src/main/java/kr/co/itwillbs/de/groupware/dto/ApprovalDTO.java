package kr.co.itwillbs.de.groupware.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import kr.co.itwillbs.de.common.aop.annotation.RequiredSessionIds;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredSessionIds(fields = {"regId", "modId"})
public class ApprovalDTO {
	
	private String idx;					// 테이블_인덱스
	@NotBlank(message = "결제유형은 필수 선택 값입니다.")
	private String approvalType;		// 결재 유형 코드
	private String approvalTypeName;	// 결재 유형 이름

	private String progressStatus;		// 진행상태코드
	private String progressName;		// 진행상태이름
	
	private String docNo;				// 문서 양식 코드
	private String docName;				// 문서 양식 이름
	
    @NotBlank(message = "제목은 필수 입력 값입니다.")
	private String title;				// 제목
    @NotBlank(message = "내용은 필수 입력 값입니다.")
	private String content;				// 내용
    
    // 결재유형에 따라 들어갈 컬럼
    private String recipeMasterIdx;		// 레시피마스터_idx
    private String recipeMasterName;	// 레시피마스터_name
    private String eventStartDate;		// 휴가시작일
    private String eventEndDate;		// 휴가종료일
    
    private String drafterId;			// 기안자
    @NotBlank(message = "기안일자는 필수 선택 값입니다.")
    private String draftDate;			// 기안일자
    private String dueDate;				// 마감일자

    // regId = drafterId
    private String regId;				// 작성자
    private LocalDateTime regDate;		// 작성일시
    private String modId;				// 최종작성자
    private LocalDateTime modDate;		// 최종작성일시

    // 필요 없을 듯
    private String uploadFile;			// 첨부파일(?)
	
    //ApprovalItemsDTO
	// 결재자
	private String approver1;
	private String approver2;
	@NotBlank(message = "결재자는 필수 선택 값입니다.")
	private String approver3;
	private String approver1Name;
	private String approver2Name;
	private String approver3Name;
	
	// 결재자 상태
	private String approver1Status;	
	private String approver2Status;	
	private String approver3Status;	

	// 결재자 승인날짜
	private String approver1Date;
	private String approver2Date;
	private String approver3Date;
	
	// 결재자 서명
	private String approver1Signature;
	private String approver2Signature;
	private String approver3Signature;
	
	// 승인/반려 시 필요한 데이터
	private int approverIndex;	// 몇번째 결재자인지
	private String approverStatus;	// 승인인지 반려인지
	private String approverId;		// 결재자 id
	
	// 레시피마스터(레시피 결재 시 필요한 DTO)
//	private RecipeMasterDTO recipeMaster;
	
	// 왜있지 일단두자
	private String name;				// employee 이름
	private String departmentCode;		// 부서코드
	private String departmentName;		// 부서이름
	private String subDepartmentCode;	// 팀코드
	private String subDepartmentName;	// 팀이름
	private String positionCode;		// 직급코드
	private String positionName;		// 직급이름
	
	
	
}
