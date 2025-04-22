package kr.co.itwillbs.de.common.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.common.vo.TaskReportDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TaskHealthMonitorService {

	private final TaskReportRegistryService taskReportRegistryService;

	public TaskHealthMonitorService(TaskReportRegistryService taskReportRegistryService) {
		this.taskReportRegistryService = taskReportRegistryService;
	}

	/**
	 * (테스트값)모니터링 30초 마다 전체 스캔 TaskPool
	 */
//	@Scheduled(fixedDelay = 30000) // unit milliseconds sec * 1000
	public void monitorTasks() {
		LogUtil.logStart(log);

		LocalDateTime now = LocalDateTime.now();
		Map<String, TaskReportDTO> reports = taskReportRegistryService.getAllReports();

		for (TaskReportDTO report : reports.values()) {
			if (report.getLastRunTime() == null) {
				// 1. 아직 한번도 실행된 적 없는 경우
				LogUtil.warn(log, "[HealthMonitor] Task [{}] has never run.", report.getTaskName());
				continue;
			}

			Duration duration = Duration.between(report.getLastRunTime(), now);
			long seconds = duration.getSeconds();

			if ("Running".equals(report.getStatus())) {
				// 2. 러닝 상태가 비정상적으로 오래 지속되는 경우 체크 (ex: 3분 이상)
				if (seconds > 180) {
					LogUtil.warn(log, "[HealthMonitor] Task [{}] is running too long! ({} seconds)", report.getTaskName(), seconds);
					// 여기서 알림/장애 처리도 가능
				}
			} else {
				// 3. 성공/실패 후 오래된 Task 감시 (선택 사항)
				if (seconds > 600) { // 10분 이상 업데이트 없는 Task는 이상할 수 있음
					LogUtil.warn(log, "[HealthMonitor] Task [{}] last updated {} seconds ago.", report.getTaskName(), seconds);
				}
			}
		}
	}

}
