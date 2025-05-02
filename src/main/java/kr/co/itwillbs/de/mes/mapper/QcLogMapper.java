package kr.co.itwillbs.de.mes.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.mes.dto.QcLogDTO;
import kr.co.itwillbs.de.mes.dto.QcLogSearchDTO;

@Mapper
public interface QcLogMapper {

	// 품질 로그 등록
	public void insertQcLog(QcLogDTO qcStandardDTO);

	// 품질 로그 목록 페이징
	public int QcLogCount(QcLogSearchDTO searchDTO);

	// 품질 로그 목록 + 검색
	public List<QcLogDTO> QcLog(QcLogSearchDTO searchDTO);

	// 품질 로그 상세 조회
	public QcLogDTO getQcLogByIdx(Long idx);

	// 품질 로그 정보 수정
	public void updateQcLog(QcLogDTO qcStandardDTO);
}