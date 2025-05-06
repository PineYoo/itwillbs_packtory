package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.common.service.CommonService;
import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.mes.dto.WorkOrdersFormDTO;
import kr.co.itwillbs.de.mes.dto.WorkOrdersItemsDTO;
import kr.co.itwillbs.de.mes.dto.WorkOrdersItemsSearchDTO;
import kr.co.itwillbs.de.mes.dto.WorkOrdersMasterDTO;
import kr.co.itwillbs.de.mes.dto.WorkOrdersSearchDTO;
import kr.co.itwillbs.de.mes.mapper.WorkOrdersMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WorkOrdersService {

	private final WorkOrdersMapper workOrdersMapper;
	private final CommonService commonService;

	public WorkOrdersService(WorkOrdersMapper workOrdersMapper, CommonService commonService) {
		this.workOrdersMapper = workOrdersMapper;
		this.commonService = commonService;
	}

	// ==============================================================
	/**
	 * 작업지시 등록
	 * 
	 * @param workOrdersFormDTO
	 * @throws Exception
	 */
	@LogExecution
	@Transactional(rollbackFor = Exception.class)
	public void registerWorkOrders(WorkOrdersFormDTO workOrdersFormDTO) throws Exception {
		LogUtil.logStart(log);
		try {
			// 넘어온 작업지시서 문서번호 세팅
			workOrdersFormDTO.setDocumentNumber(commonService.getWorkOrderDocNoFromMySQL());

			// t_work_orders_master 값 등록
			workOrdersMapper.registerWorkOrdersMaster(workOrdersFormDTO);
			// t_work_orders_items 값 등록
			workOrdersMapper.registerWorkOrdersItems(workOrdersFormDTO);
		} catch (Exception e) {
			LogUtil.error(log, "DB 작업 도중 에러 발생 {}", e.getMessage());
			throw e;
		}
	}
	// ==============================================================

	/**
	 * 작업지시 리스트 페이지 페이징용 카운트
	 * 
	 * @param workOrdersSearchDTO
	 * @return int
	 */
	public int getWorkOrdersCountBySearchDTO(WorkOrdersSearchDTO workOrdersSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		return workOrdersMapper.getWorkOrdersCountBySearchDTO(workOrdersSearchDTO);
	}

	/**
	 * 작업지시 리스트 검색 조건 조회
	 * 
	 * @param workOrdersSearchDTO
	 * @return List<WorkOrdersMasterDTO>
	 */
	public List<WorkOrdersMasterDTO> getWorkOrdersBySearchDTO(WorkOrdersSearchDTO workOrdersSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		return workOrdersMapper.getWorkOrdersBySearchDTO(workOrdersSearchDTO);
	}

	/**
	 * 작업지시 상세 단건 조회
	 * 
	 * @param idx
	 * @return WorkOrdersMasterDTO
	 */
	public WorkOrdersFormDTO getWorkOrdersByIdx(String idx) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		// Master
		WorkOrdersFormDTO workOrdersFormDTO = workOrdersMapper.getWorkOrdersByIdx(idx);
		// Items
		workOrdersFormDTO.setWorkOrdersItemList(workOrdersMapper.getWorkOrdersItemsByIdx(idx));

		return workOrdersFormDTO;
	}

	// ========================================================================
	@LogExecution
	@Transactional(rollbackFor = Exception.class)
	public void modifyWorkOrders(WorkOrdersFormDTO workOrdersFormDTO) throws Exception {
		LogUtil.logStart(log);
		try {
			// t_work_orders_master 수정
			workOrdersMapper.modifyWorkOrdersMaster(workOrdersFormDTO);

			// t_work_orders_items 값 등록은 삭제 후 등록
			workOrdersMapper.removeWorkOrdersItems(workOrdersFormDTO);
			if (workOrdersFormDTO.getWorkOrdersItemList().size() > 0) {
				workOrdersMapper.registerWorkOrdersItems(workOrdersFormDTO);
			}
			// t_work_orders_items 수정
//			workOrdersMapper.modifyWorkOrdersItems(workOrdersFormDTO);
		} catch (Exception e) {
			LogUtil.error(log, "DB 작업 도중 에러 발생 {}", e.getMessage());
			throw e;
		}
	}
	// ========================================================================

	/**
	 * 작업지시 master 수정
	 * 
	 * @param workOrdersMasterDTO
	 */
	public void modifyWorkOrdersMaster(WorkOrdersMasterDTO workOrdersMasterDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

//		workOrdersMapper.modifyWorkOrdersMaster(workOrdersMasterDTO);
	}

	/**
	 * 작업지시 itmes 수정
	 * 
	 * @param workOrdersItemsDTO
	 */
	public void modifyWorkOrdersItems(WorkOrdersItemsDTO workOrdersItemsDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

//		workOrdersMapper.modifyWorkOrdersItems(workOrdersItemsDTO);
	}

	// (외부용)
	public List<WorkOrdersItemsDTO> getWorkOrdersList() {
		return workOrdersMapper.selectWorkOrdersList();
	}
	
	/**
	 * 작업 현황 보고 리스트 페이징용 카운트
	 * @param searchDTO
	 * @return
	 */
	public int selectWorkItemsCountBySearchDTO(WorkOrdersItemsSearchDTO searchDTO) {
		LogUtil.logStart(log);

		return workOrdersMapper.selectWorkItemsCountBySearchDTO(searchDTO);
	}
	
	/**
	 * 작업 현황 보고 리스트 페이징
	 * @param searchDTO
	 * @return
	 */
	public List<WorkOrdersItemsDTO> selectWorkItemsListBySearchDTO(WorkOrdersItemsSearchDTO searchDTO) {
		LogUtil.logStart(log);
		
		return workOrdersMapper.selectWorkItemsListBySearchDTO(searchDTO);
	}
	
	/**
	 * 작업 현황 보고 상세 조회
	 * @param searchDTO
	 * @return
	 */
	public WorkOrdersItemsDTO selectWorkItemByIdx(String idx) {
		LogUtil.logStart(log);
		
		return workOrdersMapper.selectWorkItemByIdx(idx);
	}

}
