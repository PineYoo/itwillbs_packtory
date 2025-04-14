package kr.co.itwillbs.de.mes.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Setter
@Getter
@NoArgsConstructor
@ToString
public class ProductVO {
	private Long idx; 				// 인덱스 (PK)
	private String type; 			// 타입
	private String name; 			// 이름
	private String unit; 			// 단위
	private BigDecimal price; 		// 가격

	private String is_deleted; 		// 삭제여부

	private String regId; 			// 최초등록자
	private LocalDateTime regDate; 	// 최초등록일
	private String modId; 			// 최종수정자w
	private LocalDateTime modDate; 	// 최종수정일
}
