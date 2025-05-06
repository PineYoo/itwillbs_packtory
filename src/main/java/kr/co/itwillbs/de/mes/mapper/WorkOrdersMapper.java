package kr.co.itwillbs.de.mes.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import kr.co.itwillbs.de.mes.dto.WorkOrdersFormDTO;
import kr.co.itwillbs.de.mes.dto.WorkOrdersItemsDTO;
import kr.co.itwillbs.de.mes.dto.WorkOrdersItemsSearchDTO;
import kr.co.itwillbs.de.mes.dto.WorkOrdersMasterDTO;
import kr.co.itwillbs.de.mes.dto.WorkOrdersSearchDTO;

public interface WorkOrdersMapper {

	//	============================================================
	int registerWorkOrders(WorkOrdersFormDTO workOrdersFormDTO);
	//	============================================================
	
	/**
	 * INSERT INTO t_work_orders_master VALUES workOrdersMasterDTO
	 * @param workOrdersMasterDTO
	 * @return insert key
	 */
	int registerWorkOrdersMaster(WorkOrdersFormDTO workOrdersFormDTO);
	
	
	/**
	 * INSERT INTO t_work_orders_items VALUES workOrdersItemsDTO
	 * @param workOrdersItemsDTO
	 * @return insert key
	 */
	int registerWorkOrdersItems(WorkOrdersFormDTO workOrdersFormDTO);

	/**
	 * SELECT count(*) FROM t_work_orders_master WHERE workOrdersSearchDTO (페이징용)
	 * @param workOrdersSearchDTO
	 * @return count
	 */
	int getWorkOrdersCountBySearchDTO(WorkOrdersSearchDTO workOrdersSearchDTO);
	
	/**
	 * SELECT FROM t_work_orders_master WHERE workOrdersSearchDTO
	 * @param workOrdersSearchDTO
	 * @return List<WorkOrdersMasterDTO>
	 */
	List<WorkOrdersMasterDTO> getWorkOrdersBySearchDTO(WorkOrdersSearchDTO workOrdersSearchDTO);

	/**
	 * SELECT FROM t_work_orders_master WHERE idx = #{idx}
	 * @param idx
	 * @return RecipeMasterDTO
	 */
	WorkOrdersFormDTO getWorkOrdersByIdx(@Param("idx")String idx);
	
	/**
	 * SELECT FROM t_work_orders_master WHERE idx = #{idx}
	 * @param idx
	 * @return RecipeMasterDTO
	 */
	List<WorkOrdersItemsDTO> getWorkOrdersItemsByIdx(@Param("idx")String idx);
	
	//	============================================================
	int modifyWorkOrders(WorkOrdersFormDTO workOrdersFormDTO);
	//	============================================================
	
	/**
	 * UPDATE t_work_orders_master SET workOrdersMasterDTO
	 * @param workOrdersMasterDTO
	 * @return affectedRow
	 */
	int modifyWorkOrdersMaster(WorkOrdersFormDTO workOrdersFormDTO);
	
	int removeWorkOrdersItems(WorkOrdersFormDTO workOrdersFormDTO);
	
	/**
	 * UPDATE t_work_orders_itmes SET workOrdersItemsDTO
	 * @param workOrdersItemsDTO
	 * @return affectedRow
	 */
	int modifyWorkOrdersItems(WorkOrdersFormDTO workOrdersFormDTO);
	
	/*
	 * 외부용
	 */
	public List<WorkOrdersItemsDTO> selectWorkOrdersList();

	/**
	 * select count(*) from t_work-orders_items where searchDTO
	 * <br>작업 현황 보고 리스트 페이징용 카운트
	 * @param WorkOrdersItemsSearchDTO
	 * @return int 카운트
	 */
	int selectWorkItemsCountBySearchDTO(WorkOrdersItemsSearchDTO searchDTO);
	
	/**
	 * select * from t_work-orders_items where searchDTO
	 * <br>작업 현황 보고 리스트 페이징
	 * @param searchDTO WorkOrdersItemsSearchDTO
	 * @return list List<WorkOrdersItemsDTO>
	 */
	List<WorkOrdersItemsDTO> selectWorkItemsListBySearchDTO(WorkOrdersItemsSearchDTO searchDTO);

	/**
	 * 작업 현황 보고 상세 조회
	 * @param searchDTO WorkOrdersItemsSearchDTO
	 * @return WorkOrdersItemsDTO
	 */
	WorkOrdersItemsDTO selectWorkItemByIdx(@Param("idx") String idx);
}
