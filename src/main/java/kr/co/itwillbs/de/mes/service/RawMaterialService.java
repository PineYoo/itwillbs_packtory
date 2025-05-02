package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.common.util.LogUtil;
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
		LogUtil.logStart(log);

		rawMaterialMapper.insertMasterMaterial(rawMaterialDTO);
	}

	// 마스터 자재 조회 + 페이징
	public int searchMasterMaterialCount(RawMaterialSearchDTO searchDTO) {
		LogUtil.logStart(log);

		return rawMaterialMapper.MasterMaterialCount(searchDTO);
	}

	// 마스터 자재 조회
	public List<RawMaterialDTO> getMasterMaterialList(RawMaterialSearchDTO searchDTO) {
		LogUtil.logStart(log);

		return rawMaterialMapper.MasterMaterial(searchDTO);
	}

	// 마스터 자재 상세 조회
	public RawMaterialDTO getMasterMaterial(Long idx) {
		LogUtil.logStart(log);

		return rawMaterialMapper.getMasterMaterialByIdx(idx);
	}

	// 마스터 자재 정보 수정
	@LogExecution
	@Transactional
	public void updateMasterMaterial(RawMaterialDTO rawMaterialDTO) {
		LogUtil.logStart(log);
		rawMaterialMapper.updateMasterMaterial(rawMaterialDTO);
	}

	// 부속 자재 조회
	public List<RawMaterialDTO> getSubMaterialsByIdx(Long idx) {
		LogUtil.logStart(log);
		
		return rawMaterialMapper.getSubMaterialsByIdx(idx);
	}

	// 부속 자재 등록
	@LogExecution
	@Transactional
	public void insertSubMaterial(RawMaterialDTO rawMaterialDTO) {
		LogUtil.logStart(log);
		
		rawMaterialMapper.insertSubMaterial(rawMaterialDTO);
	}

	// 부속 자재 정보 수정
	@LogExecution
	@Transactional
	public void updateSubMaterial(RawMaterialDTO rawMaterialDTO) {
		LogUtil.logStart(log);
		
		rawMaterialMapper.updateSubMaterial(rawMaterialDTO);
	}

	// 자재 정보 담아 가기 (외부용)
	public List<RawMaterialDTO> getRawMaterialList() {
		LogUtil.logStart(log);
		
		return rawMaterialMapper.selectRawMaterialList();
	}
}
