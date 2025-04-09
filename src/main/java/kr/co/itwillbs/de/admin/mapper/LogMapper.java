package kr.co.itwillbs.de.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.admin.dto.LogDTO;
import kr.co.itwillbs.de.admin.dto.LogSearchDTO;

@Mapper
public interface LogMapper {

	/**
	 * 개발테스트용 로그 데이터 입력
	 * @param logDTO
	 * @return
	 */
	int registerLog(LogDTO logDTO);

	/**
	 * 로그 리스트 가져오기
	 * @return List<LogDTO>
	 */
	List<LogDTO> getLogList();

	/**
	 * 페이징 카운트가져오기
	 * @param logSearchDTO
	 * @return count(*) int
	 */
	int getLogSearchCountForPaging(LogSearchDTO logSearchDTO);
	/**
	 * 로그 리스트 검색조건 가져오기
	 * @param logSearchDTO
	 * @return List<LogDTO>
	 */
	List<LogDTO> getLogSearchList(LogSearchDTO logSearchDTO);

	/**
	 * 로그 상세 보기
	 * @param idx
	 * @return LogDTO
	 */
	LogDTO getLog(Long idx);
}
