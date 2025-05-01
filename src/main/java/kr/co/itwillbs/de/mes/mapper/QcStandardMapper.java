package kr.co.itwillbs.de.mes.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.itwillbs.de.mes.dto.QcStandardDTO;
import kr.co.itwillbs.de.mes.dto.QcStandardSearchDTO;

@Mapper
public interface QcStandardMapper {

	// 품질 기준 등록
	public void insertQcStandard(QcStandardDTO qcStandardDTO);

	// 품질 기준 목록 페이징
	public int searchQcStandardCount(QcStandardSearchDTO searchDTO);

	// 품질 기준 목록 + 검색
	public List<QcStandardDTO> searchQcStandard(QcStandardSearchDTO searchDTO);

	// 품질 기준 상세 조회
	public QcStandardDTO getQcStandardByIdx(Long idx);

	// 품질 기준 정보 수정
	public void updateQcStandard(QcStandardDTO qcStandardDTO);

	// 품질 기준 목록 들고가기 (외부용)
	public List<QcStandardDTO> selectQcStandardList();
	
	/**
	 * 품질 검사 기준에서 자재, 상품에 따라 검사해야 할 검사 리스트 가져오기
	 * @param idx
	 * @param isProduct
	 * @return
	 */
	public List<QcStandardDTO> selectQcStandardGroupByIdx(@Param("idx")String idx,
														@Param("isProduct")boolean isProduct);
}
