package kr.co.itwillbs.de.orders.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.orders.dto.ClientDTO;
import kr.co.itwillbs.de.orders.dto.OrderDTO;
import kr.co.itwillbs.de.orders.dto.OrderFormDTO;
import kr.co.itwillbs.de.orders.dto.OrderSearchDTO;

@Mapper
public interface OrderMapper {

	/**
	 * 페이징용 카운트
	 */
	int getOrderCountForPaging(OrderSearchDTO orderSearchDTO);
	
    /**
     * 수주/발주 정보 조건 검색 가져오기
     * SELECT FROM t_order inner join t_order_detail where orderSearchDTO
     * @param orderSearchDTO 리스트 화면 검색필드 DTO
     * @return List<OrderDTO>
     */
	List<OrderDTO> getOrderList(OrderSearchDTO orderSearchDTO);
	
    
    // -------------------------------------------------------------------
	/**
	 * 거래처 정보 조회(SELECT)
	 * @return List<ClientDTO>
	 */
	List<ClientDTO> getClientList();

	/**
	 * 수주/발주 주문 정보 등록(INSERT) >> orderDTO
	 * @param orderDTO
	 * @return 
	 */
	//void insertOrder(OrderDTO orderDTO);
	void insertOrder(OrderFormDTO orderFormDTO);
	
	/**
	 * 수주/발주 주문 정보 등록(INSERT) >> orderDetailDTO
	 * @param orderDetailDTO
	 * @return 
	 */
	//void insertOrderDetail(OrderDetailDTO orderDetailDTO);
	void insertOrderDetail(OrderFormDTO orderFormDTO);

	// -------------------------------------------------------------------
	/**
	 * SELECT FROM t_order inner join t_order_detail where documentNumber
	 * @param documentNumber
	 * @return OrderDTO
	 */
	OrderFormDTO getOrderByDocumentNumber(String documentNumber);
	
	@Deprecated
	OrderDTO oldgetOrderByDocumentNumber(String documentNumber);
	
	// -------------------------------------------------------------------
	/**
	 * 수주/발주 주문 정보 수정(UPDATE) >> orderDTO
	 * @param orderDTO
	 * @return 
	 */
	//void updateOrder(OrderDTO orderDTO);
	void updateOrder(OrderFormDTO orderFormDTO);
	
	/**
	 * 수주/발주 주문 정보 수정(UPDATE) >> orderDetailDTO
	 * @param orderDetailDTO
	 * @return 
	 */
	//void updateOrderDetail(OrderDetailDTO orderDetailDTO);
	void updateOrderDetail(OrderFormDTO orderFormDTO);

}
