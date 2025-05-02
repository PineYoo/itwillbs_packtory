package kr.co.itwillbs.de.mes.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QcRequiredSearchDTO {

	private String idx;
	private String status;
	private String materialName;
	private String qcName;
}
