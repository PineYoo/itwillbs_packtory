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
	
	//	거래처코드
	private String companyNumber;
	//	담당자
	private String clientEmployeeName;
	//	담장자 메모
	private String clientEmployeeMemo;
	//	전화1
	private String phone1;
	//	전화2
	private String phone2;
	
	@Builder
	public ClientInfoDTO(String companyNumber, String clientEmployeeName, String clientEmployeeMemo, String phone1,
			String phone2) {
		this.companyNumber = companyNumber;
		this.clientEmployeeName = clientEmployeeName;
		this.clientEmployeeMemo = clientEmployeeMemo;
		this.phone1 = phone1;
		this.phone2 = phone2;
	}
	
	
	
	
	
}


















