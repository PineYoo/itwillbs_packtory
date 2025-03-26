package kr.co.itwillbs.de.approval.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.approval.dto.DraftDTO;

@Mapper
public interface ApprovalMapper {
	
	// 로그인한 userId로 사원 정보 가져오기
	DraftDTO selectEmployeeInfo(String userId);
	
}
