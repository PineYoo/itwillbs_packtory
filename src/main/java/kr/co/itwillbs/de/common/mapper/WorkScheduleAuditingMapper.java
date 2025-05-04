package kr.co.itwillbs.de.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import kr.co.itwillbs.de.human.dto.EmployeeDTO;
import kr.co.itwillbs.de.mes.dto.WorkOrdersItemsDTO;
import kr.co.itwillbs.de.mes.dto.WorkerScheduleDTO;

@Mapper
public interface WorkScheduleAuditingMapper {

    // 진행 중이고 날짜가 있는 작업 아이템 조회
    List<WorkOrdersItemsDTO> selectActiveWorkItemsWithDates();

    // 특정 product_lines_idx에 속한 직원 조회
    List<EmployeeDTO> selectAvailableEmployees();
    
    // 근무 일정 저장
    int insertWorkerSchedules(@Param("list") List<WorkerScheduleDTO> workerScheduleDTO);
    
    // 근무 일정 업뎃
    int updateWorkOrdersItems(WorkOrdersItemsDTO workOrdersItemsDTO);

}
