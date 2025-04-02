package kr.co.itwillbs.de.orders.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jakarta.validation.Valid;
import kr.co.itwillbs.de.orders.dto.OrderDTO;
import kr.co.itwillbs.de.orders.dto.OrderDetailDTO;
import kr.co.itwillbs.de.orders.dto.OrderSearchDTO;

@Mapper
public interface SellMapper {

	// 전체 수주 관리 목록 조회
    List<HashMap<String, Object>> getSellList();
    
    /**
     * SELECT FROM t_order inner join t_order_detail where orderSearchDTO
     * @param orderSearchDTO 리스트 화면 검색필드 DTO
     * @return
     */
    List<OrderDTO> getOrdersInTradeSell(OrderSearchDTO orderSearchDTO);
    
	// 검색 조건에 맞는 수주 관리 목록 리스트 조회
    List<HashMap<String, Object>> getSearchSell(@Param("orderStatus") String orderStatus, 
    											@Param("searchKeyword") String searchKeyword, 
    											@Param("startDate") String startDate, 
    											@Param("endDate") String endDate);
	
	// 주문서 등록
	void insertOrder(@Valid OrderDTO orderDTO);
	void insertOrderDetail(OrderDetailDTO orderDetailDTO);

	// 주문서 상세 정보 조회
	HashMap<String, Object> getOrder(String documentNumber);

	/**
	 * SELECT FROM t_order inner join t_order_detail where documentNumber
	 * @param documentNumber
	 * @return
	 */
	OrderDTO getOrderByDocumentNumber(String documentNumber);
	
	// 주문 정보 수정
	void updateOrder(OrderDTO orderDTO);




	
}
