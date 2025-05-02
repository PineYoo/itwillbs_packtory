package kr.co.itwillbs.de.mes.service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.mes.dto.WorkerScheduleDTO;
import kr.co.itwillbs.de.mes.dto.WorkerScheduleSearchDTO;
import kr.co.itwillbs.de.mes.mapper.WorkerScheduleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkerScheduleService {

	private final WorkerScheduleMapper workerScheduleMapper;

	// 근무 일정 등록
	@LogExecution
	@Transactional
	public String insertWorkerSchedule(WorkerScheduleDTO workerScheduleDTO) {
		log.info("근무 일정 등록 요청: {}", workerScheduleDTO);
		workerScheduleMapper.insertWorkerSchedule(workerScheduleDTO);
		log.info("근무 일정 등록 완료 - idx: {}", workerScheduleDTO.getIdx());
		return "redirect:/mes/workerschedule";
	}

	// 근무 일정 총 개수 (검색 조건 포함)
	public int searchWorkerScheduleCount(WorkerScheduleSearchDTO searchDTO) {
		log.info("근무 일정 개수 조회 - 검색 조건: {}", searchDTO);
		return workerScheduleMapper.searchWorkerScheduleCount(searchDTO);
	}

	// 근무 일정 목록 조회 (검색 + 페이징)
	public List<WorkerScheduleDTO> searchWorkerSchedule(WorkerScheduleSearchDTO searchDTO) {
		log.info("근무 일정 목록 조회 - 검색 조건: {}", searchDTO);
		return workerScheduleMapper.searchWorkerSchedule(searchDTO);
	}

	// 근무 일정 상세 조회
	public WorkerScheduleDTO getWorkerScheduleByIdx(Long idx) {
		log.info("근무 일정 상세 조회 - idx: {}", idx);
		return workerScheduleMapper.getWorkerScheduleByIdx(idx);
	}

	// 근무 일정 수정
	@LogExecution
	@Transactional
	public void updateWorkerSchedule(WorkerScheduleDTO workerScheduleDTO) {
		log.info("근무 일정 수정 요청 - idx: {}", workerScheduleDTO.getIdx());

		// WorkerMetricsDTO 가 널인지 체크
		if (workerScheduleDTO != null) {
			// 근무 일정 정보를 업데이트하는 쿼리 호출
			workerScheduleMapper.updateWorkerSchedule(workerScheduleDTO);
			log.info("근무 일정 수정 완료 - idx: {}", workerScheduleDTO.getIdx());
		}
	}

	// 비즈니스 데이 계산
	private List<String> getBusinessDays(String startDate, String endDate) {
		List<String> businessDays = new ArrayList<>();

		// DateTimeFormatter를 사용하여 지정된 포맷으로 파싱
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		// String을 LocalDateTime으로 변환
		LocalDateTime date = LocalDateTime.parse(startDate, formatter);
		LocalDateTime end = LocalDateTime.parse(endDate, formatter);

		// 비즈니스 데이 계산
		while (!date.isAfter(end)) {
			DayOfWeek day = date.getDayOfWeek();
			// 주말 제외
			if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
				businessDays.add(date.toString()); // String 날짜로 저장
			}
			date = date.plusDays(1);
		}
		return businessDays;
	}

	// 근무 일정 생성
	public List<WorkerScheduleDTO> generateSchedules(WorkerScheduleDTO workerScheduleDTO) {
		String startDate = workerScheduleDTO.getStartDate();
		String endDate = workerScheduleDTO.getEndDate();

		// 비즈니스 데이 계산
		List<String> businessDays = getBusinessDays(startDate, endDate);

		// 근무 일정 생성
		String shiftType = workerScheduleDTO.getShiftType();
		String locationCode = workerScheduleDTO.getLocationIdx();

		List<WorkerScheduleDTO> scheduleList = new ArrayList<>();

		// 팀별 근무 일정 생성
		List<WorkerScheduleDTO> teamSchedules = createSchedulesForTeam(workerScheduleDTO.getEmployeeId(), businessDays,
				shiftType, locationCode, endDate);

		// 각 스케줄에 endDate 세팅
		for (WorkerScheduleDTO dto : teamSchedules) {
			dto.setEndDate(endDate);
		}

		scheduleList.addAll(teamSchedules);

		log.info("생성된 근무일정 수: {}", scheduleList.size());
		return scheduleList;
	}

	// 팀별 근무 일정 생성
	private List<WorkerScheduleDTO> createSchedulesForTeam(String employeeId, List<String> businessDays,
			String shiftType, String locationCode, String endDate) {
		List<WorkerScheduleDTO> schedules = new ArrayList<>();

		for (String day : businessDays) {
			String startDateString = day + " 00:00:00";

			WorkerScheduleDTO schedule = WorkerScheduleDTO.builder().employeeId(employeeId).shiftType(shiftType)
					.locationIdx(locationCode).startDate(startDateString).endDate(endDate).build();

			schedules.add(schedule);
		}

		return schedules;
	}

}