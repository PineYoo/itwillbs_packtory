package kr.co.itwillbs.de.orders.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.orders.dto.OrderDTO;
import kr.co.itwillbs.de.orders.dto.OrderFormDTO;
import kr.co.itwillbs.de.orders.dto.OrderItemsDTO;
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

	/**
	 * 수주/발주 주문 정보 등록(INSERT) >> orderItemsDTO
	 * @param orderItemsDTO
	 * @return 
	 */
	void insertOrderItems(OrderFormDTO orderFormDTO);
	
	// -------------------------------------------------------------------
	/**
	 * SELECT FROM t_order inner join t_order_detail where documentNumber
	 * @param documentNumber
	 * @return OrderDTO
	 */
	OrderFormDTO getOrderByDocumentNumber(String documentNumber);

	/**
	 * SELECT FROM t_order_items WHERE order_document_number
	 * @param documentNumber
	 * @return List<OrderItemsDTO>
	 */
	List<OrderItemsDTO> getOrderListByDocumentNumber(String documentNumber);
	
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

	/**
	 * 문서번호 일치하는 주문_아이템 삭제(DELETE)
	 * @param documentNumber
	 */
	void deleteOrderItemsByDocumentNumber(String documentNumber);

	/**
	 * <pre>
	 * [입고신청] 에서 사용
	 * select * from t_order_items where orderDocumentNumber for update
	 * 위의 쿼리로 select 구매 신청한 주문번호의 원자재, 부자재 등을 t_warehouse_transaction 에 입력전 선행함
	 * </pre>
	 * @param orderDocumentNumber
	 * @return
	 */
	List<OrderItemsDTO> getOrderItemsByDocumentNumberForUpodate(String orderDocumentNumber);


}
