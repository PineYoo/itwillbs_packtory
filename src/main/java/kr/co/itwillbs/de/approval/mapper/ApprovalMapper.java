package kr.co.itwillbs.de.approval.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.approval.dto.DraftDTO;

@Mapper
public interface ApprovalMapper {

	// 로그인한 userId로 사원 정보 가져오기
	DraftDTO selectEmployeeInfo(String userId);
	
	// 기안번호 중복 확인
	String getLastApprovalNo(String today);
	
	// 기안서 저장
	int insertApproval(DraftDTO draftDTO);
	
}
