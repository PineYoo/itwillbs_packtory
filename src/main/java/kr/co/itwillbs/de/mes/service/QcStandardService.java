package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.mes.dto.QcStandardDTO;
import kr.co.itwillbs.de.mes.dto.QcStandardSearchDTO;
import kr.co.itwillbs.de.mes.dto.WarehouseTransactionDTO;
import kr.co.itwillbs.de.mes.dto.WarehouseTransactionSearchDTO;
import kr.co.itwillbs.de.mes.mapper.QcStandardMapper;
import kr.co.itwillbs.de.mes.mapper.WarehouseTransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class QcStandardService {

	private final QcStandardMapper qcStandardMapper;
	private final WarehouseTransactionMapper warehouseTransactionMapper;

	// 품질 등록
	@LogExecution
	@Transactional
	public String insertQcStandard(QcStandardDTO qcStandardDTO) {
		log.info("품질 등록 요청: {}", qcStandardDTO);
		qcStandardMapper.insertQcStandard(qcStandardDTO);
		log.info("품질 등록 완료 - name: {}", qcStandardDTO.getName());
		return "redirect:/mes/qcstandard";
	}

	// 품질 총 개수 (검색 조건 포함)
	public int searchQcStandardCount(QcStandardSearchDTO searchDTO) {
		log.info("품질 개수 조회 - 검색 조건: {}", searchDTO);
		return qcStandardMapper.searchQcStandardCount(searchDTO);
	}

	// 품질 목록 조회 (검색 + 페이징)
	public List<QcStandardDTO> searchQcStandard(QcStandardSearchDTO searchDTO) {
		log.info("품질 목록 조회 - 검색 조건: {}", searchDTO);
		return qcStandardMapper.searchQcStandard(searchDTO);
	}

	// 품질 상세 조회
	public QcStandardDTO getQcStandardByIdx(Long idx) {
		log.info("품질 상세 조회 - idx: {}", idx);
		return qcStandardMapper.getQcStandardByIdx(idx);
	}

	// 품질 수정
	@LogExecution
	@Transactional
	public void updateQcStandard(QcStandardDTO qcStandardDTO) {
		log.info("품질 수정 요청 - idx: {}", qcStandardDTO.getIdx());

		// productDTO가 null이 아닌지 체크
		if (qcStandardDTO != null) {
			// 품질 정보를 업데이트하는 쿼리 호출
			qcStandardMapper.updateQcStandard(qcStandardDTO);
			log.info("품질 수정 완료 - name: {}", qcStandardDTO.getName());
		}
	}

	// 품질기준 목록 들고가기 (외부용)
	public List<QcStandardDTO> getQcStandardList() {
		return qcStandardMapper.selectQcStandardList();
	}
	
	// 품질기준 목록 들고가기 (외부용)
	public List<QcStandardDTO> selectQcStandardGroupByIdx(String idx, boolean b) {
		return qcStandardMapper.selectQcStandardGroupByIdx(idx, b);
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

}