package kr.co.itwillbs.de.mes.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.mes.dto.RawMaterialDTO;
import kr.co.itwillbs.de.mes.dto.RawMaterialSearchDTO;

@Mapper
public interface RawMaterialMapper {

	// 마스터 자재 등록
	public void insertMasterMaterial(RawMaterialDTO rawmaterialDTO);

	// 마스터 자재 조회 + 페이징
	public int MasterMaterialCount(RawMaterialSearchDTO searchDTO);

	// 마스터 자재 조회
	public List<RawMaterialDTO> MasterMaterial(RawMaterialSearchDTO searchDTO);

	// 마스터 자재 상세 조회
	public RawMaterialDTO getMasterMaterialByIdx(Long idx);

	// 마스터 자재 정보 수정
	public void updateMasterMaterial(RawMaterialDTO rawmaterialDTO);
	
	// 부속 자재 조회
	public List<RawMaterialDTO> getSubMaterialsByIdx(Long idx);

	// 부속 자재 등록
	public void insertSubMaterial(RawMaterialDTO rawMaterialDTO);

	// 부속 자재 정보 수정
	public void updateSubMaterial(RawMaterialDTO rawmaterialDTO);

	// 자재 정보 들고 가기 (외부용)
	public List<RawMaterialDTO> selectRawMaterialList();

}
