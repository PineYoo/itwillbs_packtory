package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.mes.dto.LocationInfoDTO;
import kr.co.itwillbs.de.mes.dto.LocationInfoSearchDTO;
import kr.co.itwillbs.de.mes.mapper.LocationInfoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationInfoService {

	private final LocationInfoMapper locationInfoMapper;

	// 공장 장소 정보 등록
	@LogExecution
	@Transactional
	public String insertLocationInfo(LocationInfoDTO locationInfoDTO) {
		log.info("공장 장소 정보 등록 요청: {}", locationInfoDTO);
		locationInfoMapper.insertLocationInfo(locationInfoDTO);
		log.info("공장 장소 정보 등록 완료 - idx: {}", locationInfoDTO.getIdx());
		return "redirect:/mes/locationinfo";
	}

	// 공장 장소 정보 총 개수 (검색 조건 포함)
	public int searchLocationInfoCount(LocationInfoSearchDTO searchDTO) {
		log.info("공장 장소 정보 개수 조회 - 검색 조건: {}", searchDTO);
		return locationInfoMapper.searchLocationInfoCount(searchDTO);
	}

	// 공장 장소 정보 목록 조회 (검색 + 페이징)
	public List<LocationInfoDTO> searchLocationInfo(LocationInfoSearchDTO searchDTO) {
		log.info("공장 장소 정보 목록 조회 - 검색 조건: {}", searchDTO);
		return locationInfoMapper.searchLocationInfo(searchDTO);
	}

	// 공장 장소 정보 상세 조회
	public LocationInfoDTO getLocationInfoByIdx	(Long idx) {
		log.info("공장 장소 정보 상세 조회 - idx: {}", idx);
		return locationInfoMapper.getLocationInfoByIdx(idx);
	}

	// 공장 장소 정보 수정
	@LogExecution
	@Transactional
	public void updateLocationInfo(LocationInfoDTO locationInfoDTO) {
		log.info("공장 장소 정보 수정 요청 - idx: {}", locationInfoDTO.getIdx());

		// LocationInfoDTO 가 널인지 체크
		if (locationInfoDTO != null) {
			// 공장 장소 정보 정보를 업데이트하는 쿼리 호출
			locationInfoMapper.updateLocationInfo(locationInfoDTO);
			log.info("공장 장소 정보 수정 완료 - qcIdx: {}", locationInfoDTO.getIdx());
		}
	}
	
	// 공장 장소 목록 가져가기 (외부용)
	public List<LocationInfoDTO> getLocationInfoList(){
		return locationInfoMapper.selectLocationInfoList();
	}
}