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

	private final RawMaterialMapper rawMaterialMapper; // 마스터 자재 관련 Mapper 주입

	// 마스터 자재 등록
	@LogExecution
	@Transactional
	public void registerMasterMaterial(RawMaterialDTO rawMaterialDTO) {
		log.info("마스터 자재 등록 요청: {}", rawMaterialDTO);
		rawMaterialMapper.insertMasterMaterial(rawMaterialDTO);
		log.info("마스터 자재 등록 완료 - name: {}", rawMaterialDTO.getName());
	}

	// 마스터 자재 조회 + 페이징
	public int searchMasterMaterialCount(RawMaterialSearchDTO searchDTO) {
		log.info("마스터 자재 개수 조회 - 검색 조건: {}", searchDTO);
		return rawMaterialMapper.searchMasterMaterialCount(searchDTO);
	}

	// 마스터 자재 조회
	public List<RawMaterialDTO> getMasterMaterialList(RawMaterialSearchDTO searchDTO) {
		log.info("마스터 자재 목록 조회 - 검색 조건: {}", searchDTO);
		return rawMaterialMapper.searchMasterMaterial(searchDTO);
	}

	// 마스터 자재 상세 조회
	public RawMaterialDTO getMasterMaterial(Long idx) {
		log.info("마스터 자재 상세 조회 - idx: {}", idx);
		return rawMaterialMapper.getMasterMaterialByIdx(idx);
	}

	// 마스터 자재 정보 수정
	@LogExecution
	@Transactional
	public void updateMasterMaterial(RawMaterialDTO rawMaterialDTO) {
		log.info("마스터 자재 수정 요청 - idx: {}", rawMaterialDTO.getIdx());
        // 마스터 자재가 null이 아닌지 체크
        if (rawMaterialDTO != null) {
            // 마스터 자재 정보를 업데이트하는 쿼리 호출
        	rawMaterialMapper.updateMasterMaterial(rawMaterialDTO);
            log.info("마스터 자재 수정 완료 - name: {}", rawMaterialDTO.getName());
        } 
    }
	
	// 부속 자재 조회
	public List<RawMaterialDTO> getSubMaterialsByIdx(Long idx) {
		return rawMaterialMapper.getSubMaterialsByIdx(idx);
	}
	
	// 부속 자재 등록
	@LogExecution
	@Transactional
	public void insertSubMaterial(RawMaterialDTO rawMaterialDTO) {
		rawMaterialMapper.insertSubMaterial(rawMaterialDTO);
	}
	
	// 부속 자재 정보 수정
	@LogExecution
	@Transactional
	public void updateSubMaterial(RawMaterialDTO rawMaterialDTO) {
		rawMaterialMapper.updateSubMaterial(rawMaterialDTO);
	}
	
	// 자재 정보 담아 가기 (외부용)
	public List<RawMaterialDTO> getRawMaterialList() {
		return rawMaterialMapper.selectRawMaterialList();
	}
}
