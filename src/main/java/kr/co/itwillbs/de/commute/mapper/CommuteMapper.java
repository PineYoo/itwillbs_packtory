package kr.co.itwillbs.de.commute.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.itwillbs.de.commute.dto.CommuteDTO;
import kr.co.itwillbs.de.commute.dto.CommuteListDTO;
import kr.co.itwillbs.de.commute.dto.CommuteSearchDTO;

@Mapper
public interface CommuteMapper {

	/**
	 * 출퇴근 목록 조회(SELECT)
	 * @param id
	 * @param commuteSearchDTO
	 * @return List<CommuteListDTO>
	 */
	List<CommuteListDTO> getCommuteList(@Param("id") String id, 
										@Param("search") CommuteSearchDTO commuteSearchDTO);

	/**
	 * 로그인 한 사번의 오늘 출근 기록 조회(SELECT)
	 * @param id
	 * @param today
	 * @return CommuteDTO
	 */
	CommuteDTO getCheckInTime(@Param("id") String id, 
			 				  @Param("today") LocalDate today);

	/**
	 * 로그인 한 사번의 오늘 퇴근 기록 조회(SELECT)
	 * @param id
	 * @param today
	 * @return CommuteDTO
	 */
	CommuteDTO getCheckOutTime(@Param("id") String id, 
							   @Param("today") LocalDate today);

	/**
	 * 로그인 한 사번의 오늘 마지막 출퇴근 기록 조회(SELECT)
	 * @param id
	 * @param today
	 * @return CommuteDTO
	 */
	CommuteDTO getTodayLastCommute(@Param("id") String id, 
								   @Param("today") LocalDate today);
	
	/**
	 * 출퇴근 기록 저장(INSERT)
	 * @param commuteDTO
	 */
	void insertCommuteInfo(CommuteDTO commuteDTO);
	

	
}
