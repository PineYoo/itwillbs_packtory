package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.common.util.LogUtil;
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
	public void insertLocationInfo(LocationInfoDTO locationInfoDTO) {
		LogUtil.logStart(log);

		locationInfoMapper.insertLocationInfo(locationInfoDTO);
	}

	// 공장 장소 정보 총 개수 (검색 조건 포함)
	public int searchLocationInfoCount(LocationInfoSearchDTO searchDTO) {
		LogUtil.logStart(log);

		return locationInfoMapper.LocationInfoCount(searchDTO);
	}

	// 공장 장소 정보 목록 조회 (검색 + 페이징)
	public List<LocationInfoDTO> searchLocationInfo(LocationInfoSearchDTO searchDTO) {
		LogUtil.logStart(log);

		return locationInfoMapper.LocationInfo(searchDTO);
	}

	// 공장 장소 정보 상세 조회
	public LocationInfoDTO getLocationInfoByIdx(Long idx) {
		LogUtil.logStart(log);

		return locationInfoMapper.getLocationInfoByIdx(idx);
	}

	// 공장 장소 정보 수정
	@LogExecution
	@Transactional
	public void updateLocationInfo(LocationInfoDTO locationInfoDTO) {
		LogUtil.logStart(log);

		locationInfoMapper.updateLocationInfo(locationInfoDTO);
	}

	// 공장 장소 목록 가져가기 (외부용)
	public List<LocationInfoDTO> getLocationInfoList() {
		return locationInfoMapper.selectLocationInfoList();
	}
}