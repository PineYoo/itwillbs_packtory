package kr.co.itwillbs.de.commute.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.commute.dto.CommuteListDTO;

@Mapper
public interface CommuteMapper {

	// 출퇴근 목록 조회
	List<CommuteListDTO> getCommuteList(String id);
	
}
