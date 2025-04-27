package kr.co.itwillbs.de.groupware.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class ScheduleDTO {
	
	private Integer idx;
	
	@NotBlank(message = "제목은 필수 입력 값입니다.")
	private String title;
	@NotBlank(message = "내용은 필수 입력 값입니다.")
	private String content;
	@NotBlank(message = "시작일시는 필수 입력 값입니다.")
	private LocalDateTime startDatetime;
	@NotBlank(message = "종료일시는 필수 입력 값입니다.")
	private LocalDateTime endDatetime;
	
	private boolean allDay;
	private String backgroundColor;
	private String borderColor;
	private String textColor;
	private String departmentCode;
	private String subDepartmentCode;
	private String isDeleted;
	private String regId;
	private String regDate;
	private String modId;
	private String modDate;
}
