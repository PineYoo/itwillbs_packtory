package kr.co.itwillbs.de.groupware.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.groupware.dto.ScheduleDTO;
import kr.co.itwillbs.de.groupware.dto.ScheduleRecord;
import kr.co.itwillbs.de.groupware.dto.ScheduleSearchDTO;
import kr.co.itwillbs.de.groupware.mapper.ScheduleMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ScheduleService {

	private final ScheduleMapper scheduleMapper;
	public ScheduleService(ScheduleMapper scheduleMapper) {
		this.scheduleMapper = scheduleMapper;
	}
	
	/**
	 * Schedule 등록
	 * @param ScheduleDTO
	 * @throws Exception
	 */
	public void registerSchedule(ScheduleDTO dto) throws Exception {
		LogUtil.logStart(log);
		
		scheduleMapper.registerSchedule(dto);
	}

	/**
	 * Schedule 리스트 페이지 페이징용 카운트
	 * @param ScheduleSearchDTO
	 * @return
	 */
	public int getSchedulesCountBySearchDTO(ScheduleSearchDTO searchDTO) {
		LogUtil.logStart(log);
		
		return scheduleMapper.getSchedulesCountBySearchDTO(searchDTO);
	}
	
	/**
	 * Schedule 리스트 검색 조건 조회
	 * @param ScheduleSearchDTO
	 * @return
	 */
	public List<ScheduleDTO> getSchedulesBySearchDTO(ScheduleSearchDTO searchDTO) {
		LogUtil.logStart(log);
		
		return scheduleMapper.getSchedulesBySearchDTO(searchDTO);
	}

	/**
	 * Schedule 상세 단건 조회
	 * @param idx
	 * @return
	 */
	public ScheduleDTO getScheduleByIdx(String idx) {
		LogUtil.logStart(log);
		
		return scheduleMapper.getScheduleByIdx(idx);
	}

	/**
	 * Schedule 수정
	 * @param ScheduleDTO
	 */
	public void modifySchedule(ScheduleDTO dto) {
		LogUtil.logStart(log);
		
		scheduleMapper.modifySchedule(dto);
	}
	
	/**
	 * Schedule 수정
	 * @param ScheduleDTO
	 */
	public void removeSchedule(ScheduleDTO dto) {
		LogUtil.logStart(log);
		
		scheduleMapper.removeSchedule(dto);
	}
	
	/**
	 * Schedule Api호출용
	 * @param ScheduleDTO
	 */
	public List<ScheduleRecord> getApiSchedule(ScheduleSearchDTO searchDTO) {
		LogUtil.logStart(log);
		
		return scheduleMapper.getApiSchedule(searchDTO);
	}
	

}
