package kr.co.itwillbs.de.common.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.common.vo.TaskReportDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TaskReportRegistryService {

	private final Map<String, TaskReportDTO> reports = new ConcurrentHashMap<>();

	public void initializeReport(String taskName, String status) {
		LogUtil.logStart(log);

		reports.put(taskName, new TaskReportDTO(taskName, null, status));
	}

	public void markRunning(String taskName) {
		LogUtil.logStart(log);

		TaskReportDTO report = reports.get(taskName);
		if (report != null) {
			report.running();
		} else {
			reports.put(taskName, new TaskReportDTO(taskName, LocalDateTime.now(), "Running"));
			reports.get(taskName).running();
		}
	}

	public void markSuccess(String taskName) {
		LogUtil.logStart(log);

		TaskReportDTO report = reports.get(taskName);
		if (report != null) {
			report.success();
		} else {
			reports.put(taskName, new TaskReportDTO(taskName, LocalDateTime.now(), "Success"));
			reports.get(taskName).success();
		}
	}

	public void markFail(String taskName) {
		LogUtil.logStart(log);

		TaskReportDTO report = reports.get(taskName);
		if (report != null) {
			report.fail();
		} else {
			reports.put(taskName, new TaskReportDTO(taskName, LocalDateTime.now(), "Fail"));
			reports.get(taskName).fail();
		}
	}

	public void updateReport(String taskName, String status) {
		LogUtil.logStart(log);

		TaskReportDTO report = reports.get(taskName);
		if (report != null) {
			if ("Running".equals(status)) {
				report.running();
			} else if ("Success".equals(status)) {
				report.success();
			} else if ("Fail".equals(status)) {
				report.fail();
			}
		} else {
			reports.put(taskName, new TaskReportDTO(taskName, LocalDateTime.now(), status));
		}
	}

	public Map<String, TaskReportDTO> getAllReports() {
		LogUtil.logStart(log);

		return reports;
	}

	public TaskReportDTO getReport(String taskName) {
		LogUtil.logStart(log);
		
		return reports.get(taskName);
	}
}
