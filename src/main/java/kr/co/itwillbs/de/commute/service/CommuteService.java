package kr.co.itwillbs.de.commute.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.commute.dto.CommuteDTO;
import kr.co.itwillbs.de.commute.entity.Commute;
import kr.co.itwillbs.de.commute.repository.CommuteRepository;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class CommuteService {

	@Autowired
	private CommuteRepository commuteRepository;

	// 출퇴근 목록 조회 요청
	public List<CommuteDTO> getCommuteList() {
		List<Commute> commuteList = commuteRepository.findAll();
		log.debug(">>>>commuteList : " + commuteList);
		return convertCommuteToCommuteDTO(commuteList);
		
	}
	
	private List<CommuteDTO> convertCommuteToCommuteDTO(List<Commute> commuteList) {
		return commuteList.stream() // 스트림으로 읽어들임
				.map(commute -> commute.toDto()) // 각 요소 구별(item 객체를 Item 클래스의 toDto() 메서드로 ItemDTO 로 변환
				.collect(Collectors.toList());
	}
	
}
