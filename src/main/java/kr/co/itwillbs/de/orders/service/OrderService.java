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
import kr.co.itwillbs.de.orders.dto.OrderFormDTO;
import kr.co.itwillbs.de.orders.dto.OrderSearchDTO;
import kr.co.itwillbs.de.orders.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderService {
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private CommonService commonService;

	/**
	 * 수주/발주 정보 조건 카운트 가져오기 페이징용
	 * @param orderSearchDTO
	 * @return
	 */
	public int getOrderCountForPaging(OrderSearchDTO orderSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return orderMapper.getOrderCountForPaging(orderSearchDTO);
	}
	
	/**
	 * 수주/발주 정보 조건 검색 가져오기
	 * @param orderSearchDTO
	 * @return List<OrderDTO>
	 */
	public List<OrderDTO> getOrderList(OrderSearchDTO orderSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return orderMapper.getOrderList(orderSearchDTO);
	}
	
	// ------------------------------------------------------------------------------------
	/**
	 * 거래처 정보 조회(SELECT)
	 * @return List<ClientDTO>
	 */
	public List<ClientDTO> getClientList() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		return orderMapper.getClientList();
	}

	/**
	 * 수주/발주 주문 정보 등록(INSERT) >> orderDTO
	 * @param orderFormDTO
	 * @return 
	 */
	@LogExecution // 로그 남길 서비스
	public void registerOrder(@Valid OrderFormDTO orderFormDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// MySQL DB에서 시퀀스 가져와서 document_number에 넣기
		orderFormDTO.getOrderDTO().setDocumentNumber(commonService.getSeqOrderNumberfromMySQL());
		orderMapper.insertOrder(orderFormDTO.getOrderDTO());
		
		// orderDTO에 들어간 documentNumber 가져오기
		String documentNumber = orderFormDTO.getOrderDTO().getDocumentNumber();	
		orderFormDTO.getOrderDetailDTO().setDocumentNumber(documentNumber);
		orderMapper.insertOrderDetail(orderFormDTO.getOrderDetailDTO());
	}

	// ------------------------------------------------------------------------------------
	/**
	 * 수주/발주 상세 정보 가져오기
	 * @param documentNumber
	 * @return OrderDTO
	 */
	public OrderDTO getOrderByDocumentNumber(String documentNumber) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return orderMapper.getOrderByDocumentNumber(documentNumber);
	}

	// ------------------------------------------------------------------------------------
	/**
	 * 수주/발주 주문 정보 수정(UPDATE)
	 * @param orderDTO
	 * @param orderDetailDTO
	 * @return 
	 */
	@LogExecution // 로그 남길 서비스
	public void modifyOrder(OrderDTO orderDTO, OrderDetailDTO orderDetailDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		orderMapper.updateOrder(orderDTO);
		orderMapper.updateOrderDetail(orderDetailDTO);
	}
	
	// ------------------------------------------------------------------------------------
	public List<OrderCodeDTO> getDepartmentList() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return orderMapper.getDepartmentList();
	}

	public List<OrderCodeDTO> getSubDepartmentList(String departmentCode) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return orderMapper.getSubDepartmentList(departmentCode);
	}

	public List<OrderCodeDTO> getEmployeeList(String departmentCode, String subDepartmentCode) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return orderMapper.getEmployeeList(departmentCode, subDepartmentCode);
	}

	public OrderCodeDTO getEmployeeInfo(String employeeId) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return orderMapper.getEmployeeInfo(employeeId);
	}
	
	
	
}
