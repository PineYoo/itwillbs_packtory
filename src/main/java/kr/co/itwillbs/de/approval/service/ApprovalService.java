package kr.co.itwillbs.de.approval.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import kr.co.itwillbs.de.approval.dto.DraftDTO;
import kr.co.itwillbs.de.approval.mapper.ApprovalMapper;

@Service
public class ApprovalService {
	@Autowired
	private ApprovalMapper approvalMapper;

	//------------------------------------------------------------
	// 로그인한 userId로 사원 정보 가져오기
	public DraftDTO getEmployeeInfo(String userId) {
		return approvalMapper.selectEmployeeInfo(userId);
	}
	
	
	//------------------------------------------------------------
	// 기안서 등록(저장) 요청
	public int registerApproval(DraftDTO draftDTO) {
		
		// ----------- approval_no 결재번호 생성 ----------- 
		// "A"+ yyyyMMdd + 4자리 숫자 (총13자리)  예) A202503270001
//		String approval_no = draftDTO.getApproval_no();
		
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		int code = 1;
		
		// DB에서 오늘 날짜의 최대 결재번호 조회
		String lastApprovalNo = approvalMapper.getLastApprovalNo(today);
		System.out.println("최대 결재번호: " + lastApprovalNo); //A202503270001
		
		if(lastApprovalNo != null) { // 기안한 것이 있을 경우 최대 결재번호의 마지막 4자리 숫자에서 +1
			int lastCode = Integer.parseInt(lastApprovalNo.substring(9));
			code = lastCode + 1;
		}
		
		String formattedCode = String.format("%04d", code); // 4자리 숫자로 포맷
//		approval_no = "A" + today + formattedCode; // 최종 결재번호 생성
		
//		draftDTO.setApproval_no(approval_no);
		System.out.println(draftDTO);
		//-------------------------------------------------------------
		// 기안서 저장
		return 1;
//		return approvalMapper.insertApproval(draftDTO);
	}
	
	
}
