package kr.co.itwillbs.de.orders.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ClientInfoDTO {

	private int idx;
	
	private String businessNumber;			//	거래처코드
	private String clientEmployeeName;	//	담당자
	private String clientEmployeeMemo;	//	담장자 메모
	private String phone1;				//	전화1
	private String phone2;				//	전화2
	
	@Builder
	public ClientInfoDTO(String businessNumber, String clientEmployeeName, String clientEmployeeMemo, String phone1,
			String phone2) {
		this.businessNumber = businessNumber;
		this.clientEmployeeName = clientEmployeeName;
		this.clientEmployeeMemo = clientEmployeeMemo;
		this.phone1 = phone1;
		this.phone2 = phone2;
	}
	
	
	
	
	
}


















