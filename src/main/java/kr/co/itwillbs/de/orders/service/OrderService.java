package kr.co.itwillbs.de.orders.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.orders.dto.ClientDTO;
import kr.co.itwillbs.de.orders.mapper.ClientMapper;
import lombok.extern.slf4j.Slf4j;

//	log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

@Slf4j
@Service
public class OrderService {
	
	@Autowired
	private ClientMapper clientMapper;
	
	/**
	 * 모든 거래처 목록 조회
	 * @return
	 */
	public List<ClientDTO> getClientList() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return clientMapper.getClientList();
	}

	/**
	 * 거래처 입력
	 * @param clientDTO 
	 */
	public int insertClient(ClientDTO clientDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return clientMapper.insertClient(clientDTO);
		
	}
	
}
