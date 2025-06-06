package kr.co.itwillbs.de.commute.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.commute.dto.CommuteDTO;
import kr.co.itwillbs.de.commute.dto.CommuteListDTO;
import kr.co.itwillbs.de.commute.dto.CommuteSearchDTO;
import kr.co.itwillbs.de.commute.dto.CommuteStatusDTO;
import kr.co.itwillbs.de.commute.entity.Commute;
import kr.co.itwillbs.de.commute.mapper.CommuteMapper;
import kr.co.itwillbs.de.commute.repository.CommuteRepository;
import kr.co.itwillbs.de.human.dto.DepartmentInfoDTO;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class CommuteService {

	@Autowired
	private CommuteMapper commuteMapper;

	/**
	 * 출퇴근 목록 조회 요청(SELECT) 카운트 페이징용
	 * @param id
	 * @param commuteSearchDTO
	 * @return List<CommuteListDTO>
	 */
	public int getCommuteCountForPaging(String id, CommuteSearchDTO commuteSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return commuteMapper.getCommuteCountForPaging(id, commuteSearchDTO);
	}
	
	/**
	 * 출퇴근 목록 조회 요청(SELECT)
	 * @param id
	 * @param commuteSearchDTO
	 * @return List<CommuteListDTO>
	 */
	public List<CommuteListDTO> getCommuteList(String id, CommuteSearchDTO commuteSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return commuteMapper.getCommuteList(id, commuteSearchDTO);
	}
	
	/**
	 * 부서 조회 요청(SELECT)
	 * @param id 
	 * @return List<DepartmentInfoDTO>
	 */
	public List<DepartmentInfoDTO> getDepartmentList(String id) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return commuteMapper.getDepartmentList(id);
	}
	
	/**
	 * 날짜별 지각 건수 조회 요청(SELECT)
	 * @return List<CommuteStatusDTO>
	 */
	public List<CommuteStatusDTO> getCommuteCountByDate() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return commuteMapper.getCommuteCountByDate();
	}
	
	// ======================================================================================
	/**
	 * 로그인 한 사번의 오늘 출근 기록 조회 요청(SELECT)
	 * @param id
	 * @param today
	 * @return CommuteDTO
	 */
	public CommuteDTO getCheckInTime(String id, LocalDate today) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return commuteMapper.getCheckInTime(id, today);
	}

	/**
	 * 로그인 한 사번의 오늘 퇴근 기록 조회 요청(SELECT)
	 * @param id
	 * @param today
	 * @return CommuteDTO
	 */
	public CommuteDTO getCheckOutTime(String id, LocalDate today) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return commuteMapper.getCheckOutTime(id, today);
	}

	/**
	 * 로그인 한 사번의 오늘 마지막 출퇴근 기록 조회 요청(SELECT)
	 * @param id
	 * @param today
	 * @return CommuteDTO
	 */
	public CommuteDTO getTodayLastCommute(String id, LocalDate today) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return commuteMapper.getTodayLastCommute(id, today);
	}
	
	/**
	 * 출퇴근 기록 저장 요청(INSERT)
	 * @param commuteDTO
	 */
	@LogExecution
	public void saveCommuteInfo(CommuteDTO commuteDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		commuteMapper.insertCommuteInfo(commuteDTO);
	}

}
