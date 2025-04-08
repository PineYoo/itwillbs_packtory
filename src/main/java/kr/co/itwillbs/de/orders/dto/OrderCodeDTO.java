package kr.co.itwillbs.de.orders.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OrderCodeDTO {
    private String code; 	// 코드
    private String name; 	// 코드명(또는 이름)
    private String phone;	// 전화번호
}
