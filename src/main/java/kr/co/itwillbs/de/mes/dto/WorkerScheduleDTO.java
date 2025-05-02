package kr.co.itwillbs.de.mes.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import kr.co.itwillbs.de.common.aop.annotation.RequiredSessionIds;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@RequiredSessionIds(fields = { "regId", "modId" })
public class WorkerScheduleDTO {

	private Long idx; // 테이블 인덱스

	@NotBlank(message = "사원번호는 필수 입력 값입니다.")
	private String employeeId; // 사원번호

	@NotBlank(message = "근무타입은 필수 입력 값입니다.")
	private String shiftType; // 근무타입
	private String shiftTypeName; // 근무타입

	@NotBlank(message = "장소정보는 필수 입력 값입니다.")
	private String locationIdx; // 장소 인덱스
	private String locationName; // 장소 이름

	private String startDate; // 시작일자
	private String endDate; // 종료일자

	private String memo; // 메모

	@Builder.Default
	private String isDeleted = "N"; // 삭제유무

	private String regId; // 작성자
	private LocalDate regDate; // 작성일자시간
	private String modId; // 최종 작성자
	private LocalDate modDate; // 최종작성일자

	// 페이징 변수들
	private int totalCount;
	private int rowAsc;
	private int rowNum;
}
