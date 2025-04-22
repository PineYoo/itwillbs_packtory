package kr.co.itwillbs.de.mes.dto;

import kr.co.itwillbs.de.common.vo.PageDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class RawMaterialSearchDTO {
    
    private String type;			// 타입
    private String name;			// 이름
    
    private Long clientIdx;			// 거래처 idx
    private String clientName;		// 거래처명 (뷰 표시용)
    
    private Long bomIdx;            // BOM idx
    private String bomName;         // BOM명 (뷰 표시용)
    
    private Integer minQuantity;    // 최소 수량
    private Integer maxQuantity;    // 최대 수량
    
    private Integer minPrice;    // 최소 가격
    private Integer maxPrice;    // 최대 가격
    
    private PageDTO pageDTO = new PageDTO();  // 페이징 정보
}
