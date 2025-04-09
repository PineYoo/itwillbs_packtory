package kr.co.itwillbs.de.groupware.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.common.service.CommonService;
import kr.co.itwillbs.de.common.service.FileService;
import kr.co.itwillbs.de.common.vo.FileVO;
import kr.co.itwillbs.de.groupware.dto.ApprovalDTO;
import kr.co.itwillbs.de.groupware.dto.ApprovalSearchDTO;
import kr.co.itwillbs.de.groupware.dto.DraftDTO;
import kr.co.itwillbs.de.groupware.dto.NoticeDTO;
import kr.co.itwillbs.de.groupware.mapper.ApprovalMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApprovalService {
	@Autowired
	private ApprovalMapper approvalMapper;
	@Autowired
	private CommonService commonService;
	@Autowired
	private FileService fileService;
	
	
	//	=====================================================================================
	//	보라씨 작업
	//	------------------------------------------------------------
	// 로그인한 userId로 사원 정보 가져오기
	public ApprovalDTO getEmployeeInfo(String memberId) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return approvalMapper.selectEmployeeInfo(memberId);
	}
	
	//	------------------------------------------------------------
	//	기안서 등록(저장) 요청
	@LogExecution // 로그 남길 서비스
	public String registerApproval(ApprovalDTO approvalDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		//	------------------------------------------------------------
		//	approval_no 결재번호 생성
		String year = new SimpleDateFormat("yyyy").format(new Date());
		String approvalNo = "A" + year + "-" + commonService.getApprovalNoFromMySQL();
		
		approvalDTO.setApprovalNo(approvalNo);
		
		//	서버로 넘어오는 값(ex - 2025-04-03 ~ 2025-04-10)
		//	~ 기준으로 앞쪽 날짜를 draftDate(기안일자)로 세팅
		//	~ 기준으로 뒤쪽 날짜를 dueDate(마감일자)로 세팅
		approvalDTO.setDraftDate(approvalDTO.getDueDate().split("~")[0].trim());
		approvalDTO.setDueDate(approvalDTO.getDueDate().split("~")[1].trim());
		log.info("draftDTO : " + approvalDTO);
		//DraftDTO(approval_no=A202503280001, drafter_id=100008, drafter_name=김보라, drafter_department=아이티윌, drafter_position=주임, 
		// approval_type=null, doc_no=, approver1=, approver2=, approver3=, title=ㅇㅇㄹㄴ, content=ㅇㄴㄹㅇ, due_date=null, approvalStatus=null)
		//	-------------------------------------------------------------
		//	기안서 저장
		approvalMapper.insertApproval(approvalDTO); // DB 비즈니스 로직 처리
		
		//	생성한 결재번호 리턴
		return approvalNo;
		
	}
	//	=====================================================================================
	
	//	------------------------------------------------------------
	//	결재서류 전체 목록 조회
	public List<ApprovalDTO> getApprovalList(String memberId) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return approvalMapper.getApprovalList(memberId);
	}

	//	=====================================================================================
	//	결재라인을 위한 모든 회원 목록 조회
	public List<ApprovalDTO> getAllEmployeeInfo() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return approvalMapper.getAllEmployeeInfo();
	}
	//	결재라인 AJAX로 검색어 조회
	public List<ApprovalDTO> getSearchEmployeeInfo(String keyword) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return approvalMapper.getSearchEmployeeInfo(keyword);
	}
	
	/**
	 * 결재번호로 전자결재문서 가져오기
	 * @param approvalNo
	 * @return ApprovalDTO
	 */
	public ApprovalDTO getApprovalByApprovalNo(String approvalNo) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		ApprovalDTO approvalDTO = approvalMapper.getApprovalByApprovalNo(approvalNo);
		return approvalDTO;
	}

	/**
	 * 전자결재 업데이트
	 * @param approvalDTO
	 */
	@LogExecution // 로그 남길 서비스
	public void modifyApproval(ApprovalDTO approvalDTO) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		//	기안일자 세팅
		approvalDTO.setDraftDate(approvalDTO.getDueDate().split("~")[0].trim());
		//	마감일자 세팅
		approvalDTO.setDueDate(approvalDTO.getDueDate().split("~")[1].trim());
		
		int affectedRow = approvalMapper.modifyApproval(approvalDTO);
		log.info("affectedRow is {}", affectedRow);
		//TODO 0이 나올 경우 예외처리 필요? 다음엔 좀더 예쁘게?
		if(affectedRow < 1) {
			throw new Exception("수정할 문서가 존재하지 않습니다.");
		}
		
		
	}

	/**
	 * 검색 키워드로 문서 목록 조회
	 * @param approvalSearchDTO
	 * @return
	 */
	public List<ApprovalDTO> getApprovalSearchList(String memberId, ApprovalSearchDTO approvalSearchDTO) {
		return approvalMapper.getApprovalSearchList(memberId, approvalSearchDTO);
	}

	/**
	 * 전자결재 파일 개별 삭제(AJAX) 
	 * @param idx
	 */
	@LogExecution // 로그 남길 서비스
	public void removeFile(String idx) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		FileVO fileVO = new FileVO();
		
		//	받아온 idx 세팅
		fileVO.setIdx(idx);
		//	ENUM 대신 임시로 Y값 하드코딩
		fileVO.setIsDeleted("Y");
		
		int affectedRow = fileService.removeFile(fileVO);
		log.info("affectedRow is {}", affectedRow);
		if(affectedRow < 1) {
			throw new Exception("업데이트할 샘플 데이터가 존재하지 않습니다.");
		}
	}

	/**
	 * 필터를 기준으로 approval 목록 가져오기
	 * @param filter
	 * @param memberId
	 * @return List<ApprovalDTO>
	 */
	public List<ApprovalDTO> getApprovalListByFilter(String filter, String memberId) {
		return approvalMapper.getApprovalListByFilter(filter, memberId);
	}

	
	
	
	
	
	
	
	
}
