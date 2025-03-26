package kr.co.itwillbs.de.orders.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.orders.dto.OrderDTO;
import kr.co.itwillbs.de.orders.mapper.SellMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SellService {
	@Autowired
	private SellMapper sellMapper;


	// 전체 수주 관리 목록 조회 요청
//	public List<OrderDTO> getSellList() {
//		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
//		return sellMapper.getSellList();
//	}
	public List<HashMap<String, Object>> getSellList() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return sellMapper.getSellList();
	}
	
	
}
