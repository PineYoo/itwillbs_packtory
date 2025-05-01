package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.common.util.LotNumberUtil;
import kr.co.itwillbs.de.mes.dto.QcLogDTO;
import kr.co.itwillbs.de.mes.dto.QcLogFormDTO;
import kr.co.itwillbs.de.mes.dto.QcLogSearchDTO;
import kr.co.itwillbs.de.mes.dto.QcStandardDTO;
import kr.co.itwillbs.de.mes.dto.WarehouseTransactionDTO;
import kr.co.itwillbs.de.mes.dto.WarehouseTransactionSearchDTO;
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
	
	// 품질로그 등록
	@LogExecution
	@Transactional
	public String insertQcLog(QcLogDTO qcLogDTO) {
		log.info("품질로그 등록 요청: {}", qcLogDTO);
		qcLogMapper.insertQcLog(qcLogDTO);
		log.info("품질로그 등록 완료 - qcIdx: {}", qcLogDTO.getQcIdx());
		return "redirect:/mes/qclog";
	}

	// 품질로그 총 개수 (검색 조건 포함)
	public int searchQcLogCount(QcLogSearchDTO searchDTO) {
		log.info("품질로그 개수 조회 - 검색 조건: {}", searchDTO);
		return qcLogMapper.searchQcLogCount(searchDTO);
	}

	// 품질로그 목록 조회 (검색 + 페이징)
	public List<QcLogDTO> searchQcLog(QcLogSearchDTO searchDTO) {
		log.info("품질로그 목록 조회 - 검색 조건: {}", searchDTO);
		return qcLogMapper.searchQcLog(searchDTO);
	}

	// 품질로그 상세 조회
	public QcLogDTO getQcLogByIdx(Long idx) {
		log.info("품질로그 상세 조회 - idx: {}", idx);
		return qcLogMapper.getQcLogByIdx(idx);
	}

	// 품질로그 수정
	@LogExecution
	@Transactional
	public void updateQcLog(QcLogDTO qcLogDTO) {
		log.info("품질로그 수정 요청 - idx: {}", qcLogDTO.getIdx());

		// productDTO가 null이 아닌지 체크
		if (qcLogDTO != null) {
			// 품질로그 정보를 업데이트하는 쿼리 호출
			qcLogMapper.updateQcLog(qcLogDTO);
			log.info("품질로그 수정 완료 - qcIdx: {}", qcLogDTO.getQcIdx());
		}
	}
	// =======================================품질 검사 조회 , 등록, 수정?
	/**
	 * 품질 검사 항목 카운트 - 페이징용
	 * @param searchDTO
	 * @return
	 */
	public int getRequiredQCCount(WarehouseTransactionSearchDTO searchDTO) {
		LogUtil.logStart(log);
		
		return warehouseTransactionMapper.getRequiredQCCountBySearchDTO(searchDTO);
	}

	/**
	 * 품질 검사 항목 조회
	 * @param searchDTO status = {"1", "5"} 둘 중 하나가 필요함
	 * @return
	 */
	public List<WarehouseTransactionDTO> getRequiredQCListBySearchDTO(WarehouseTransactionSearchDTO searchDTO) {
		LogUtil.logStart(log);
		
		return warehouseTransactionMapper.getRequiredQCListBySearchDTO(searchDTO);
	}

	/**
	 * 품질 검사 기준에서 자재, 상품에 따라 검사해야 할 검사 리스트 가져오기
	 * @param idx
	 * @param b
	 * @return
	 */
	public List<QcStandardDTO> selectQcStandardGroupByIdx(String idx, boolean b) {
		return qcStandardMapper.selectQcStandardGroupByIdx(idx, b);
	}

	/**
	 * 품질 검사 진행하기
	 * <pre>
	 * 1) QcLogFormDTO.qcList > 입력 받은 품질 검사 항목들을 검사해야한다.
	 * 2) 모든 검사가 정상일 경우 LotNumber 생성하기
	 * 3) LotNumber를 가지고 t_qc_log 테이블 작성
	 * 4) t_lots 테이블 작성
	 * 5) 입고/출고 자재일 경우 transaction_lot 어쩌구에 남기기로 하지 않았지? (이거 내일 다시 물어보자)
	 * </pre>
	 * @param formDTO
	 */
	public void validatingQCs(QcLogFormDTO formDTO) {
		LogUtil.logStart(log);
		LogUtil.logDetail(log, "currentLotNumber is {}", LotNumberUtil.generateLotNumber());
	}
}