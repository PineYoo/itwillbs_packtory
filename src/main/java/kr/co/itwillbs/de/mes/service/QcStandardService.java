package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.mes.dto.QcStandardDTO;
import kr.co.itwillbs.de.mes.dto.QcStandardSearchDTO;
import kr.co.itwillbs.de.mes.mapper.QcStandardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class QcStandardService {

	private final QcStandardMapper qcStandardMapper;

	// 품질 등록
	@LogExecution
	@Transactional
	public void insertQcStandard(QcStandardDTO qcStandardDTO) {
		LogUtil.logStart(log);

		qcStandardMapper.insertQcStandard(qcStandardDTO);
	}

	// 품질 총 개수 (검색 조건 포함)
	public int searchQcStandardCount(QcStandardSearchDTO searchDTO) {
		LogUtil.logStart(log);

		return qcStandardMapper.QcStandardCount(searchDTO);
	}

	// 품질 목록 조회 (검색 + 페이징)
	public List<QcStandardDTO> searchQcStandard(QcStandardSearchDTO searchDTO) {
		LogUtil.logStart(log);

		return qcStandardMapper.QcStandard(searchDTO);
	}

	// 품질 상세 조회
	public QcStandardDTO getQcStandardByIdx(Long idx) {
		LogUtil.logStart(log);

		return qcStandardMapper.getQcStandardByIdx(idx);
	}

	// 품질 수정
	@LogExecution
	@Transactional
	public void updateQcStandard(QcStandardDTO qcStandardDTO) {
		LogUtil.logStart(log);

		qcStandardMapper.updateQcStandard(qcStandardDTO);
	}

	// 품질기준 목록 들고가기 (외부용)
	public List<QcStandardDTO> getQcStandardList() {
		return qcStandardMapper.selectQcStandardList();
	}

}