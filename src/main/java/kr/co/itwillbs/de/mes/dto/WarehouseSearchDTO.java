package kr.co.itwillbs.de.mes.dto;

import kr.co.itwillbs.de.common.vo.PageDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class WarehouseSearchDTO {
    private String name;						// 창고 이름
    private String directionCode;               // 입출고 상태
    
    private String productName;                 // 상품 이름
    private String productIdx;                  // 상품 인덱스
    
    private String startManufacturingDate;   	// 제조일자 시작
    private String endManufacturingDate;     	// 제조일자 종료
    
    private Integer minQuantity;                // 최소 총개수
    private Integer maxQuantity;                // 최대 총개수
    
    private PageDTO pageDTO = new PageDTO();  	// 페이징 정보
}
