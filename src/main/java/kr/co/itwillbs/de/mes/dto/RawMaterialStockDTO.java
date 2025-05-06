package kr.co.itwillbs.de.mes.dto;

import java.math.BigDecimal;

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
public class RawMaterialStockDTO {

	private Long idx;
	
	private String name;
	private String type;
	private String typeName;
	private String quantity;
	private BigDecimal requiredQuantity;
	private BigDecimal stock;
	private BigDecimal stockGap;
	private String unit;
	private String unitName;

	private String location;
	private String locationName;
	
	private String stockStatus;
	
	private String isDeleted;
	private String regId;
	private String regDate;
	private String modId;
	private String modDate;
}
