package kr.co.itwillbs.de.orders.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import kr.co.itwillbs.de.common.service.CommonService;
import kr.co.itwillbs.de.orders.dto.OrderDTO;
import kr.co.itwillbs.de.orders.dto.OrderDetailDTO;
import kr.co.itwillbs.de.orders.dto.OrderFormDTO;
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
//	public List<OrderDTO> getSellList() {
//		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
//		return sellMapper.getSellList();
//	}
	public List<HashMap<String, Object>> getSellList() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return sellMapper.getSellList();
	}

	// ------------------------------------------------------------------------------------
	// 주문서 등록 요청
	public void registerOrder(@Valid OrderDTO orderDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
//		commonService.cr
		sellMapper.insertOrder(orderDTO);
	}


	public void registerOrderDetail(OrderDetailDTO orderDetailDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		sellMapper.insertOrderDetail(orderDetailDTO);
		
	}
	
	
}
