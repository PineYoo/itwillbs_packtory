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
import kr.co.itwillbs.de.orders.mapper.SellMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SellService {
	@Autowired
	private SellMapper sellMapper;
	
	@Autowired
	private CommonService commonService;


	// 전체 수주 관리 목록 조회 요청
	public List<HashMap<String, Object>> getSellList() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

        return sellMapper.getSellList();
    }

	// 검색 조건에 맞는 수주 관리 목록 리스트 조회 요청
	public List<HashMap<String, Object>> getSearchSell(String orderStatus, String searchKeyword, String startDate,
			String endDate) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return sellMapper.getSearchSell(orderStatus, searchKeyword, startDate, endDate);
	}
	// ------------------------------------------------------------------------------------
	// 주문서 등록 요청
	public void registerOrder(@Valid OrderDTO orderDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// MySQL DB에서 시퀀스 가져와서 document_number에 넣기
		orderDTO.setDocumentNumber(commonService.getSeqOrderNumberfromMySQL());
		sellMapper.insertOrder(orderDTO);
	}


	public void registerOrderDetail(OrderDetailDTO orderDetailDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		sellMapper.insertOrderDetail(orderDetailDTO);
		
	}

	// ------------------------------------------------------------------------------------
	// 주문서 상세 정보 조회 요청
	public HashMap<String, Object> getOrder(String documentNumber) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return sellMapper.getOrder(documentNumber);
	}

	// 주문 정보 수정 요청
	public void modifyOrder(OrderDTO orderDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		sellMapper.updateOrder(orderDTO);
	}
	
	
	/**
	 * 수주 정보 조건 검색 가져오기
	 * @param orderSearchDTO
	 * @return
	 */
	public List<OrderDTO> getOrdersInTradeSell(OrderSearchDTO orderSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return sellMapper.getOrdersInTradeSell(orderSearchDTO);
	}

	/**
	 * 수주 상세 정보 가져오기
	 * @param documentNumber
	 * @return
	 */
	public OrderDTO getOrderByDocumentNumber(String documentNumber) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return sellMapper.getOrderByDocumentNumber(documentNumber);
	}

	// ===============================================================================================
	// ----------------------------------- 여기부터 발주 ---------------------------------------------
	// 전체 발주 관리 목록 조회 요청
	public List<HashMap<String, Object>> getBuyList(String code) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

        return sellMapper.getBuyList(code);
    }

	/**
	 * 발주 정보 조건 검색 가져오기
	 * @param orderSearchDTO
	 * @return
	 */
	public List<OrderDTO> getOrdersInTradeBuy(OrderSearchDTO orderSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return sellMapper.getOrdersInTradeBuy(orderSearchDTO);
	}
	
}
