package kr.co.itwillbs.de.approval.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.approval.dto.DraftDTO;

@Mapper
public interface ApprovalMapper {

	DraftDTO selectEmployee(String userId);
	
}
