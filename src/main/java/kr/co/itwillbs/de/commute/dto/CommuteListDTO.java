package kr.co.itwillbs.de.commute.dto;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CommuteListDTO {	// Wrapper DTO로 묶어버림
	private Date workDate; 			// 날짜
	private String employeeId; 		// 사원번호
	private String department; 		// 부서
	private String position; 		// 직책
	private String name;	 		// 이름
	private LocalDateTime checkIn; 	// 출근시간
	private LocalDateTime checkOut; // 퇴근시간
	private String workStatus; 		// 근무상태
    
	@Builder
	public CommuteListDTO(String employeeId, Date workDate, LocalDateTime checkIn, LocalDateTime checkOut,
			String workStatus) {
		this.employeeId = employeeId;
		this.workDate = workDate;
		this.checkIn = checkIn;
		this.checkOut = checkOut;
		this.workStatus = workStatus;
	}
	
	
}
