package kr.co.itwillbs.de.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * <pre>
 * 스프링부트 Daemon 서비스 생성
 * > 목표 작업 리스트
 * 1. 매 00초 마다(1분) 전자 결재 상태를 감시
 * > 1-1. 전자 결재 카테고리 별 후 처리
 * 2. 매 00초 마다(1분) 하나는 심심하니까 log를 계속 찍는 서비스
 * 3. 01~04시 (서버 작업이 없는 시간)에 작동하는 통계 서비스
 * 
 * @Schedule 어노테이션은 스프링은 기본적으로 싱글톤이고, 스케줄 작업도 단일 스레드라서 직렬 처리, 그래서 미채택!
 * </pre>
 */
@EnableScheduling
@Configuration
public class SchedulerConfig{

	private static final int POOL_SIZE = 4;
	private static final int AWAIT_TERMINATAION_SEC = 10;
	private static final boolean WAIT_TASK_COMPLETE = true;
	private static final String THREAD_NAME_PREFIX = "DaemonTask-";
	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(POOL_SIZE); // 동시에 (n)개의 스케줄 작업 처리 가능
		scheduler.setThreadNamePrefix(THREAD_NAME_PREFIX); // 스레드 이름 prefix
		scheduler.setAwaitTerminationSeconds(AWAIT_TERMINATAION_SEC); // 종료시 (n) 대기시간 단위:초(sec)
		scheduler.setWaitForTasksToCompleteOnShutdown(WAIT_TASK_COMPLETE); // graceful shutdown 지원 이게 있음으로 이제 서버 끄고 켜는게 늦어져서 답답할껄?
		// 사용자들이 그레이스풀하게 셧다운 할거란걸 기대는 하지 않지만 ... 그래도 안전벨트는 착용하자
		//scheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // ExecutorService에 사용할 RejectedExecutionHandler를 설정, 기본값은 ExecutorService의 기본 중단 정책
		return scheduler;
	}

}
