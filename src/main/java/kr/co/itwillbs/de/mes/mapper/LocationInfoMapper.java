package kr.co.itwillbs.de.mes.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.mes.dto.LocationInfoDTO;
import kr.co.itwillbs.de.mes.dto.LocationInfoSearchDTO;

@Mapper
public interface LocationInfoMapper {

	// 공장 장소 정보 등록
	public void insertLocationInfo(LocationInfoDTO locationInfoDTO);

	// 공장 장소 정보 목록 페이징
	public int searchLocationInfoCount(LocationInfoSearchDTO searchDTO);

	// 공장 장소 정보 목록 + 검색
	public List<LocationInfoDTO> searchLocationInfo(LocationInfoSearchDTO searchDTO);

	// 공장 장소 정보 상세 조회
	public LocationInfoDTO getLocationInfoByIdx(Long idx);

	// 공장 장소 정보 정보 수정
	public void updateLocationInfo(LocationInfoDTO locationInfoDTO);

	// 공장 장소 정보 들고 가기 (외부용)
	public List<LocationInfoDTO> selectLocationInfoList();
}
