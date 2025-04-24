package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.common.service.CommonService;
import kr.co.itwillbs.de.mes.dto.LocationInfoDTO;
import kr.co.itwillbs.de.mes.dto.RecipeMasterDTO;
import kr.co.itwillbs.de.mes.dto.RecipeMasterSearchDTO;
import kr.co.itwillbs.de.mes.dto.WorkOrdersFormDTO;
import kr.co.itwillbs.de.mes.dto.WorkOrdersItemsDTO;
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
	
	//	==============================================================
	public void registerWorkOrders(WorkOrdersFormDTO workOrdersFormDTO) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		//	넘어온 작업지시서 문서번호 세팅
		workOrdersFormDTO.setDocumentNumber(commonService.getWorkOrderDocNoFromMySQL());
		
		//	t_work_orders_master 값 등록
		workOrdersMapper.registerWorkOrdersMaster(workOrdersFormDTO);
		//	t_work_orders_items 값 등록
		workOrdersMapper.registerWorkOrdersItems(workOrdersFormDTO);
	}
	//	==============================================================
	
	/**
	 * 작업지시 master 등록
	 * @param workOrdersMasterDTO
	 * @throws Exception
	 */
	public void registerWorkOrdersMaster(WorkOrdersFormDTO workOrdersFormDTO) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		workOrdersMapper.registerWorkOrdersMaster(workOrdersFormDTO);
	}
	
	
	/**
	 * 작업지시 items 등록
	 * @param workOrdersItemsDTO
	 * @throws Exception
	 */
	public void registerWorkOrdersItems(WorkOrdersFormDTO workOrdersFormDTO) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		workOrdersMapper.registerWorkOrdersItems(workOrdersFormDTO);
	}

	/**
	 * 작업지시 리스트 페이지 페이징용 카운트
	 * @param workOrdersSearchDTO
	 * @return int
	 */
	public int getWorkOrdersCountBySearchDTO(WorkOrdersSearchDTO workOrdersSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return workOrdersMapper.getWorkOrdersCountBySearchDTO(workOrdersSearchDTO);
	}
	
	/**
	 * 작업지시 리스트 검색 조건 조회
	 * @param workOrdersSearchDTO
	 * @return List<WorkOrdersMasterDTO>
	 */
	public List<WorkOrdersMasterDTO> getWorkOrdersBySearchDTO(WorkOrdersSearchDTO workOrdersSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return workOrdersMapper.getWorkOrdersBySearchDTO(workOrdersSearchDTO);
	}

	/**
	 * 작업지시 상세 단건 조회
	 * @param idx
	 * @return WorkOrdersMasterDTO
	 */
	public WorkOrdersFormDTO getWorkOrdersByIdx(String idx) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return workOrdersMapper.getWorkOrdersByIdx(idx);
	}

	//	========================================================================
	public void modifyWorkOrders(WorkOrdersFormDTO workOrdersFormDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		workOrdersMapper.modifyWorkOrdersMaster(workOrdersFormDTO);
		
		workOrdersMapper.modifyWorkOrdersItems(workOrdersFormDTO);
	}
	//	========================================================================
	
	/**
	 * 작업지시 master 수정
	 * @param workOrdersMasterDTO
	 */
	public void modifyWorkOrdersMaster(WorkOrdersMasterDTO workOrdersMasterDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
//		workOrdersMapper.modifyWorkOrdersMaster(workOrdersMasterDTO);
	}
	
	/**
	 * 작업지시 itmes 수정
	 * @param workOrdersItemsDTO
	 */
	public void modifyWorkOrdersItems(WorkOrdersItemsDTO workOrdersItemsDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
//		workOrdersMapper.modifyWorkOrdersItems(workOrdersItemsDTO);
	}

}
