package kr.co.itwillbs.de.mes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import kr.co.itwillbs.de.common.aop.annotation.RequiredSessionIds;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@RequiredSessionIds(fields = { "regId", "modId" })
public class ProductDTO {
	private Long idx; 				// 인덱스 (PK)
	private String type; 			// 타입
	private String name; 			// 이름
	private String unit; 			// 단위
	private BigDecimal price; 		// 가격

	private String is_deleted; 		// 삭제여부

	private String regId; 			// 최초등록자 ID
	private LocalDateTime regDate; 	// 최초등록일
	private String modId; 			// 최종수정자 ID
	private LocalDateTime modDate; 	// 최종수정일

}
