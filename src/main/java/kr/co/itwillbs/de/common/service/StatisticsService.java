package kr.co.itwillbs.de.common.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.mapper.StatisticsMapper;
import kr.co.itwillbs.de.common.util.LogUtil;
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
}
