package kr.co.itwillbs.de.orders.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OrderFormDTO {	// Wrapper DTO로 묶어버림
    private OrderDTO orderDTO;				// 주문DTO
    private OrderDetailDTO orderDetailDTO;	// 주문 상세DTO
    private OrderItemsDTO orderItemsDTO;	// 주문 아이템DTO
//    private ClientDTO clientDTO;			// 거래처 DTO
//    private ClientInfoDTO clientInfoDTO;	// 거래처 부가정보DTO
    
}
