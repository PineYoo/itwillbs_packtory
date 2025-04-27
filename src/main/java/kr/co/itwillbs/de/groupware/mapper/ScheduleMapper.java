package kr.co.itwillbs.de.groupware.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.itwillbs.de.groupware.dto.ScheduleDTO;
import kr.co.itwillbs.de.groupware.dto.ScheduleSearchDTO;

@Mapper
public interface ScheduleMapper {
	
	/**
	 * INSERT INTO t_bom values bomDTO
	 * @param ScheduleDTO
	 * @return insert key
	 */
	int registerSchedule(ScheduleDTO dto);

	/**
	 * SELECT count(*) from t_bom where bomSearchDTO (페이징용)
	 * @param ScheduleSearchDTO
	 * @return count
	 */
	int getSchedulesCountBySearchDTO(ScheduleSearchDTO searchDTO);
	
	/**
	 * SELECT FROM t_bom where bomSearchDTO
	 * @param ScheduleSearchDTO
	 * @return List<ScheduleDTO>
	 */
	List<ScheduleDTO> getSchedulesBySearchDTO(ScheduleSearchDTO searchDTO);

	/**
	 * SELECT FROM t_schedules where idx = #{idx}
	 * @param idx
	 * @return ScheduleDTO
	 */
	ScheduleDTO getScheduleByIdx(@Param("idx")String idx);

	/**
	 * UPDATE t_schedules SET bomDTO
	 * @param ScheduleDTO
	 * @return affectedRow
	 */
	int modifySchedule(ScheduleDTO dto);
	
	/**
	 * UPDATE t_schedules set is_deleted = ScheduleDTO
	 * @param ScheduleDTO
	 * @return affectedRow
	 */
	int removeSchedule(ScheduleDTO dto);
}
