package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.mes.dto.WorkerMetricsDTO;
import kr.co.itwillbs.de.mes.dto.WorkerMetricsSearchDTO;
import kr.co.itwillbs.de.mes.mapper.WorkerMetricsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkerMetricsService {

	private final WorkerMetricsMapper workerMetricsMapper;

	// 보유 자격증 정보 등록
	@LogExecution
	@Transactional
	public String insertWorkerMetrics(WorkerMetricsDTO workerMetricsDTO) {
		log.info("보유 자격증 정보 등록 요청: {}", workerMetricsDTO);
		workerMetricsMapper.insertWorkerMetrics(workerMetricsDTO);
		log.info("보유 자격증 정보 등록 완료 - idx: {}", workerMetricsDTO.getIdx());
		return "redirect:/mes/workermetrics";
	}

	// 보유 자격증 정보 총 개수 (검색 조건 포함)
	public int searchWorkerMetricsCount(WorkerMetricsSearchDTO searchDTO) {
		log.info("보유 자격증 정보 개수 조회 - 검색 조건: {}", searchDTO);
		return workerMetricsMapper.searchWorkerMetricsCount(searchDTO);
	}

	// 보유 자격증 정보 목록 조회 (검색 + 페이징)
	public List<WorkerMetricsDTO> searchWorkerMetrics(WorkerMetricsSearchDTO searchDTO) {
		log.info("보유 자격증 정보 목록 조회 - 검색 조건: {}", searchDTO);
		return workerMetricsMapper.searchWorkerMetrics(searchDTO);
	}

	// 보유 자격증 정보 상세 조회
	public WorkerMetricsDTO getWorkerMetricsByIdx	(Long idx) {
		log.info("보유 자격증 정보 상세 조회 - idx: {}", idx);
		return workerMetricsMapper.getWorkerMetricsByIdx(idx);
	}

	// 보유 자격증 정보 수정
	@LogExecution
	@Transactional
	public void updateWorkerMetrics(WorkerMetricsDTO workerMetricsDTO) {
		log.info("보유 자격증 정보 수정 요청 - idx: {}", workerMetricsDTO.getIdx());

		// WorkerMetricsDTO 가 널인지 체크
		if (workerMetricsDTO != null) {
			// 보유 자격증 정보 정보를 업데이트하는 쿼리 호출
			workerMetricsMapper.updateWorkerMetrics(workerMetricsDTO);
			log.info("보유 자격증 정보 수정 완료 - qcIdx: {}", workerMetricsDTO.getIdx());
		}
	}
}