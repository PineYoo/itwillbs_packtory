package kr.co.itwillbs.de.orders.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import jakarta.validation.Valid;
import kr.co.itwillbs.de.orders.dto.OrderDTO;
import kr.co.itwillbs.de.orders.dto.OrderDetailDTO;
import kr.co.itwillbs.de.orders.dto.OrderFormDTO;

@Mapper
public interface SellMapper {

	// 전체 수주 관리 목록 조회
//	List<OrderDTO> getSellList();
	List<HashMap<String, Object>> getSellList();
	
	// 주문서 등록
	void insertOrder(@Valid OrderDTO orderDTO);
	void insertOrderDetail(OrderDetailDTO orderDetailDTO);

	// 주문서 상세 정보 조회
	HashMap<String, Object> getOrder(String documentNumber);

	// 주문 정보 수정
	void updateOrder(OrderDTO orderDTO);

	
}
