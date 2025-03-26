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
    private OrderDTO orderDTO;
    private OrderDetailDTO orderDetailDTO;
    private ClientDTO clientDTO;
    private ClientInfoDTO clientInfoDTO;
    
}
