package kr.co.itwillbs.de.groupware.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.itwillbs.de.groupware.dto.ApprovalDTO;
import kr.co.itwillbs.de.groupware.dto.ApprovalSearchDTO;
import kr.co.itwillbs.de.groupware.dto.DraftDTO;
import kr.co.itwillbs.de.groupware.dto.NoticeDTO;
import kr.co.itwillbs.de.mes.dto.RecipeMasterDTO;

@Mapper
public interface ApprovalMapper {
	//	=========================================================
	//	보라씨 작업
	/**
	 * SELECT 
		   id AS drafterId
		 , name
		 , department_name
		 , position_name
	  FROM empBase
	 WHERE id = #{memberId}
	 * @param approvalDTO
	 * @return
	 */
//	ApprovalDTO selectEmployeeInfo(String memberId);
	
	//	기안번호 중복 확인
//	String getLastApprovalNo(String today);
	
	//	기안서 저장
	int insertApproval(ApprovalDTO approvalDTO);
	int insertApprovalItems(ApprovalDTO approvalDTO);
	
	//	=========================================================
	//	전자결재 목록 조회	
	List<ApprovalDTO> getApprovalList(ApprovalSearchDTO approvalSearchDTO);
	
	//	=========================================================
	//	결재라인을 위한 모든 회원 목록 조회
	List<ApprovalDTO> getAllEmployeeInfo();
	
	//	결재라인 AJAX로 검색어 조회
	List<ApprovalDTO> getSearchEmployeeInfo(String keyword);
	
	/**
	 * 결재번호로 전자결재문서 가져오기
	 * @param approvalNo
	 * @return ApprovalDTO
	 */
	ApprovalDTO getApprovalByDocNo(String docNo);
	
	/**
	 * 전자결재문서 업데이트
	 * @param approvalDTO
	 * @return int
	 */
	int modifyApproval(ApprovalDTO approvalDTO);

//	List<ApprovalDTO> getApprovalSearchList(@Param("memberId") String memberId,
//											@Param("search") ApprovalSearchDTO approvalSearchDTO);

	List<ApprovalDTO> getApprovalListByFilter(@Param("filter") String filter,
											  @Param("memberId") String memberId);

	int getApprovalCountBySearchDTO(ApprovalSearchDTO approvalSearchDTO);
	
	// 진행상태 변경
	void updateProgressStatus(ApprovalDTO approvalDTO);
	
}
