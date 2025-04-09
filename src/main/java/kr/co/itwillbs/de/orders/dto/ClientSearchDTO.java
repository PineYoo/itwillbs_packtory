package kr.co.itwillbs.de.orders.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientSearchDTO {
	private String companyNumber;
	private String companyName;
	private String ownerName;
	private String isDeleted;
}
