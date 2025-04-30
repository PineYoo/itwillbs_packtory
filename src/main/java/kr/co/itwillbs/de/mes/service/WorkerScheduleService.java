package kr.co.itwillbs.de.mes.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.human.dto.EmployeeDTO;
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
			log.info("근무 일정 수정 완료 - qcIdx: {}", workerScheduleDTO.getIdx());
		}
	}

	// 비즈니스 데이 계산
	private List<LocalDate> getBusinessDays(LocalDate startDate, LocalDate endDate) {
		List<LocalDate> businessDays = new ArrayList<>();
		LocalDate date = startDate;
		while (!date.isAfter(endDate)) {
			DayOfWeek day = date.getDayOfWeek();
			if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
				businessDays.add(date);
			}
			date = date.plusDays(1);
		}
		return businessDays;
	}

	public List<WorkerScheduleDTO> generateSchedules(List<EmployeeDTO> employees, LocalDate startDate,
			LocalDate endDate, String shiftType1, String location1Code, String shiftType2, String location2Code) {
		// 생산부 소속 직원만 필터링 (부서 코드가 5인 직원만)
		List<EmployeeDTO> productionEmployees = employees.stream().filter(e -> "5".equals(e.getDepartmentCode()))
				.collect(Collectors.toList());

		// 팀별 근무 일정 생성 (부서가 5이고 서브부서가 4 또는 5인 직원들만)
		List<EmployeeDTO> team1 = productionEmployees.stream().filter(e -> "4".equals(e.getSubDepartmentCode()))
				.collect(Collectors.toList());
		List<EmployeeDTO> team2 = productionEmployees.stream().filter(e -> "5".equals(e.getSubDepartmentCode()))
				.collect(Collectors.toList());

		// 비즈니스 데이 계산
		List<LocalDate> businessDays = getBusinessDays(startDate, endDate);

		// 근무 일정 리스트 생성
		List<WorkerScheduleDTO> scheduleList = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		// 동적으로 등록폼에서 받은 shiftType과 locationCode를 사용하여 팀별 일정 생성
		scheduleList.addAll(createSchedulesForTeam(team1, businessDays, shiftType1, location1Code, formatter)); // 1팀
		scheduleList.addAll(createSchedulesForTeam(team2, businessDays, shiftType2, location2Code, formatter)); // 2팀

		return scheduleList;
	}

	// 팀별 근무 일정 생성
	private List<WorkerScheduleDTO> createSchedulesForTeam(List<EmployeeDTO> team, List<LocalDate> businessDays,
			String shiftType, String locationCode, DateTimeFormatter formatter) {
		List<WorkerScheduleDTO> schedules = new ArrayList<>();
		for (EmployeeDTO emp : team) {
			for (LocalDate day : businessDays) {
				WorkerScheduleDTO schedule = WorkerScheduleDTO.builder().employeeId(emp.getId()).shiftType(shiftType)
						.locationIdx(locationCode).startDate(day.format(formatter)).build();
				schedules.add(schedule);
			}
		}
		return schedules;
	}
}