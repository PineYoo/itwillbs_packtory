package kr.co.itwillbs.de.mes.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import kr.co.itwillbs.de.common.aop.annotation.RequiredSessionIds;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@RequiredSessionIds(fields = { "regId", "modId" })
public class LocationInfoDTO {

	private Long idx; // 테이블 인덱스
	
	@NotBlank(message = "타입은 필수 입력 값입니다.")
	private String type; // 타입
	private String typeName; // 타입 이름
	
	@NotBlank(message = "이름은 필수 입력 값입니다.")
	private String name; // 공장명
	
	@NotBlank(message = "우편번호는 필수 입력 값입니다.")
	private String postcode; // 우편번호
	
	@NotBlank(message = "주소정보는 필수 입력 값입니다.")
	private String address1; // 주소1
	
	@NotBlank(message = "주소정보는 필수 입력 값입니다.")
	private String address2; // 주소2
	
	private String memo; // 메모
	private String isDeleted; // 삭제유무
	
	private String regId; // 작성자
	private LocalDate regDate; // 작성일자시간
	private String modId; // 최종 작성자
	private LocalDate modDate; // 최종작성일자
	
	// 페이징 변수들
	private int totalCount;
	private int rowAsc;
	private int rowNum;
}
