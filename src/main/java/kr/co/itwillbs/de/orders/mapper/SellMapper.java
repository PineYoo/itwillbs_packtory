package kr.co.itwillbs.de.orders.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.orders.dto.OrderDTO;

@Mapper
public interface SellMapper {

	// 전체 수주 관리 목록 조회
//	List<OrderDTO> getSellList();
	List<HashMap<String, Object>> getSellList();

	
}
