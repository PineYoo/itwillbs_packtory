package kr.co.itwillbs.de.common.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.mapper.WorkScheduleAuditingMapper;
import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.human.dto.EmployeeDTO;
import kr.co.itwillbs.de.mes.dto.WorkOrdersItemsDTO;
import kr.co.itwillbs.de.mes.dto.WorkerScheduleDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class WorkScheduleAuditingService {

	private final WorkScheduleAuditingMapper workScheduleAuditingMapper;
	private final TaskReportRegistryService taskReportRegistryService;

	/*
	 * 조건 : t_work_orders_master.status = 2(: 진행) 이 된 item 데이터를 본다 그 때
	 * t_work_orders_item.actual_start_date ~ actual_end_date 이 입력된 리스트를 가져온다.
	 * 
	 * 준비물1 : t_work_orders_items.actual_start_date ~ actual_end_date 기간 중 비지니스 데이 만
	 * 가져오기 준비물2 : t_work_orders_items.product_lines_idx 에 연관있는 직원들을 가져온다.
	 * 
	 * >> 가공한다.
	 * 
	 * t_worker_schedule 에 입력 : shift_type = 풀타임 근무 값 employee_id = 위의 준비물2에 해당하는 직원
	 * id location_idx = 직원들의 공통분모 start_date = 준비물1에서 계산한 비지니스 일자
	 */

	// 비즈니스 데이 생성
	private List<LocalDate> getBusinessDays(String startDate, String endDate) {
		// 날짜 포맷을 맞추기 위해 DateTimeFormatter 사용
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		// 문자열을 LocalDate로 변환 (시작일과 종료일)
		LocalDate start = LocalDateTime.parse(startDate, formatter).toLocalDate();
		LocalDate end = LocalDateTime.parse(endDate, formatter).toLocalDate();

		List<LocalDate> businessDays = new ArrayList<>();

		// 주말을 제외한 날짜 계산 (토요일, 일요일 제외)
		while (!start.isAfter(end)) {
			DayOfWeek day = start.getDayOfWeek();
			// 주말 제외 (주말은 비지니스 데이가 아님)
			if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
				businessDays.add(start);
			}
			start = start.plusDays(1); // 하루씩 증가
		}

		return businessDays;
	}

	// 근무 일정 생성
	
	@Transactional
	public void generateWorkerScheduleList() {
	    // 작업 아이템 목록 조회 (진행 상태인 아이템만)
	    List<WorkOrdersItemsDTO> workItems = workScheduleAuditingMapper.selectActiveWorkItemsWithDates();
	    // 근무 가능한 직원들 조회
	    List<EmployeeDTO> employees = workScheduleAuditingMapper.selectAvailableEmployees();

	    List<WorkerScheduleDTO> scheduleList = new ArrayList<>();

	    // 각 작업 아이템에 대해 비지니스 데이 리스트를 가져옴
	    for (WorkOrdersItemsDTO item : workItems) {
	        // actual_start_date ~ actual_end_date 기간 중 비지니스 데이만 추출
	        List<LocalDate> businessDays = getBusinessDays(item.getActualStartDate(), item.getActualEndDate());

	        // 각 직원들에 대해
	        for (EmployeeDTO employee : employees) {
	            String locationIdx = null;
	            // 직원의 부서 코드에 따른 생산공장 선택
	            if ("4".equals(employee.getSubDepartmentCode())) {
	                locationIdx = "7";  // 생산공장 1 (하드코딩된 값)
	            } else if ("5".equals(employee.getSubDepartmentCode())) {
	                locationIdx = "8";  // 생산공장 2 (하드코딩된 값)
	            } else {
	                // 생산팀 1팀, 2팀이 아닌 직원은 건너뜀
	                continue;
	            }

	            // 각 비즈니스 데이마다 근무 일정 생성
	            for (LocalDate day : businessDays) {
	                WorkerScheduleDTO dto = WorkerScheduleDTO.builder()
	                    .employeeId(employee.getId())
	                    .shiftType("6") // 풀타임 근무 (하드코딩된 값)
	                    .locationIdx(locationIdx) // 생산공장 1 혹은 2 (하드코딩된 값)
	                    .startDate(day.toString()) // 비지니스 일자
	                    .endDate(day.toString()) // 종료일자도 비지니스 일자와 동일
	                    .memo("자동 생성된 근무일정") // 일정 메모
	                    .isDeleted("N") // 삭제되지 않은 상태
	                    .regId("system") // 시스템 ID
	                    .build();

	                // 생성된 일정 리스트에 추가
	                scheduleList.add(dto);
	            }
	        }
	    }

	    // db insert
	    if (!scheduleList.isEmpty()) {
	        saveWorkerSchedules(scheduleList);
	    }
	    // db update
	    updateWorkOrdersItems(workItems);
	}

	// 근무 일정 저장
	public void saveWorkerSchedules(List<WorkerScheduleDTO> workerScheduleDTO) {
		workScheduleAuditingMapper.insertWorkerSchedules(workerScheduleDTO);
	}
	// 근무 일정 업데이트
	public void updateWorkOrdersItems(List<WorkOrdersItemsDTO> workItems) {
		// 각 작업 아이템에 대해 비지니스 데이 리스트를 가져옴
	    for (WorkOrdersItemsDTO item : workItems) {
	        item.setStatus("5"); // 5: REQUESTED 배치작업_요청됨 -> 상태 변경
	        workScheduleAuditingMapper.updateWorkOrdersItems(item);
	    }
	}

	public void startWorkSchedule(String taskName) {
		LogUtil.logStart(log);

		taskReportRegistryService.markRunning(taskName);

		try {
			taskReportRegistryService.updateReport(taskName, "Running");

			generateWorkerScheduleList();
			LogUtil.logDetail(log, "[{}] Health check running at {}", taskName, LocalDateTime.now());
			taskReportRegistryService.markSuccess(taskName);
		} catch (RuntimeException e) {
			LogUtil.error(log, "[{}] Health check failed: {}", taskName, e.getMessage(), e);

			taskReportRegistryService.markFail(taskName);
		}
	}
}
