package kr.co.itwillbs.de.commute.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.commute.dto.CommuteDTO;
import kr.co.itwillbs.de.commute.dto.CommuteListDTO;
import kr.co.itwillbs.de.commute.dto.CommuteSearchDTO;
import kr.co.itwillbs.de.commute.entity.Commute;
import kr.co.itwillbs.de.commute.mapper.CommuteMapper;
import kr.co.itwillbs.de.commute.repository.CommuteRepository;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class CommuteService {

	@Autowired
	private CommuteMapper commuteMapper;

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
	public void saveCommuteInfo(CommuteDTO commuteDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		commuteMapper.insertCommuteInfo(commuteDTO);
	}

}
