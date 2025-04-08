package kr.co.itwillbs.de.commute.dto;

import java.time.LocalDateTime;

import kr.co.itwillbs.de.common.aop.annotation.RequiredSessionIds;
import kr.co.itwillbs.de.commute.constant.WorkStatusCode;
import kr.co.itwillbs.de.commute.entity.Commute;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// DTO 클래스 정의
// 계층간 데이터를 주고 받을 때 Entity 클래스 자체를 사용하지 않고
// 별도의 데이터 전송용 객체인 DTO 사용하는 것이 일반적이다.
// => 요청과 응답 객체 형태가 항상 엔티티와 같지 않기 때문에 계층간 데이터 이동에는 DTO 사용

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredSessionIds(fields = {"employeeId"})
public class CommuteDTO {
	
	private Long idx; // 테이블 인덱스
	private String employeeId; // 사원번호
//	private WorkStatusCode workStatusCode; // 근무상태코드
	private String workStatusCode; // 근무상태코드
	private LocalDateTime regDate; // 날짜 및 시간
	
	@Builder
//	public CommuteDTO(Long idx, String employeeId, WorkStatusCode workStatusCode, LocalDateTime regDate) {
//		this.idx = idx;
//		this.employeeId = employeeId;
//		this.workStatusCode = workStatusCode;
//		this.regDate = regDate;
//	}
	public CommuteDTO(Long idx, String employeeId, String workStatusCode, LocalDateTime regDate) {
		this.idx = idx;
		this.employeeId = employeeId;
		this.workStatusCode = workStatusCode;
		this.regDate = regDate;
	}
	
	
	// CommuteDTO -> Commute(엔티티)로 변환하는 toEntity() 메서드 정의
	public Commute toEntity() {
		return Commute.builder()
				.employeeId(employeeId)
				.workStatusCode(workStatusCode)
				.regDate(regDate)
				.build();
	}

}
