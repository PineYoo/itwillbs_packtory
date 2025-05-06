package kr.co.itwillbs.de.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.constant.TransStatus;
import kr.co.itwillbs.de.common.constant.TransType;
import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.common.util.LotNumberUtil;
import kr.co.itwillbs.de.common.vo.QcValidationResult;
import kr.co.itwillbs.de.common.vo.TransferDTO;
import kr.co.itwillbs.de.mes.dto.LotsDTO;
import kr.co.itwillbs.de.mes.dto.QcLogDTO;
import kr.co.itwillbs.de.mes.dto.QcStandardDTO;
import kr.co.itwillbs.de.mes.dto.WarehouseTransactionDTO;
import kr.co.itwillbs.de.mes.dto.WarehouseTransactionLotsDTO;
import kr.co.itwillbs.de.mes.mapper.LotsMapper;
import kr.co.itwillbs.de.mes.mapper.QcLogMapper;
import kr.co.itwillbs.de.mes.mapper.QcStandardMapper;
import kr.co.itwillbs.de.mes.mapper.WarehouseTransactionLotsMapper;
import kr.co.itwillbs.de.mes.mapper.WarehouseTransactionMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QcTransferCommand implements TransferCommand {

	private final WarehouseTransactionMapper warehouseTransactionMapper;
	private final WarehouseTransactionLotsMapper warehouseTransactionLotsMapper;
	private final QcStandardMapper qcStandardMapper;
	private final QcLogMapper qcLogMapper;
	private final LotsMapper lotsMapper;
	
	public QcTransferCommand(WarehouseTransactionMapper warehouseTransactionMapper,
							WarehouseTransactionLotsMapper warehouseTransactionLotsMapper,
								QcLogMapper qcLogMapper,QcStandardMapper qcStandardMapper,
								LotsMapper lotsMapper) {
		
		this.warehouseTransactionMapper = warehouseTransactionMapper;
		this.warehouseTransactionLotsMapper = warehouseTransactionLotsMapper;
		this.qcLogMapper = qcLogMapper;
		this.qcStandardMapper = qcStandardMapper;
		this.lotsMapper = lotsMapper;
	}
	
	/**
	 * <pre>
	 * [품질검사 대기] 에서 사용
	 * t_warehouse_transaction으로 이동하는 서비스
	 * trasnType = '1' and trasnStatus = '1' 인 자재를 보고
	 * QC검사를 작성하고 그 결과를 받아 진행하는 로직
	 * QC검사에 quantity를 입력 받기로 함.
	 * 돌아온 의문은 품질검사 대기 자재가 100개일 때 성공95, 실패5 이렇게 입력 받아야 함
	 * 받을 수 있는 기대 데이터 QclogDTO 그리고 t_warehouse_transaction.idx
	 * ※ 입력 도중 하나라도 에러가 날 경우 롤백이 되어야 한다.
	 * 
	 * 0. 품질 검사 검증 -> validateQcLogs
	 * 1. 입출고 테이블에서 검사장 이동 테이블 인서트
	 * 2. LOT 번호 생성 -> LotNumberUtil.generateLotNumber()
	 * 3. LOT 테이블 인서트 -> 
	 * 4. QC 로그 테이블 인서트
	 * 5. 입출고 테이블 인서트
	 * 6. 입출고-LOT 관계 테이블 인서트
	 * 
	 * 일단 되는대로 작성하고 보니 줄일게 엄청 보이는데 너무 졸리다.
	 * </pre>
	 */
	@Override
	@Transactional(timeout = 10, rollbackFor = Exception.class)
	public void execute(TransferDTO dto) throws Exception {
		LogUtil.logStart(log);
		
		// 검사 항목 가져오기
		List<QcStandardDTO> standardList = qcStandardMapper.selectQcStandardGroupByIdx(dto.getWarehouseIdx());
		// 입력 받은 값가져오기
		List<QcLogDTO> qcResults = dto.getInputQCList();
		// 자재 정보 가져오기
		WarehouseTransactionDTO wtInfo = warehouseTransactionMapper.getWarehouseTransactionByIdx(Long.parseLong(dto.getWarehouseIdx()));
		
		// 0. 품질 검사 검증
		QcValidationResult result = validateQcLogs(standardList, qcResults);
		
		// 1. 입출고 테이블에서 검사장 이동 테이블 인서트 가만히 생각하면 이게 중요한게 아닌데?
		WarehouseTransactionDTO wtDTO = new WarehouseTransactionDTO();
		wtDTO.setTransType(TransType.OUT.toString()); // 창고에서 검사장으로 이동
		wtDTO.setStatus(TransStatus.COMPLETED.toString());
		wtDTO.setMaterialIdx(wtInfo.getMaterialIdx());
		wtDTO.setProductIdx(wtInfo.getProductIdx());
		wtDTO.setQuantity(wtInfo.getQuantity());
		wtDTO.setUnit(wtInfo.getUnit());
		wtDTO.setSourceLocation(IN_DESINATION_LOCATION_1);
		wtDTO.setDestinationLocation(QC_DESINATION_LOCATION_1);
		wtDTO.setMemo("QC_시작_창고to검수장");
		wtDTO.setIsDeleted(IS_DELETE_N);
		wtDTO.setRegId(REG_ID_TRANSFER);
		
		// 디게 복잡하게 느껴질 수 있는데 실제로도 복잡하니까 걱정하지말자
		// Qc 적합 결과가 있을 경우
		if(!result.getSuccessList().isEmpty()) {
			// 2. LOT 번호 생성
			String lotNumber = LotNumberUtil.generateLotNumber();
			
			// 3. LOT 테이블 인서트
			LotsDTO lots = new LotsDTO();
			lots.setLotNumber(lotNumber);
			lots.setProductIdx(wtInfo.getProductIdx());
			lots.setMaterialIdx(wtInfo.getMaterialIdx());
			lots.setQuantity(String.valueOf(result.getAvgSuccessQty())); // 아무리 생각해도 값이 애매해서 평균값으로 함
			lots.setUnit(wtInfo.getUnit());
			lots.setMemo("QC_Pass_Lot");
			lots.setRegId(REG_ID_QC_COMMAND);
			
			lotsMapper.registerLots(lots);
			
			// 4. QC 로그 테이블 인서트 (한건이라고 안함)
			for(QcLogDTO item: result.getSuccessList()) {
				QcLogDTO logDTO = new QcLogDTO();
				logDTO.setQcIdx(item.getQcIdx());
				logDTO.setLotsIdx(lots.getIdx()); // QC_Pass_lot의 idx를 넣음
				logDTO.setQcResult(item.getQcResult());
				logDTO.setQuantity(result.getAvgSuccessQty()); // 아무리 생각해도 값이 애매해서 평균값으로 함2
				logDTO.setUnit(wtInfo.getUnit());
				logDTO.setRegId(REG_ID_QC_COMMAND);
				
				qcLogMapper.insertQcLog(logDTO);
			}
			
			// 5. 입출고 테이블 인서트 -> 검사를 위해 OUT 했으니 다시 IN
			wtDTO.setTransType(TransType.IN.toString());
			wtDTO.setStatus(TransStatus.QC_PASSED.toString()); // QC_Pass 상태!
			wtDTO.setMaterialIdx(wtInfo.getMaterialIdx());
			wtDTO.setProductIdx(wtInfo.getProductIdx());
			wtDTO.setQuantity(result.getAvgSuccessQty()); // 아무리 생각해도 값이 애매해서 평균값으로 함3
			wtDTO.setUnit(wtInfo.getUnit());
			wtDTO.setSourceLocation(QC_DESINATION_LOCATION_1); // 검수장에서
			wtDTO.setDestinationLocation(IN_DESINATION_LOCATION_2); // 창고로
			wtDTO.setMemo("QC 완료 → 창고");
			wtDTO.setIsDeleted(IS_DELETE_N);
			wtDTO.setRegId(REG_ID_QC_COMMAND);
			
			// 6. 입출고-LOT 관계 테이블 인서트
			WarehouseTransactionLotsDTO wtlots = new WarehouseTransactionLotsDTO();
			wtlots.setTransactionIdx(String.valueOf(wtDTO.getIdx())); // 5. 에서 창고로 IN 한 idx값
			wtlots.setLotIdx(lots.getIdx()); // QC_Pass_lot의 idx를 넣음
			wtlots.setQuantity(result.getAvgSuccessQty()); // 아무리 생각해도 값이 애매해서 평균값으로 함4
			wtlots.setUnit(wtInfo.getUnit());
			wtlots.setRegId(REG_ID_QC_COMMAND);
			
			warehouseTransactionLotsMapper.insertWarehouseTransactionLots(wtlots);
		}
		
		// Qc 부적합 결과가 있을 경우
		if(!result.getFailList().isEmpty()) {
			// 2. LOT 번호 생성
			String lotNumber = LotNumberUtil.generateLotNumber();
			
			// 3. LOT 테이블 인서트
			LotsDTO lots = new LotsDTO();
			lots.setLotNumber(lotNumber);
			lots.setProductIdx(wtInfo.getProductIdx());
			lots.setMaterialIdx(wtInfo.getMaterialIdx());
			lots.setQuantity(String.valueOf(result.getAvgFailQty())); // 아무리 생각해도 값이 애매해서 평균값으로 함
			lots.setUnit(wtInfo.getUnit());
			lots.setMemo("QC_fail_LOT");
			lots.setIsDeleted(IS_DELETE_N);
			lots.setRegId(REG_ID_QC_COMMAND);
			
			lotsMapper.registerLots(lots);
			
			// 4. QC 로그 테이블 인서트 (한건이라고 안함)
			for(QcLogDTO item: result.getFailList()) {
				QcLogDTO logDTO = new QcLogDTO();
				logDTO.setQcIdx(item.getQcIdx());
				logDTO.setLotsIdx(lots.getIdx());// QC_fail_LOT의 idx를 넣음
				logDTO.setQcResult(item.getQcResult());
				logDTO.setQuantity(result.getAvgFailQty()); // 아무리 생각해도 값이 애매해서 평균값으로 함2
				logDTO.setUnit(wtInfo.getUnit());
				logDTO.setIsDeleted(IS_DELETE_N);
				logDTO.setRegId(REG_ID_QC_COMMAND);
				
				qcLogMapper.insertQcLog(logDTO);
			}
			
			// 5. 입출고 테이블 인서트 -> 검사를 위해 OUT 했으니 다시 IN
			wtDTO.setTransType(TransType.IN.toString());
			wtDTO.setStatus(TransStatus.QC_FAILED.toString()); // QC_Fail 상태
			wtDTO.setMaterialIdx(wtInfo.getMaterialIdx());
			wtDTO.setProductIdx(wtInfo.getProductIdx());
			wtDTO.setQuantity(result.getAvgFailQty()); // 아무리 생각해도 값이 애매해서 평균값으로 함3
			wtDTO.setUnit(wtInfo.getUnit());
			wtDTO.setSourceLocation(QC_DESINATION_LOCATION_1);
			wtDTO.setDestinationLocation(IN_DESINATION_LOCATION_2);
			wtDTO.setMemo("QC 어쨋든 완료 → 창고");
			wtDTO.setIsDeleted(IS_DELETE_N);
			wtDTO.setRegId(REG_ID_QC_COMMAND);

			warehouseTransactionMapper.insertWarehouseTransaction(wtDTO);
			
			// 6. 입출고-LOT 관계 테이블 인서트
			WarehouseTransactionLotsDTO wtlots = new WarehouseTransactionLotsDTO();
			wtlots.setTransactionIdx(String.valueOf(wtDTO.getIdx())); // 5. 에서 창고로 IN 한 idx값
			wtlots.setLotIdx(lots.getIdx());// QC_Fail_lot의 idx를 넣음
			wtlots.setQuantity(result.getAvgFailQty()); // 아무리 생각해도 값이 애매해서 평균값으로 함4
			wtlots.setUnit(wtInfo.getUnit());
			wtlots.setIsDeleted(IS_DELETE_N);
			wtlots.setRegId(REG_ID_QC_COMMAND);
			
			warehouseTransactionLotsMapper.insertWarehouseTransactionLots(wtlots);
		}

	}
	
	/**
	 * QC 로그들을 검증하고 결과를 단일 리스트로 반환 (성공/실패 정렬)
	 * 
	 * @param standards QC 품질 검사기준
	 * @param logs QC 사용자 입력 값
	 * @return QcValidationResult -> successList, failList 존재
	 */
	private QcValidationResult validateQcLogs(List<QcStandardDTO> standards, List<QcLogDTO> logs) {
		LogUtil.logStart(log);
		
		QcValidationResult result = new QcValidationResult();

		// 표준 값들을 인덱스로 맵핑하여 빠른 접근이 가능하도록 함
		Map<String, QcStandardDTO> standardMap = createStandardMap(standards);

		// 각 로그에 대해 검증 수행
		for (QcLogDTO item : logs) {
			String qcIdx = item.getQcIdx();
			QcStandardDTO standard = standardMap.get(qcIdx);

			if (standard == null) {
				// 해당 인덱스의 표준이 없으면 실패로 처리
				LogUtil.warn(log, "Standard Not found QClog is {}", item);
				item.setQcResult(QC_RESULT_FAIL);
				result.addFail(item);
				continue;
			}

			String value = item.getValue();
			if (standard.isPassed(value)) {
				item.setQcResult(QC_RESULT_SUCCESS);
				result.addSuccess(item);
			} else {
				item.setQcResult(QC_RESULT_FAIL);
				result.addFail(item);
			}
		}
		return result;
	}
	
	/**
	 * 품질 검사기준 맵 생성 (k: t_qc_standard.idx, v: QcStandardDTO)
	 */
	private Map<String, QcStandardDTO> createStandardMap(List<QcStandardDTO> standards) {
		Map<String, QcStandardDTO> standardMap = new HashMap<>();
		
		for (QcStandardDTO standard : standards) {
			standardMap.put(String.valueOf(standard.getIdx()), standard);
		}
		
		return standardMap;
	}
}
