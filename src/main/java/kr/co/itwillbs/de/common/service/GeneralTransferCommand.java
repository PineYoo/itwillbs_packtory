package kr.co.itwillbs.de.common.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.constant.TransStatus;
import kr.co.itwillbs.de.common.constant.TransType;
import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.common.vo.TransferDTO;
import kr.co.itwillbs.de.mes.dto.WarehouseTransactionDTO;
import kr.co.itwillbs.de.mes.dto.WarehouseTransactionLotsDTO;
import kr.co.itwillbs.de.mes.mapper.WarehouseTransactionLotsMapper;
import kr.co.itwillbs.de.mes.mapper.WarehouseTransactionMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GeneralTransferCommand implements TransferCommand {

	private final WarehouseTransactionMapper warehouseTransactionMapper;
	private final WarehouseTransactionLotsMapper warehouseTransactionLotsMapper;
	
	
	public GeneralTransferCommand(WarehouseTransactionMapper warehouseTransactionMapper,
								WarehouseTransactionLotsMapper warehouseTransactionLotsMapper) {
		this.warehouseTransactionMapper = warehouseTransactionMapper;
		this.warehouseTransactionLotsMapper = warehouseTransactionLotsMapper;
	}
	
	@Override
	@Transactional(timeout = 10, rollbackFor = Exception.class)
	public void execute(TransferDTO dto) throws Exception {
		LogUtil.logStart(log);
		
		switch (dto.getMoveType()) {
			case TO_PRODUCTION -> moveToProduction(dto);
			case TO_FINISHED_GOODS -> moveToFinishedGoods(dto);
			default -> throw new UnsupportedOperationException("Unknown move type");
		}
		
	}
	
	/**
	 * <pre>
	 * [작업지시서 등록 후 자재 이동]
	 * QC가 끝난 자재들이란 조건으로 생산공정으로 자재 이동이 가능하다
	 * t_warehouse_transaction.idx가 여전히 키이지만
	 * transType = '1' 입고된 and status = '6' qc_pass 상태
	 * 
	 * 이제부터는 LOT이 나온 자재이기 때문에 t_warehouse_transaction_lot 테이블도 입력해야한다!
	 * 1. t_warehouse_transaction 인서트
	 * 2. t_warehouse_transaction_lots 인서트
	 * </pre>
	 */
	private void moveToProduction(TransferDTO dto) {
		// 작업 전에 QC_PASS 자재들의 t_lots 테이블의 idx까지 조인해서 가져와야 한다.
		// DTO도 새로 만들어야 할것 같다. 이제부터 lot기반 여러테이블들의 조인된 필드값을 가질테니
		
		// 1. 입출고 테이블에서 생산공장으로 자재 이동
		WarehouseTransactionDTO wtDTO = new WarehouseTransactionDTO();
		wtDTO.setTransType(TransType.OUT.toString());
		wtDTO.setStatus(TransStatus.TO_PRODUCTION.toString()); // 창고 -> 생산
//		wtDTO.setMaterialIdx(lotInfo.getMaterialIdx());
//		wtDTO.setProductIdx(lotInfo.getProductIdx());
//		wtDTO.setQuantity(lotInfo.getQuantity());
//		wtDTO.setUnit(lotInfo.getUnit());
		wtDTO.setSourceLocation(IN_DESINATION_LOCATION_2);
		wtDTO.setDestinationLocation(""); // 이건 작업지시서 생산라인에 따라 바뀌는 정보
		wtDTO.setMemo("생산공장1인지 2인지 메모에 남겨야할까?");
		wtDTO.setIsDeleted(IS_DELETE_N);
		wtDTO.setRegId(REG_ID_TRANSFER);

		warehouseTransactionMapper.insertWarehouseTransaction(wtDTO);
		// 2. LOT-입출고 관계 기록
		WarehouseTransactionLotsDTO wtlots = new WarehouseTransactionLotsDTO();
		wtlots.setTransactionIdx(String.valueOf(wtDTO.getIdx())); // 5. 에서 창고로 IN 한 idx값
//		wtlots.setLotIdx(lotInfo.getIdx()); // QC_Pass_lot의 idx를 넣음
//		wtlots.setQuantity(lotInfo.getAvgSuccessQty()); // 아무리 생각해도 값이 애매해서 평균값으로 함4
//		wtlots.setUnit(lotInfo.getUnit());
		wtlots.setRegId(REG_ID_TRANSFER);
		
		warehouseTransactionLotsMapper.insertWarehouseTransactionLots(wtlots);
	}
	
	/**
	 * <pre>
	 * [작업이 끝난 후 원부자재+상품을 다시 창고로 이동]
	 * 
	 * 이제부터는 LOT이 나온 자재이기 때문에 t_warehouse_transaction_lot 테이블도 입력해야한다!
	 * 1. t_warehouse_transaction 인서트
	 * 2. t_warehouse_transaction_lots 인서트
	 * </pre>
	 */
	private void moveToFinishedGoods(TransferDTO dto) {
		
	}
}
