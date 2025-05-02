package kr.co.itwillbs.de.mes.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
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
public class ProductDTO {
	private Long idx; // 인덱스 (PK)

	@NotBlank(message = "이름은 필수 입력 값입니다.")
	private String name;
	private String code; 
	private String spec;

	@PositiveOrZero(message = "가격은 0원 이상만 입력 가능합니다.")
	private Integer price; 
	private String expiryDate;
	private String storageCondition;
	private String status;
	private String statusName;

	private String isDeleted; 

	private String regId; 
	private LocalDateTime regDate; 
	private String modId;
	private LocalDateTime modDate;
	
	// 페이징 변수들
	private int totalCount;
	private int rowAsc;
	private int rowNum;
}
