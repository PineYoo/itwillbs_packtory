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
public class QcStandardDTO {

	private Long idx;
	private String type;
	
	@NotBlank(message = "품질기준 이름은 필수 입력 값입니다.")
	private String name;
	
	private String unit;
	private String targetValue;
	private String minValue;
	private String maxValue;
	private String memo;
	private String isDeleted;
	private String regId;
	private String regDate;
	private String modId;
	private String modDate;
	
	// 페이징 변수들
	private int totalCount;
	private int rowAsc;
	private int rowNum;
}

