package kr.co.itwillbs.de.orders.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.orders.dto.ClientDTO;



@Mapper
public interface ClientMapper {
	
	/**
	 * 거래처 리스트 조회
	 * @return
	 */
	List<ClientDTO> getClientList();

	int insertClient(ClientDTO clientDTO);

	ClientDTO getClient(String idx);
	
}
