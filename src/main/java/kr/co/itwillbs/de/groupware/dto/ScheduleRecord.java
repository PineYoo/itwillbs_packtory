package kr.co.itwillbs.de.groupware.dto;

import java.time.LocalDateTime;

public record ScheduleRecord(
	Integer idx,
	String title,
	String content,
	LocalDateTime startDatetime,
	LocalDateTime endDatetime,
	boolean allDay,
	String backgroundColor,
	String borderColor,
	String textColor,
	String departmentCode,
	String subDepartmentCode,
	String isDeleted,
	String regId,
	String regDate,
	String modId,
	String modDate
) {}
