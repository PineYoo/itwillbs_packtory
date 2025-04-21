package kr.co.itwillbs.de.common.controller;

import java.util.Collection;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.itwillbs.de.common.service.TaskReportRegistryService;
import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.common.vo.TaskReportDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/common/task", name = "스케쥴러 관리")
public class TaskReportController {

	private final TaskReportRegistryService taskReportRegistryService;

	public TaskReportController(TaskReportRegistryService taskReportRegistryService) {
		this.taskReportRegistryService = taskReportRegistryService;
	}

	private final String VIEW_PATH = "/common/task"; // 이 클래스 내에서 사용 할 뷰 경로

	@GetMapping("/monitoring")
	public String taskReport(Model model) {
		LogUtil.logStart(log);

		model.addAttribute("reports", taskReportRegistryService.getAllReports().values());
		return VIEW_PATH + "/monitoring";
	}

	@GetMapping("/report/api")
	@ResponseBody
	public Collection<TaskReportDTO> getReports() {
		LogUtil.logStart(log);
		return taskReportRegistryService.getAllReports().values();
	}
}
