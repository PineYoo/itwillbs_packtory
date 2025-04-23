package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.mes.dto.WorkerScheduleDTO;
import kr.co.itwillbs.de.mes.dto.WorkerScheduleSearchDTO;
import kr.co.itwillbs.de.mes.mapper.WorkerScheduleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkerScheduleService {

	private final WorkerScheduleMapper workerScheduleMapper;

	// 근무 일정 등록
	@LogExecution
	@Transactional
	public String insertWorkerSchedule(WorkerScheduleDTO workerScheduleDTO) {
		log.info("근무 일정 등록 요청: {}", workerScheduleDTO);
		workerScheduleMapper.insertWorkerSchedule(workerScheduleDTO);
		log.info("근무 일정 등록 완료 - idx: {}", workerScheduleDTO.getIdx());
		return "redirect:/mes/workerschedule";
	}

	// 근무 일정 총 개수 (검색 조건 포함)
	public int searchWorkerScheduleCount(WorkerScheduleSearchDTO searchDTO) {
		log.info("근무 일정 개수 조회 - 검색 조건: {}", searchDTO);
		return workerScheduleMapper.searchWorkerScheduleCount(searchDTO);
	}

	// 근무 일정 목록 조회 (검색 + 페이징)
	public List<WorkerScheduleDTO> searchWorkerSchedule(WorkerScheduleSearchDTO searchDTO) {
		log.info("근무 일정 목록 조회 - 검색 조건: {}", searchDTO);
		return workerScheduleMapper.searchWorkerSchedule(searchDTO);
	}

	// 근무 일정 상세 조회
	public WorkerScheduleDTO getWorkerScheduleByIdx	(Long idx) {
		log.info("근무 일정 상세 조회 - idx: {}", idx);
		return workerScheduleMapper.getWorkerScheduleByIdx(idx);
	}

	// 근무 일정 수정
	@LogExecution
	@Transactional
	public void updateWorkerSchedule(WorkerScheduleDTO workerScheduleDTO) {
		log.info("근무 일정 수정 요청 - idx: {}", workerScheduleDTO.getIdx());

		// WorkerMetricsDTO 가 널인지 체크
		if (workerScheduleDTO != null) {
			// 근무 일정 정보를 업데이트하는 쿼리 호출
			workerScheduleMapper.updateWorkerSchedule(workerScheduleDTO);
			log.info("근무 일정 수정 완료 - qcIdx: {}", workerScheduleDTO.getIdx());
		}
	}
}