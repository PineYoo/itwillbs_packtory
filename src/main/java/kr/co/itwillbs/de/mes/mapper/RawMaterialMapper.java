package kr.co.itwillbs.de.mes.mapper;

import kr.co.itwillbs.de.mes.dto.RawMaterialDTO;
import kr.co.itwillbs.de.mes.dto.RawMaterialSearchDTO;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RawMaterialMapper {

	// 원자재 등록
	public void insertRawMaterial(RawMaterialDTO rawmaterialDTO);

	// 원자재 목록 페이징
	public int searchRawMaterialCount(RawMaterialSearchDTO searchDTO);

	// 원자재 목록 + 검색
	public List<RawMaterialDTO> searchRawMaterial(RawMaterialSearchDTO searchDTO);

	// 원자재 상세 조회
	public RawMaterialDTO getRawMaterialByIdx(Long idx);

	// 원자재 정보 수정
	public void updateRawMaterial(RawMaterialDTO rawmaterialDTO);

	// 원자재 삭제
	public void deleteRawMaterial(Long idx);
	
	// --------------------------------------
	// 원자재 정보 들고 가기
	public RawMaterialDTO selectRawMaterialById(Long idx);

}
