package kr.co.itwillbs.de.mes.dto;

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
public class QcLogDTO {

	private Long idx; // 테이블 인덱스
	
	private String lotsIdx;
	@NotBlank(message = "품질IDX 는 필수 입력 값입니다.")
	private String qcIdx; // 품질IDX
	private String qcStandardName; // 품질기준 이름
	
	@NotBlank(message = "측정결과는 필수 입력 값입니다.")
	private String qcResult; // 측정결과
	private String qcResultName;
	
	@NotBlank(message = "개수는 필수 입력 값입니다.")
	private Integer quantity;
	
	private String value; // 측정값
	
//	@NotBlank(message = "단위는 필수 입력 값입니다.")
	private String unit; // 단위
	private String unitName;
	
	private String memo; // 메모

	private String isDeleted; // 삭제유무

	private String regId; // 작성자 = 작업자
	private String regDate; // 작성일자시간
	private String modId; // 최종작성자
	private String modDate; // 최종작성일자
	
	// 페이징 변수들
	private int totalCount;
	private int rowAsc;
	private int rowNum;
}
