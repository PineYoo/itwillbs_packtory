package kr.co.itwillbs.de.orders.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.common.service.CommonService;
import kr.co.itwillbs.de.orders.dto.ClientDTO;
import kr.co.itwillbs.de.orders.dto.OrderCodeDTO;
import kr.co.itwillbs.de.orders.dto.OrderDTO;
import kr.co.itwillbs.de.orders.dto.OrderDetailDTO;
import kr.co.itwillbs.de.orders.dto.OrderSearchDTO;
import kr.co.itwillbs.de.orders.mapper.SellMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SellService {
	@Autowired
	private SellMapper sellMapper;
	
	@Autowired
	private CommonService commonService;

	/**
	 * 수주/발주 정보 조건 카운트 가져오기 페이징용
	 * @param orderSearchDTO
	 * @return
	 */
	public int getOrderCountForPaging(OrderSearchDTO orderSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return sellMapper.getOrderCountForPaging(orderSearchDTO);
	}
	
	/**
	 * 수주/발주 정보 조건 검색 가져오기
	 * @param orderSearchDTO
	 * @return List<OrderDTO>
	 */
	public List<OrderDTO> getOrderList(OrderSearchDTO orderSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return sellMapper.getOrderList(orderSearchDTO);
	}
	
	// ------------------------------------------------------------------------------------
	/**
	 * 거래처 정보 조회(SELECT)
	 * @return List<ClientDTO>
	 */
	public List<ClientDTO> getClientList() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		return sellMapper.getClientList();
	}

	/**
	 * 수주/발주 주문 정보 등록(INSERT) >> orderDTO
	 * @param orderDTO
	 * @return 
	 */
	@LogExecution // 로그 남길 서비스
	public void registerOrder(@Valid OrderDTO orderDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// MySQL DB에서 시퀀스 가져와서 document_number에 넣기
		orderDTO.setDocumentNumber(commonService.getSeqOrderNumberfromMySQL());
		sellMapper.insertOrder(orderDTO);
	}

	/**
	 * 수주/발주 주문 정보 등록(INSERT) >> orderDetailDTO
	 * @param orderDetailDTO
	 * @return 
	 */
	@LogExecution // 로그 남길 서비스
	public void registerOrderDetail(OrderDetailDTO orderDetailDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		sellMapper.insertOrderDetail(orderDetailDTO);
		
	}

	// ------------------------------------------------------------------------------------
	/**
	 * 수주/발주 상세 정보 가져오기
	 * @param documentNumber
	 * @return OrderDTO
	 */
	public OrderDTO getOrderByDocumentNumber(String documentNumber) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return sellMapper.getOrderByDocumentNumber(documentNumber);
	}

	// ------------------------------------------------------------------------------------
	/**
	 * 수주/발주 주문 정보 수정(UPDATE) >> orderDTO
	 * @param orderDTO
	 * @return 
	 */
	@LogExecution // 로그 남길 서비스
	public void modifyOrder(OrderDTO orderDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		sellMapper.updateOrder(orderDTO);
	}
	
	/**
	 * 수주/발주 주문 정보 수정(UPDATE) >> orderDetailDTO
	 * @param orderDetailDTO
	 * @return 
	 */
	@LogExecution // 로그 남길 서비스
	public void modifyOrderDetail(OrderDetailDTO orderDetailDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		sellMapper.updateOrderDetail(orderDetailDTO);
	}
	
	// ------------------------------------------------------------------------------------
	public List<OrderCodeDTO> getDepartmentList() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return sellMapper.getDepartmentList();
	}

	public List<OrderCodeDTO> getSubDepartmentList(String departmentCode) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return sellMapper.getSubDepartmentList(departmentCode);
	}

	public List<OrderCodeDTO> getEmployeeList(String departmentCode, String subDepartmentCode) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return sellMapper.getEmployeeList(departmentCode, subDepartmentCode);
	}

	public OrderCodeDTO getEmployeeInfo(String employeeId) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return sellMapper.getEmployeeInfo(employeeId);
	}
	
	
	
}
