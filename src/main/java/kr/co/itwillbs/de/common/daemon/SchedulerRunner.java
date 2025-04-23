package kr.co.itwillbs.de.common.daemon;

import java.time.Duration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import kr.co.itwillbs.de.common.service.HealthCheckService;
import kr.co.itwillbs.de.common.service.StatisticsService;
import kr.co.itwillbs.de.common.service.TaskReportRegistryService;
import kr.co.itwillbs.de.groupware.service.ApprovalService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SchedulerRunner implements CommandLineRunner {

	private final ThreadPoolTaskScheduler scheduler;
	private final HealthCheckService healthCheckService;
	private final StatisticsService statisticsService;
	private final TaskReportRegistryService taskReportRegistryService;
//	private final ApprovalService approvalService;

	

	public SchedulerRunner(ThreadPoolTaskScheduler scheduler, HealthCheckService healthCheckService,
			StatisticsService statisticsService, TaskReportRegistryService taskReportRegistryService) {
		this.scheduler = scheduler;
		this.healthCheckService = healthCheckService;
		this.statisticsService = statisticsService;
		this.taskReportRegistryService= taskReportRegistryService;
	}

	@Override
	public void run(String... args) {
		// sample task 등록
//		registerTaskForSec("every45SecHealthCheck", 45);
//		registerTaskForMin("E-ApprovalMonitor", 1);
//		// cron 형식 작업
		registerStatisticsJob();
		registerApprovalProcessingJob(); // 결재 처리 데몬 등록
	}

	@SuppressWarnings("unused")
	private void registerTaskForSec(String taskName, long periodSec) {
		
		Runnable task = () -> {
			taskReportRegistryService.updateReport(taskName, "Running");
			try {
				healthCheckService.check(taskName);
				taskReportRegistryService.updateReport(taskName, "Success");
			} catch (Exception e) {
				taskReportRegistryService.updateReport(taskName, "Fail");
			}
		};
		
		//ScheduledFuture<?> scheduledFuture = scheduler.scheduleAtFixedRate(task, Duration.ofSeconds(periodSec));
		scheduler.scheduleAtFixedRate(task, Duration.ofSeconds(periodSec));
		taskReportRegistryService.initializeReport(taskName, "Standby");
		// TODO: scheduledFuture를 추후 모니터링/수동조작할 수 있도록 저장할 수도 있음
	}
	
	@SuppressWarnings("unused")
	private void registerTaskForMin(String taskName, long periodMin) {
		
		Runnable task = () -> {
			taskReportRegistryService.updateReport(taskName, "Running");
			try {
				healthCheckService.check(taskName);
				taskReportRegistryService.updateReport(taskName, "Success");
			} catch (Exception e) {
				taskReportRegistryService.updateReport(taskName, "Fail");
			}
		};
		
		//ScheduledFuture<?> scheduledFuture = scheduler.scheduleAtFixedRate(task, Duration.ofMinutes(periodMin));
		scheduler.scheduleAtFixedRate(task, Duration.ofMinutes(periodMin));
		taskReportRegistryService.initializeReport(taskName, "Standby");
		// TODO: scheduledFuture를 추후 모니터링/수동조작할 수 있도록 저장할 수도 있음
	}
	
//	private void registerTaskForMin(String taskName, long periodMin) {
//		
//		Runnable task = () -> healthCheckService.check(taskName);
//		ScheduledFuture<?> scheduledFuture = scheduler.scheduleAtFixedRate(task, Duration.ofMinutes(periodMin));
//		
//		// TODO: scheduledFuture를 추후 모니터링/수동조작할 수 있도록 저장할 수도 있음
//	}

	/**
	 * 통계성 데이터 작업할 서비스 <br>
	 * 우리 프로젝트에서는 근퇴 통계가 있음! <br>
	 * 공정 통계? 이런건 할 수 있을려나?
	 */
	private void registerStatisticsJob() {
		Runnable task = () -> statisticsService.generateStatistics();

		// String cronExpression = "0 0 2 * * *"; // 매일 02:00:00에 실행
		String cronExpression = "0 * * * * *"; // 매 분마다 시간 가져온 쿼리 수행!

		scheduler.schedule(task, new CronTrigger(cronExpression));
	}

	/**
	 * 결재 완료된 기안 데몬 작업
	 */
	private void registerApprovalProcessingJob() {
		Runnable task = () -> statisticsService.processTodayApprovedDocs();

		String cronExpression = "0 * * * * *"; // 매 분마다 시간 가져온 쿼리 수행!
//		String cronExpression = "0 0/5 * * * *"; // 5분마다 실행
		scheduler.schedule(task, new CronTrigger(cronExpression));
	}
}
