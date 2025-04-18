package kr.co.itwillbs.de.mes.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.mes.dto.QcStandardDTO;
import kr.co.itwillbs.de.mes.dto.QcStandardSearchDTO;

@Mapper
public interface QcStandardMapper {

	// 창고 등록
	public void insertQcStandard(QcStandardDTO qcStandardDTO);

	// 창고 목록 페이징
	public int searchQcStandardCount(QcStandardSearchDTO searchDTO);

	// 창고 목록 + 검색
	public List<QcStandardDTO> searchQcStandard(QcStandardSearchDTO searchDTO);

	// 창고 상세 조회
	public QcStandardDTO getQcStandardByIdx(Long idx);

	// 창고 정보 수정
	public void updateQcStandard(QcStandardDTO qcStandardDTO);

	// 창고 삭제
	public void deleteQcStandard(Long idx);
	
}
