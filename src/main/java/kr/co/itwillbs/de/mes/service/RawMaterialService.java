package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.mes.dto.RawMaterialDTO;
import kr.co.itwillbs.de.mes.dto.RawMaterialSearchDTO;
import kr.co.itwillbs.de.mes.mapper.RawMaterialMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RawMaterialService {

	private final RawMaterialMapper rawMaterialMapper; // 원자재 관련 Mapper 주입

	// 원자재 등록
	@LogExecution
	@Transactional
	public void registerRawMaterial(RawMaterialDTO rawMaterialDTO) {
		log.info("원자재 등록 요청: {}", rawMaterialDTO);
		rawMaterialMapper.insertRawMaterial(rawMaterialDTO);
		log.info("원자재 등록 완료 - name: {}", rawMaterialDTO.getName());
	}

	// 원자재 총 개수 (검색 조건 포함)
	public int searchRawMaterialCount(RawMaterialSearchDTO searchDTO) {
		log.info("원자재 개수 조회 - 검색 조건: {}", searchDTO);
		return rawMaterialMapper.searchRawMaterialCount(searchDTO);
	}

	// 원자재 목록 조회 (검색 + 페이징)
	public List<RawMaterialDTO> getRawMaterialList(RawMaterialSearchDTO searchDTO) {
		log.info("원자재 목록 조회 - 검색 조건: {}", searchDTO);
		return rawMaterialMapper.searchRawMaterial(searchDTO);
	}

	// 원자재 상세 조회
	public RawMaterialDTO getRawMaterial(Long idx) {
		log.info("원자재 상세 조회 - idx: {}", idx);
		return rawMaterialMapper.getRawMaterialByIdx(idx);
	}

	// 원자재 수정
	@LogExecution
	@Transactional
	public void updateRawMaterial(RawMaterialDTO rawMaterialDTO) {
		log.info("원자재 수정 요청 - idx: {}", rawMaterialDTO.getIdx());
		rawMaterialMapper.updateRawMaterial(rawMaterialDTO);
		log.info("원자재 수정 완료 - name: {}", rawMaterialDTO.getName());
	}

	// 원자재 삭제 (Soft delete)
	@Transactional
	public void deleteRawMaterial(Long idx) {
		log.info("원자재 삭제 요청 - idx: {}", idx);
		rawMaterialMapper.deleteRawMaterial(idx);
	}

	/*
	 * ===== 필요하면 쓰세요 =====
	 * 원자재 정보 담아 가기 (외부용)
	 */
	public RawMaterialDTO getRawMaterialById(Long idx) {
		log.info("원자재 들고 가기 - idx: {}", idx);
		return rawMaterialMapper.selectRawMaterialById(idx);
		/*
		 * ex) RawMaterialDTO rawMaterial = rawMaterialService.getRawMaterialById(rawMaterialIdx);
		 */
	}
}
