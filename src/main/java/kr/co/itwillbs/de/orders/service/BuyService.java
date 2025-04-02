package kr.co.itwillbs.de.orders.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import kr.co.itwillbs.de.common.service.CommonService;
import kr.co.itwillbs.de.orders.dto.OrderDTO;
import kr.co.itwillbs.de.orders.dto.OrderDetailDTO;
import kr.co.itwillbs.de.orders.dto.OrderSearchDTO;
import kr.co.itwillbs.de.orders.mapper.BuyMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BuyService {
	@Autowired
	private BuyMapper buyMapper;
	
	@Autowired
	private CommonService commonService;


	// 전체 발주 관리 목록 조회 요청
	public List<HashMap<String, Object>> getBuyList(String code) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

        return buyMapper.getBuyList(code);
    }

	/**
	 * 발주 정보 조건 검색 가져오기
	 * @param orderSearchDTO
	 * @return
	 */
	public List<OrderDTO> getOrdersInTradeBuy(OrderSearchDTO orderSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return buyMapper.getOrdersInTradeBuy(orderSearchDTO);
	}

	// 검색 조건에 맞는 수주 관리 목록 리스트 조회 요청
//	public List<HashMap<String, Object>> getSearchBuy(String orderStatus, String searchKeyword, String startDate,
//			String endDate) {
//		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
//		return buyMapper.getSearchBuy(orderStatus, searchKeyword, startDate, endDate);
//	}
	// ------------------------------------------------------------------------------------
	// 주문서 등록 요청
//	public void registerOrder(@Valid OrderDTO orderDTO) {
//		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
//		
//		// MySQL DB에서 시퀀스 가져와서 document_number에 넣기
//		orderDTO.setDocumentNumber(commonService.getSeqOrderNumberfromMySQL());
//		buyMapper.insertOrder(orderDTO);
//	}
//
//
//	public void registerOrderDetail(OrderDetailDTO orderDetailDTO) {
//		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
//		buyMapper.insertOrderDetail(orderDetailDTO);
//		
//	}

	// ------------------------------------------------------------------------------------
	// 주문서 상세 정보 조회 요청
//	public HashMap<String, Object> getOrder(String documentNumber) {
//		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
//		return buyMapper.getOrder(documentNumber);
//	}

	// 주문 정보 수정 요청
//	public void modifyOrder(OrderDTO orderDTO) {
//		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
//		buyMapper.updateOrder(orderDTO);
//	}

	
	
}
