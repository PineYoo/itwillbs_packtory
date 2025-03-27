package kr.co.itwillbs.de.approval.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	
}
