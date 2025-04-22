package kr.co.itwillbs.de.common.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.common.util.LogUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HealthCheckService {

	private final TaskReportRegistryService taskReportRegistryService;

	public HealthCheckService(TaskReportRegistryService taskReportRegistryService) {
		this.taskReportRegistryService = taskReportRegistryService;
	}

	public void check(String taskName) {
		LogUtil.logStart(log);

		taskReportRegistryService.markRunning(taskName);

		try {
			taskReportRegistryService.updateReport(taskName, "Running");

			LogUtil.logDetail(log, "[{}] Health check running at {}", taskName, LocalDateTime.now());

			taskReportRegistryService.markSuccess(taskName);
		} catch (RuntimeException e) {
			LogUtil.error(log, "[{}] Health check failed: {}", taskName, e.getMessage(), e);
			
			taskReportRegistryService.markFail(taskName);
		}
	}

}
