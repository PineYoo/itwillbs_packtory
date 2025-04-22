package kr.co.itwillbs.de.common.vo;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TaskReportDTO {

	private String taskName;
	private LocalDateTime lastRunTime;
	private String status; // Success || Fail || Running || Standby
	private int failCount; // failCount
	private String threadName;
//	private ScheduledFuture<?> future;

	public TaskReportDTO(String taskName, LocalDateTime lastRunTime, String status) {
		this.taskName = taskName;
		this.lastRunTime = lastRunTime;
		this.status = status;
		this.failCount = 0;
	}

	public void running() {
		this.threadName = Thread.currentThread().getName();
		this.status = "Running";
		this.lastRunTime = LocalDateTime.now();
	}

	public void fail() {
		this.threadName = Thread.currentThread().getName();
		this.status = "Fail";
		this.failCount++;
		this.lastRunTime = LocalDateTime.now();
	}

	public void success() {
		this.threadName = Thread.currentThread().getName();
		this.status = "Success";
		this.failCount = 0;
		this.lastRunTime = LocalDateTime.now();
	}

	public void standby() {
		this.threadName = Thread.currentThread().getName();
		this.status = "Standby";
		this.lastRunTime = LocalDateTime.now();
	}

}
