package kr.co.itwillbs.de.approval.constant;

public enum ApprovalStatus {
	IN_PROGRESS,  // 진행중
    REJECTED,     // 반려
    REQUESTED,    // 결재요청
    APPROVED      // 결재완료
    
    // Delegated(대결), PostApproval(후결) 등은 나중에..!! 
}
