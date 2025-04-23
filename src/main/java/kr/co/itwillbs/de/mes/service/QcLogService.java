package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.mes.dto.QcLogDTO;
import kr.co.itwillbs.de.mes.dto.QcLogSearchDTO;
import kr.co.itwillbs.de.mes.mapper.QcLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class QcLogService {

	private final QcLogMapper qcLogMapper;

	// 품질로그 등록
	@LogExecution
	@Transactional
	public String insertQcLog(QcLogDTO qcLogDTO) {
		log.info("품질로그 등록 요청: {}", qcLogDTO);
		qcLogMapper.insertQcLog(qcLogDTO);
		log.info("품질로그 등록 완료 - qcIdx: {}", qcLogDTO.getQcIdx());
		return "redirect:/mes/qclog";
	}

	// 품질로그 총 개수 (검색 조건 포함)
	public int searchQcLogCount(QcLogSearchDTO searchDTO) {
		log.info("품질로그 개수 조회 - 검색 조건: {}", searchDTO);
		return qcLogMapper.searchQcLogCount(searchDTO);
	}

	// 품질로그 목록 조회 (검색 + 페이징)
	public List<QcLogDTO> searchQcLog(QcLogSearchDTO searchDTO) {
		log.info("품질로그 목록 조회 - 검색 조건: {}", searchDTO);
		return qcLogMapper.searchQcLog(searchDTO);
	}

	// 품질로그 상세 조회
	public QcLogDTO getQcLogByIdx(Long idx) {
		log.info("품질로그 상세 조회 - idx: {}", idx);
		return qcLogMapper.getQcLogByIdx(idx);
	}

	// 품질로그 수정
	@LogExecution
	@Transactional
	public void updateQcLog(QcLogDTO qcLogDTO) {
		log.info("품질로그 수정 요청 - idx: {}", qcLogDTO.getIdx());

		// productDTO가 null이 아닌지 체크
		if (qcLogDTO != null) {
			// 품질로그 정보를 업데이트하는 쿼리 호출
			qcLogMapper.updateQcLog(qcLogDTO);
			log.info("품질로그 수정 완료 - qcIdx: {}", qcLogDTO.getQcIdx());
		}
	}

}