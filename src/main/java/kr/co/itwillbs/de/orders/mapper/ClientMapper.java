package kr.co.itwillbs.de.orders.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.ModelAttribute;

import kr.co.itwillbs.de.orders.dto.ClientDTO;
import kr.co.itwillbs.de.orders.dto.ClientInfoDTO;
import kr.co.itwillbs.de.orders.dto.ClientSearchDTO;



@Mapper
public interface ClientMapper {
	
	/**
	 * 거래처 리스트 조회
	 * @return
	 */
	List<ClientDTO> getClientList(ClientSearchDTO clientSearchDTO);

	/**
	 * 거래처 등록 요청
	 * @param clientDTO
	 * @return int
	 */
	int insertClient(ClientDTO clientDTO);
	
	/**
	 * 거래처 상세정보 요청
	 * @param companyNumber
	 * @return Map<String, Object>
	 */
	ClientInfoDTO getClient(String companyNumber);
	
	/**
	 * 거래처 정보 수정
	 * @param clientDTO
	 */
	void updateClient(ClientInfoDTO clientInfoDTO);

	//	=====================================================================
	//	거래처_부가정보 테이블
	
	/**
	 * 사업자번호로 거래처_부가정보 테이블에 레코드 존재여부 확인
	 * @param companyNumber
	 * @return
	 */
	int getClientInfo(String companyNumber);
	
	/**
	 * 거래처_부가정보 테이블 사업자번호 등록
	 * @param companyNumber
	 */
	void insertClientInfo(ClientInfoDTO clientInfoDTO);
	
	/**
	 * 거래처_부가정보 수정
	 * @param clientInfoDTO
	 */
	void updateClientInfo(ClientInfoDTO clientInfoDTO);
	
	/*
	 * 원자재에서 씀
	 */
	List<ClientDTO> selectClientList();

	
}
