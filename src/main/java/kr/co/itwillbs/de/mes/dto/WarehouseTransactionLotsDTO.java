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
public class WarehouseTransactionLotsDTO {

	private Long idx; // 테이블 인덱스
	
	@NotBlank(message = "롯은 필수 입력 값입니다.")
	private String lotIdx; // 롯 인덱스
	
	@NotBlank(message = "트랜잭션은 필수 입력 값입니다.")
	private String transactionIdx; // 트랜잭션 인덱스
	
	private Integer quantity; // 개수
	private String unit; // 단위
	private String unitName; // 단위
	
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
