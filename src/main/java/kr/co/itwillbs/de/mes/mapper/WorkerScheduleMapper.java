package kr.co.itwillbs.de.mes.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.mes.dto.WorkerScheduleDTO;
import kr.co.itwillbs.de.mes.dto.WorkerScheduleSearchDTO;

@Mapper
public interface WorkerScheduleMapper {

	// 근무 일정 등록
	public void insertWorkerSchedule(WorkerScheduleDTO workerScheduleDTO);

	// 근무 일정 목록 페이징
	public int searchWorkerScheduleCount(WorkerScheduleSearchDTO searchDTO);

	// 근무 일정 목록 + 검색
	public List<WorkerScheduleDTO> searchWorkerSchedule(WorkerScheduleSearchDTO searchDTO);

	// 근무 일정 상세 조회
	public WorkerScheduleDTO getWorkerScheduleByIdx(Long idx);

	// 근무 일정 정보 수정
	public void updateWorkerSchedule(WorkerScheduleDTO workerScheduleDTO);

}
