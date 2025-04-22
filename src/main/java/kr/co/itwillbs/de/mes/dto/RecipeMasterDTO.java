package kr.co.itwillbs.de.mes.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import kr.co.itwillbs.de.common.aop.annotation.RequiredSessionIds;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@RequiredSessionIds(fields = {"regId", "modId"})
public class RecipeMasterDTO {

	private Long idx;				// 테이블 인덱스
	private String productIdx;		// 상품_idx
	private String name;			// 이름
	private String version;			// 버전
	private String batchSize;		// 레시피_단위
	private String status;			// 상태
	private String approvalStatus;	// 결재_상태
	private LocalDate validFrom;	// 유효기간 시작일
	private LocalDate validTo;		// 유효기간 종료일
	
	private String isDeleted;		// 삭제여부
	private String regId;			// 작성자
	private LocalDateTime regDate;	// 작성일시
	private String modId;			// 최종작성자
	private LocalDateTime modDate;	// 최종작성일시
	
	
	private String productName;		// 상품명

}
