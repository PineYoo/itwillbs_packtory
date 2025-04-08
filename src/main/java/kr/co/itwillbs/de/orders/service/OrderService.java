package kr.co.itwillbs.de.orders.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.orders.dto.ClientDTO;
import kr.co.itwillbs.de.orders.dto.ClientInfoDTO;
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
	 * 거래처 등록
	 * @param clientDTO 
	 */
	@LogExecution // 로그 남길 서비스
	public int insertClient(ClientDTO clientDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return clientMapper.insertClient(clientDTO);
	}

	/**
	 * 거래처 상세정보 요청
	 * @param businessNumber
	 * @return Map<String, Object>
	 */
	public Map<String, Object> getClient(String businessNumber) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return clientMapper.getClient(businessNumber);
	}

	/**
	 * 거래처 정보 수정
	 * @param clientDTO
	 */
	@LogExecution // 로그 남길 서비스
	public void updateClient(ClientDTO clientDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		clientMapper.updateClient(clientDTO);
	}

	/**
	 * 거래처_부가정보 수정
	 * @param clientInfoDTO
	 */
	@LogExecution // 로그 남길 서비스
	public void updateClientInfo(ClientInfoDTO clientInfoDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		if(clientMapper.getClientInfo(clientInfoDTO.getCompanyNumber()) > 0) { // 사업자 번호로 T_CLIENT_INFO 테이블 조회
			//	조회결과가 있을 시 UPDATE
			log.info(">>>>>>>>>>>>>>>>>>>>>>>>>updateClientInfo");
			clientMapper.updateClientInfo(clientInfoDTO);
		} else {
			//	조회결과가 없을 시 INSERT
			log.info(">>>>>>>>>>>>>>>>>>>>>>>>insertClientInfo");
			clientMapper.insertClientInfo(clientInfoDTO);
		}
		
	}

	
}



















