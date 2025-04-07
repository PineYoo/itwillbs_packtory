package kr.co.itwillbs.de.approval.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.approval.dto.ApprovalDTO;
import kr.co.itwillbs.de.approval.dto.DraftDTO;

@Mapper
public interface ApprovalMapper {
	//	=========================================================
	//	보라씨 작업
	//	로그인한 userId로 사원 정보 가져오기
	DraftDTO selectEmployeeInfo(String userId);
	
	//	기안번호 중복 확인
	String getLastApprovalNo(String today);
	
	//	기안서 저장
	int insertApproval(DraftDTO draftDTO);
	
	//	=========================================================
	//	전자결재 목록 조회	
	List<ApprovalDTO> getApprovalList();

	
	
	
	
	
	//	=========================================================
	//	결재라인을 위한 모든 회원 목록 조회
	List<DraftDTO> getAllEmployeeInfo();
	
	//	결재라인 AJAX로 검색어 조회
	List<DraftDTO> getSearchEmployeeInfo(String keyword);
	
	
	
}
