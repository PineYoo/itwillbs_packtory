package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.common.util.LogUtil;
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
	public void insertWorkerMetrics(WorkerMetricsDTO workerMetricsDTO) {
		LogUtil.logStart(log);

		workerMetricsMapper.insertWorkerMetrics(workerMetricsDTO);
	}

	// 보유 자격증 정보 총 개수 (검색 조건 포함)
	public int searchWorkerMetricsCount(WorkerMetricsSearchDTO searchDTO) {
		LogUtil.logStart(log);

		return workerMetricsMapper.WorkerMetricsCount(searchDTO);
	}

	// 보유 자격증 정보 목록 조회 (검색 + 페이징)
	public List<WorkerMetricsDTO> searchWorkerMetrics(WorkerMetricsSearchDTO searchDTO) {
		LogUtil.logStart(log);

		return workerMetricsMapper.WorkerMetrics(searchDTO);
	}

	// 보유 자격증 정보 상세 조회
	public WorkerMetricsDTO getWorkerMetricsByIdx(Long idx) {
		LogUtil.logStart(log);

		return workerMetricsMapper.getWorkerMetricsByIdx(idx);
	}

	// 보유 자격증 정보 수정
	@LogExecution
	@Transactional
	public void updateWorkerMetrics(WorkerMetricsDTO workerMetricsDTO) {
		LogUtil.logStart(log);

		workerMetricsMapper.updateWorkerMetrics(workerMetricsDTO);
	}
}