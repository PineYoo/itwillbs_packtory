package kr.co.itwillbs.de.sample;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import kr.co.itwillbs.de.human.dto.EmployeeDTO;
import kr.co.itwillbs.de.mes.dto.WorkerScheduleDTO;

public class WorkSchedule {

	public static void main(String[] args) {

		/**
		 * 
		 * workOrdersItems 에서 actual_start_date, actual_end_date 가져와서 그 사이에 있는 토, 일(공휴일)
		 * 제외한 비지니스 데이(일하는 날)                   
		 * 
		 * EmployeeDTO 조회해서 가져와서 각각 product_lines_idx = 1(생산1팀) product_lines_idx =
		 * 2(생산2팀) 배치한다
		 *
		 * t_workorders_schedule 에 있는 shift_type employee_id location_idx start_date
		 * 데이터를 각각 채워넣는다
		 * 
		 */

		// ================================================================================

		// 1.EmpDTOList 생성
		List<EmployeeDTO> employees = List.of(
				EmployeeDTO.builder().id("E001").departmentCode("5").subDepartmentCode("4").build(), // 생산 1팀
				EmployeeDTO.builder().id("E002").departmentCode("5").subDepartmentCode("4").build(), // 생산 1팀
				EmployeeDTO.builder().id("E003").departmentCode("5").subDepartmentCode("5").build(), // 생산 2팀
				EmployeeDTO.builder().id("E004").departmentCode("5").subDepartmentCode("5").build(), // 생산 2팀
				EmployeeDTO.builder().id("E999").departmentCode("6").subDepartmentCode("7").build() // 제외 대상
		);

		// 2. 생산부 소속 직원만 필터링 (departmentCode = "5")
		List<EmployeeDTO> productionEmployees = employees.stream().filter(e -> "5".equals(e.getDepartmentCode()))
				.collect(Collectors.toList());

		// 3. 생산부 소속 직원만 필터링 (departmentCode = "5")
		List<EmployeeDTO> team1 = productionEmployees.stream().filter(e -> "4".equals(e.getSubDepartmentCode()))
				.collect(Collectors.toList());

		List<EmployeeDTO> team2 = productionEmployees.stream().filter(e -> "5".equals(e.getSubDepartmentCode()))
				.collect(Collectors.toList());

		// 4. 결과 출력
		System.out.println("생산1팀:");
		team1.forEach(e -> System.out.println("- " + e.getId()));

		System.out.println("생산2팀:");
		team2.forEach(e -> System.out.println("- " + e.getId()));

		// ================================================================================

		// 비지니스데이
		LocalDate startDate = LocalDate.of(2025, 4, 29);
		LocalDate endDate = LocalDate.of(2025, 5, 7);

		List<LocalDate> businessDays = new ArrayList<>();
		LocalDate date = startDate;
		while (!date.isAfter(endDate)) {
			DayOfWeek day = date.getDayOfWeek();
			if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
				businessDays.add(date);
			}
			date = date.plusDays(1);
		}
		System.out.println("비지니스 데이:");
		businessDays.forEach(d -> System.out.println("- " + d));

		// ================================================================================

		// 스케줄 리스트 생성
		List<WorkerScheduleDTO> scheduleList = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		String[] shifts = { "A", "B" };

		// 생산1팀
		int shiftIndex = 0;
		for (EmployeeDTO emp : team1) {
			for (LocalDate day : businessDays) {
				WorkerScheduleDTO schedule = WorkerScheduleDTO.builder().employeeId(emp.getId())
						.shiftType(shifts[shiftIndex % 2]).locationIdx("1").startDate(day.format(formatter)).build();

				scheduleList.add(schedule);
				shiftIndex++;
			}
		}

		// 생산 2팀
		shiftIndex = 0;
		for (EmployeeDTO emp : team2) {
			for (LocalDate day : businessDays) {
				WorkerScheduleDTO schedule = WorkerScheduleDTO.builder().employeeId(emp.getId())
						.shiftType(shifts[shiftIndex % 2]).locationIdx("2").startDate(day.format(formatter)).build();

				scheduleList.add(schedule);
				shiftIndex++;
			}
		}

		// 확인 출력
		scheduleList.forEach(System.out::println);
	}
}
