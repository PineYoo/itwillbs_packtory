package kr.co.itwillbs.de.commute.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.commute.dto.CommuteDTO;
import kr.co.itwillbs.de.commute.dto.CommuteListDTO;
import kr.co.itwillbs.de.commute.entity.Commute;
import kr.co.itwillbs.de.commute.mapper.CommuteMapper;
import kr.co.itwillbs.de.commute.repository.CommuteRepository;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class CommuteService {

	@Autowired
	private CommuteMapper commuteMapper;

	// 출퇴근 목록 조회 요청
	public List<CommuteListDTO> getCommuteList(String id) {
		return commuteMapper.getCommuteList(id);
	}
	}
