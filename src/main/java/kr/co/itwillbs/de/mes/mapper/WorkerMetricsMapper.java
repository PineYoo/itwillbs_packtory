package kr.co.itwillbs.de.mes.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.mes.dto.LocationInfoDTO;
import kr.co.itwillbs.de.mes.dto.LocationInfoSearchDTO;
import kr.co.itwillbs.de.mes.dto.WorkerMetricsDTO;
import kr.co.itwillbs.de.mes.dto.WorkerMetricsSearchDTO;

@Mapper
public interface WorkerMetricsMapper {

	// 보유 자격증 정보 등록
	public void insertWorkerMetrics(WorkerMetricsDTO workerMetricsDTO);

	// 보유 자격증 정보 목록 페이징
	public int WorkerMetricsCount(WorkerMetricsSearchDTO searchDTO);

	// 보유 자격증 정보 목록 + 검색
	public List<WorkerMetricsDTO> WorkerMetrics(WorkerMetricsSearchDTO searchDTO);

	// 보유 자격증 정보 상세 조회
	public WorkerMetricsDTO getWorkerMetricsByIdx(Long idx);

	// 보유 자격증 정보 정보 수정
	public void updateWorkerMetrics(WorkerMetricsDTO workerMetricsDTO);
	
	public int WorkerMetricsCount(LocationInfoSearchDTO searchDTO);

	public List<LocationInfoDTO> WorkerMetrics(LocationInfoSearchDTO searchDTO);
}
