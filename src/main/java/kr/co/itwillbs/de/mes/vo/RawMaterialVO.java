package kr.co.itwillbs.de.mes.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RawMaterialVO {
	
	private Long idx;				// 인덱스 (PK)
    private String bomIdx;			// BOM_idx
    private String clientIdx;		// 거래처_idx

    private String type;			// 타입
    private String name;			// 이름
    private Integer quantity;		// 개수
    private String unit;			// 단위

    private BigDecimal price;		// 가격

    private String expirationDate;	// 평균사용기한

    private String isDeleted;		// 삭제 여부

	private String regId; 			// 최초등록자
	private LocalDateTime regDate; 	// 최초등록일
	private String modId; 			// 최종수정자
	private LocalDateTime modDate; 	// 최종수정일
}
