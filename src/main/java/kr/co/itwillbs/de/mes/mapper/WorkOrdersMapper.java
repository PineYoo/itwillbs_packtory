package kr.co.itwillbs.de.mes.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import kr.co.itwillbs.de.mes.dto.RecipeProcessDTO;
import kr.co.itwillbs.de.mes.dto.WorkOrdersFormDTO;
import kr.co.itwillbs.de.mes.dto.WorkOrdersItemsDTO;
import kr.co.itwillbs.de.mes.dto.WorkOrdersMasterDTO;
import kr.co.itwillbs.de.mes.dto.WorkOrdersSearchDTO;
import kr.co.itwillbs.de.mes.dto.RecipeMaterialDTO;
import kr.co.itwillbs.de.mes.dto.RecipeMaterialSearchDTO;
import kr.co.itwillbs.de.mes.dto.RecipeMasterDTO;
import kr.co.itwillbs.de.mes.dto.RecipeMasterSearchDTO;

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
	
	//	============================================================
	int modifyWorkOrders(WorkOrdersFormDTO workOrdersFormDTO);
	//	============================================================
	
	/**
	 * UPDATE t_work_orders_master SET workOrdersMasterDTO
	 * @param workOrdersMasterDTO
	 * @return affectedRow
	 */
	int modifyWorkOrdersMaster(WorkOrdersFormDTO workOrdersFormDTO);
	
	/**
	 * UPDATE t_work_orders_itmes SET workOrdersItemsDTO
	 * @param workOrdersItemsDTO
	 * @return affectedRow
	 */
	int modifyWorkOrdersItems(WorkOrdersFormDTO workOrdersFormDTO);


}
