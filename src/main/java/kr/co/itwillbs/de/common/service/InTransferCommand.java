package kr.co.itwillbs.de.common.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.constant.TransStatus;
import kr.co.itwillbs.de.common.constant.TransType;
import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.common.vo.TransferDTO;
import kr.co.itwillbs.de.mes.dto.WarehouseTransactionDTO;
import kr.co.itwillbs.de.mes.mapper.WarehouseTransactionMapper;
import kr.co.itwillbs.de.orders.dto.OrderItemsDTO;
import kr.co.itwillbs.de.orders.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InTransferCommand implements TransferCommand {

	private final OrderMapper orderMapper;
	private final WarehouseTransactionMapper warehouseTransactionMapper;
	
	public InTransferCommand(OrderMapper orderMapper, WarehouseTransactionMapper warehouseTransactionMapper) {
		this.orderMapper = orderMapper;
		this.warehouseTransactionMapper = warehouseTransactionMapper;
	}

	/**
	 * <pre>
	 * [입고신청] 에서 사용
	 * t_order_items 에서 구매한 자재들이 t_warehouse_transaction으로 이동하는 서비스
	 * 입고된 자재는
	 * TransType = '1' and status = '1'
	 * </pre>
	 */
	@Override
	@Transactional(timeout = 5, rollbackFor = Exception.class)
	public void execute(TransferDTO dto) throws Exception {
		LogUtil.logStart(log);
		
		List<OrderItemsDTO> orderItems = orderMapper.getOrderItemsByDocumentNumberForUpodate(dto.getOrderDocumentNumber());
		if(orderItems.size() == 0) {
			throw new RuntimeException("NotFound OrderItems");
		}
		
		List<WarehouseTransactionDTO> DTOList = new ArrayList<>(orderItems.size());
		
		for(OrderItemsDTO item : orderItems) {
			WarehouseTransactionDTO wtDTO = new WarehouseTransactionDTO();
			wtDTO.setTransType(TransType.IN.toString());
			wtDTO.setStatus(TransStatus.STANDBY.toString());
			wtDTO.setMaterialIdx(item.getMaterialIdx());
			wtDTO.setProductIdx(item.getProductIdx());
			wtDTO.setQuantity(item.getQuantity());
			wtDTO.setUnit(item.getUnit());
			wtDTO.setSourceLocation(item.getIdx().toString()); // 볼때마다 sourceLocationIdx 로 바꾸고 싶다. 참자! 조원들이랑은 null로 하자고 했으나 생각이 바뀜!(또잉?)
			wtDTO.setDestinationLocation(IN_DESINATION_LOCATION_1); // 볼때마다 destinationLocationIdx 로 바꾸고 싶다. 참2자2!
			wtDTO.setMemo(IN_MEMO_PREFIX+item.getOrderDocumentNumber()); //Orders_item에서 온 documentNumber 표시 최소한 OI로 시작되는 것을 알기에 source에 있는 idx로 조인을 걸어 검증을 할 수 있을 것 같다.
			wtDTO.setIsDeleted(IS_DELETE_N);
			wtDTO.setRegId(REG_ID_TRANSFER); // 얘도 하드코딩해야되...네?
			// 디게 나쁜 DB모양을 하게 되고 있는데.. 좀더 좋은걸 보여줘야 할텐데...
			
			// list.add!
			DTOList.add(wtDTO);
		}
		warehouseTransactionMapper.insertWarehouseTransactionFromList(DTOList);
		if (dto.isForceError()) {
			LogUtil.error(log, "DB 작업 도중 에러 발생 {}", DTOList);
			throw new RuntimeException("강제 오류 발생 → rollback 테스트");
		}
	}
}
