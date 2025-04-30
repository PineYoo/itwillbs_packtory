package kr.co.itwillbs.de.mes.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.human.dto.EmployeeDTO;
import kr.co.itwillbs.de.mes.dto.WorkerScheduleDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WorkScheduleGeneratorService {

	/**
	 * 고정 근무 일정 생성 (9 to 6)
	 */
	public List<WorkerScheduleDTO> generateFixedSchedules(List<EmployeeDTO> employees, LocalDate startDate,
			LocalDate endDate) {
		log.info("고정 근무 일정 생성 요청 - 인원 수: {}, 기간: {} ~ {}", employees.size(), startDate, endDate);

		List<WorkerScheduleDTO> result = new ArrayList<>();

		// 1. 팀 기준으로 사원 분류 (부서 코드로 그룹화)
		Map<String, List<EmployeeDTO>> teamMap = employees.stream()
				.collect(Collectors.groupingBy(EmployeeDTO::getDepartmentCode));

		// 2. 각 팀에 대해 고정 근무 일정 생성
		for (List<EmployeeDTO> teamMembers : teamMap.values()) {

			// 3. 지정된 기간에 대해 근무 일정을 생성
			for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {

				// 주말/공휴일 제외
				if (!isBusinessDay(date))
					continue;

				// 4. 팀원별로 일정 생성
				for (EmployeeDTO emp : teamMembers) {
					WorkerScheduleDTO dto = new WorkerScheduleDTO();
					dto.setEmployeeId(emp.getId()); // 직원 ID 설정
					dto.setShiftType("9:00 ~ 18:00"); // 고정 근무 시간
					dto.setLocationIdx("1"); // 장소 인덱스 설정 (예시로 1로 설정, 실제 값에 맞게 수정 필요)
					dto.setStartDate(date.toString()); // 근무 시작일자
					dto.setEndDate(date.toString()); // 근무 종료일자
					result.add(dto);
				}
			}
		}

		return result;
	}

	// 주말/공휴일 여부 체크 (단순히 주말 체크로 구현)
	private boolean isBusinessDay(LocalDate date) {
		return !(date.getDayOfWeek().getValue() == 6 || date.getDayOfWeek().getValue() == 7); // 토요일, 일요일 제외
	}
}
