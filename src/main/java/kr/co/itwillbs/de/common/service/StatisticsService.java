package kr.co.itwillbs.de.common.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.mapper.StatisticsMapper;
import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.groupware.dto.ApprovalDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StatisticsService {

	private final StatisticsMapper statisticsMapper;
	private final TaskReportRegistryService taskReportRegistryService;

	public StatisticsService(StatisticsMapper statisticsMapper, TaskReportRegistryService taskReportRegistryService) {
		this.statisticsMapper = statisticsMapper;
		this.taskReportRegistryService = taskReportRegistryService;
	}

	@Transactional
	public void generateStatistics() {
		LogUtil.logStart(log);
		String taskName = "statisticsTask";

		try {
			// 🏁 Task 시작 표시
			taskReportRegistryService.markRunning(taskName);

			// 이제 여기 통계 작업을 할 매퍼를 넣으면 되려나?
			String dbTime = statisticsMapper.getSelectNow();

			// 🏆 성공 표시
			taskReportRegistryService.markSuccess(taskName);
			LogUtil.logDetail(log, "[{}] generateStatistics success! / DB Time: {}", taskName, dbTime);
		} catch (RuntimeException e) {
			LogUtil.error(log, "[{}] generateStatistics failed! : {}", taskName, e.getMessage());
			// 💥 실패 표시
			taskReportRegistryService.markFail(taskName);
		}
	}
	
	
	/**
	 * 
	 */
	@Transactional
    public void processTodayApprovedDocs() {
		LogUtil.logStart(log);
		String taskName = "approvedTask";
		
		try {
			// 🏁 Task 시작 표시
			taskReportRegistryService.markRunning(taskName);
			
			// 오늘 승인 완료된 결재 조회
			List<ApprovalDTO> approvedList = statisticsMapper.getSelectApprovedToday();

			// 조회된 결재 관련 로직 처리
			for (ApprovalDTO approved : approvedList) {
				// 문서타입에 따른 결재처리
				// => 1: 품의서, 2: 기안서, 3:지출결의서, 4: 휴가서, 5:작업지시서, 6:출고요청서, 7: 레시피결재
				if (approved.getApprovalType().equals("4")) {	// 휴가서 일 경우
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

					String employeeId = approved.getDrafterId();
					LocalDate startDate = LocalDate.parse(approved.getEventStartDate(), formatter);	// DTO에서 String으로 담아오므로 LocalDate 으로 변환 필요
					LocalDate endDate = LocalDate.parse(approved.getEventEndDate(), formatter);

					for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
						statisticsMapper.insertApprovedVacation(employeeId, date);
					}
				} else if (approved.getApprovalType().equals("7")) {	// 레시피 결재 일 경우
					statisticsMapper.updateApprovedRecipe(approved.getRecipeMasterIdx());
				}
			}
			
			// 🏆 성공 표시
			taskReportRegistryService.markSuccess(taskName);
			LogUtil.logDetail(log, "[{}] generateStatistics success! {}", taskName);
		} catch (RuntimeException e) {
			LogUtil.error(log, "[{}] generateStatistics failed! : {}", taskName, e.getMessage());
			// 💥 실패 표시
			taskReportRegistryService.markFail(taskName);
		}
		

    }
}
