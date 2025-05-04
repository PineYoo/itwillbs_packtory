package kr.co.itwillbs.de.mes.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.common.util.LotNumberUtil;
import kr.co.itwillbs.de.common.util.StringUtil;
import kr.co.itwillbs.de.mes.dto.LotsDTO;
import kr.co.itwillbs.de.mes.dto.QcLogDTO;
import kr.co.itwillbs.de.mes.dto.QcLogFormDTO;
import kr.co.itwillbs.de.mes.dto.QcLogSearchDTO;
import kr.co.itwillbs.de.mes.dto.QcRequiredLogDTO;
import kr.co.itwillbs.de.mes.dto.QcRequiredSearchDTO;
import kr.co.itwillbs.de.mes.dto.QcStandardDTO;
import kr.co.itwillbs.de.mes.dto.WarehouseTransactionDTO;
import kr.co.itwillbs.de.mes.mapper.LotsMapper;
import kr.co.itwillbs.de.mes.mapper.QcLogMapper;
import kr.co.itwillbs.de.mes.mapper.QcStandardMapper;
import kr.co.itwillbs.de.mes.mapper.WarehouseTransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class QcLogService {

	private final QcLogMapper qcLogMapper;
	private final QcStandardMapper qcStandardMapper;
	private final WarehouseTransactionMapper warehouseTransactionMapper;
	private final LotsMapper lotsMapper;

	// 품질로그 등록
	@LogExecution
	@Transactional
	public void insertQcLog(QcLogDTO qcLogDTO) {
		LogUtil.logStart(log);

		qcLogMapper.insertQcLog(qcLogDTO);
	}

	// 품질로그 총 개수 (검색 조건 포함)
	public int searchQcLogCount(QcLogSearchDTO searchDTO) {
		LogUtil.logStart(log);

		return qcLogMapper.QcLogCount(searchDTO);
	}

	// 품질로그 목록 조회 (검색 + 페이징)
	public List<QcLogDTO> searchQcLog(QcLogSearchDTO searchDTO) {
		LogUtil.logStart(log);

		return qcLogMapper.QcLog(searchDTO);
	}

	// 품질로그 상세 조회
	public QcLogDTO getQcLogByIdx(Long idx) {
		LogUtil.logStart(log);

		return qcLogMapper.getQcLogByIdx(idx);
	}

	// 품질로그 수정
	@LogExecution
	@Transactional
	public void updateQcLog(QcLogDTO qcLogDTO) {
		LogUtil.logStart(log);

		qcLogMapper.updateQcLog(qcLogDTO);
	}

	// =======================================품질 검사 조회 , 등록, 수정?
	/**
	 * 품질 검사 항목 카운트 - 페이징용
	 * 
	 * @param searchDTO
	 * @return
	 */
	public int getRequiredQCCount(QcRequiredSearchDTO searchDTO) {
		LogUtil.logStart(log);

		return warehouseTransactionMapper.getRequiredQCCountBySearchDTO(searchDTO);
	}

	/**
	 * 품질 검사 항목 조회
	 * 
	 * @param searchDTO status = {"1", "5"} 둘 중 하나가 필요함
	 * @return
	 */
	public List<WarehouseTransactionDTO> getRequiredQCListBySearchDTO(QcRequiredSearchDTO searchDTO) {
		LogUtil.logStart(log);

		return warehouseTransactionMapper.getRequiredQCListBySearchDTO(searchDTO);
	}

	/**
	 * 품질 검사 기준에서 자재, 상품에 따라 검사해야 할 검사 리스트 가져오기
	 * 
	 * @param idx
	 * @param b
	 * @return
	 */
	public List<QcStandardDTO> selectQcStandardGroupByIdx(String idx) {
		return qcStandardMapper.selectQcStandardGroupByIdx(idx);
	}

	/**
	 * 품질 검사 진행하기
	 * 
	 * <pre>
	 * 1) QcLogFormDTO.qcList > 입력 받은 품질 검사 항목들을 검사해야한다.
	 * 2) 모든 검사가 정상일 경우 LotNumber 생성하기
	 * 3) t_lots 테이블 작성
	 *  3 조건추가) 입고하자마자 lots번호 남기기로 했다. 그래서 lot테이블 작성시 paretns 찾아서 해야한다.
	 * 4) LotNumber를 가지고 t_qc_log 테이블 작성
	 * 5) 입고/출고 자재일 경우 transaction_lot 어쩌구에 남기기로 하지 않았지?
	 * </pre>
	 * 
	 * @param formDTO
	 */
	public void validatingQCs(QcLogFormDTO formDTO) {
		LogUtil.logStart(log);
		LogUtil.logDetail(log, "currentLotNumber is {}", LotNumberUtil.generateLotNumber());
		
		//검사 항목 가져오기
		List<QcStandardDTO> standardList = selectQcStandardGroupByIdx(formDTO.getIdx());
		Map<String, QcStandardDTO> standardMap = new HashMap<>();
		for(QcStandardDTO item: standardList) {
			LogUtil.logDetail(log, "standardList.item is {}", StringUtil.objToString(item));
			standardMap.put(String.valueOf(item.getIdx()), item);
		}
		
		List<QcLogDTO> inputList = formDTO.getQcList();
		// 입력받은 데이터들 반복으로 돌면서 검사랑 비교하기
		int passCnt = 0;
		for(int i=0; i < inputList.size(); i++) {
			QcLogDTO inputObj = (QcLogDTO)inputList.get(i);
			QcStandardDTO standard =standardMap.get(String.valueOf(inputObj.getQcIdx()));
			if(standard == null) {
				// 없는 경우가 있을 경우 ? 일단 얘는 실패
				inputObj.setQcResult("2");
			} else {
				if(standard.isPassed(inputObj.getValue())) {
					inputObj.setQcResult("1");
					passCnt += 1;
				}else {
					inputObj.setQcResult("2");
				}
			}
		} // end of for(int i=0; i < inputList.size(); i++) {
		
		
		String lotIdx = "";
		// 2) 모든 검사가 정상일 경우 LotNumber 생성하기 
		if(passCnt == inputList.size()) {
			String lotnumber = LotNumberUtil.generateLotNumber();
			
			// warehouse_transaction 에서 status 1이나 5에 해당하는 idx 데이터 가져오기
			WarehouseTransactionDTO wtDTO = warehouseTransactionMapper.getWarehouseTransactionByIdx(Long.parseLong(formDTO.getIdx()));
			
			// lots 테이블에 데이터 입력 후 idx 가져오기
			LotsDTO lotsDTO = new LotsDTO(); // 생성자로 바로 만들기, 빌더로 만들기, 세터로 만들기
			lotsDTO.setLotNumber(lotnumber);
			lotsDTO.setQuantity(String.valueOf(wtDTO.getQuantity()));
			lotsDTO.setUnit(wtDTO.getUnit());
			
			// 3) t_lots 테이블 작성
			// 부모 값 가져와서 idx -> parentsIdx 에 넣어야 한다.
			lotsMapper.registerLots(lotsDTO);
			
			lotIdx = lotsDTO.getIdx();
		}
		
		// 4) LotNumber를 가지고 t_qc_log 테이블 작성 QC결과가 부적합일때도 작성은 해야한다.
		for(QcLogDTO item : inputList) {
			item.setLotsIdx(lotIdx);
			qcLogMapper.insertQcLog(item);
		}
		
		//5) 입고/출고 자재일 경우 transaction_lot 어쩌구에 남기기로 하지 않았다고 한다.
		// 어떤 자재는 어떤 검사장을 가는지 규칙이 정해져야 한다.
		// 없으면 전부 하드코딩 될테니까
		// if(status .equal 1 ) sourcelocation 창고고 destination_location 은 무슨 검사장이어야 한다.
		// 심지어 raw_material 에 code 화학식으로 넣어두자 ... -> 원료에 해당하는 화확품은 화학검사장
		// type 원자재는 원자재 검사장 -> 이게 뭐야
		// type 부자재는 부자재 검사장 -> ???
		// 완제품은 완제품 검사장 -> 전부 if 하드코딩 냄새가 풀풀
		// 
		
	}
	
	/**
	 * 품질 검사 기록된 리스트 조회 카운트 - 페이징용
	 * @param searchDTO idx = t_warehouse_transaction.idx
	 * @return
	 */
	public int getRequiredQCLogCountBySearchDTO(QcRequiredSearchDTO searchDTO) {
		LogUtil.logStart(log);
		
		return warehouseTransactionMapper.getRequiredQCLogCountBySearchDTO(searchDTO);
	}

	/**
	 * 품질 검사 기록된 리스트 조회
	 * @param idx
	 * @return
	 */
	public List<QcRequiredLogDTO> getRequiredQCLogListBySearchDTO(QcRequiredSearchDTO searchDTO) {
		return warehouseTransactionMapper.getRequiredQCLogListBySearchDTO(searchDTO);
	}
}