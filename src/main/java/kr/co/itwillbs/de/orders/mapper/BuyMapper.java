package kr.co.itwillbs.de.orders.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jakarta.validation.Valid;
import kr.co.itwillbs.de.orders.dto.OrderDTO;
import kr.co.itwillbs.de.orders.dto.OrderDetailDTO;

@Mapper
public interface BuyMapper {

	// 전체 수주 관리 목록 조회
    List<HashMap<String, Object>> getBuyList(String code);
    
	// 검색 조건에 맞는 수주 관리 목록 리스트 조회
    List<HashMap<String, Object>> getSearchBuy(@Param("orderStatus") String orderStatus, 
    											@Param("searchKeyword") String searchKeyword, 
    											@Param("startDate") String startDate, 
    											@Param("endDate") String endDate);
	
	// 주문서 등록
//	void insertOrder(@Valid OrderDTO orderDTO);
//	void insertOrderDetail(OrderDetailDTO orderDetailDTO);
//
//	// 주문서 상세 정보 조회
//	HashMap<String, Object> getOrder(String documentNumber);
//
//	// 주문 정보 수정
//	void updateOrder(OrderDTO orderDTO);


	
}
