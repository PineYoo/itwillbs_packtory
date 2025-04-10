package kr.co.itwillbs.de.human.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.human.dto.DepartmentCodeDTO;
import kr.co.itwillbs.de.human.dto.DepartmentInfoDTO;
import kr.co.itwillbs.de.human.dto.DepartmentInfoSearchDTO;
import kr.co.itwillbs.de.human.entity.DepartmentInfo;
import kr.co.itwillbs.de.human.repository.DepartmentInfoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DepartmentInfoService {

	@Autowired
	private DepartmentInfoRepository departmentInfoRepository;
	private CommonCodeUtil commonCodeUtil;

	@LogExecution
	// 부서 등록
	public void registerDepartment(DepartmentInfoDTO departmentInfoDTO) {
		log.info("registerDepartment --- start");

		if (departmentInfoDTO.getIsDeleted() == null) {
			departmentInfoDTO.setIsDeleted("N");
		}

		departmentInfoRepository.save(departmentInfoDTO.toEntity());
	}

	// 부서 리스트 조회 (삭제되지 않은 것만)
	public List<DepartmentInfoDTO> getDepartmentList(List<CodeItemDTO> codeItems) {
	    log.info("getDepartmentList --- start (with codeItems)");

	    List<DepartmentInfo> entityList = departmentInfoRepository.findByIsDeleted("N");

	    return entityList.stream().map(entity -> {
	        DepartmentInfoDTO dto = DepartmentInfoDTO.fromEntity(entity);

	        // departmentCode -> 한글명 (minorCode로 비교)
	        String deptName = codeItems.stream()
	            .filter(item -> item.getMinorCode().equals(entity.getDepartmentCode()))
	            .map(CodeItemDTO::getMinorName)
	            .findFirst()
	            .orElse(null);

	        dto.setDepartmentName(deptName);

	        return dto;
	    }).collect(Collectors.toList());
	}


	// 부서 검색 (검색 필터 조건: 대표부서코드, 하위부서코드, 장소참조)
	public List<DepartmentInfoDTO> searchDepartments(DepartmentInfoSearchDTO departmentSearchDTO) {
		log.info("searchDepartments --- start");

		// 빈 문자열을 null로 처리하여 Repository 쿼리에서 조건 무시
		String deptCode = (departmentSearchDTO.getDepartmentCode() != null
				&& !departmentSearchDTO.getDepartmentCode().trim().isEmpty())
						? departmentSearchDTO.getDepartmentCode().trim()
						: null;
		String childCode = (departmentSearchDTO.getChildCode() != null
				&& !departmentSearchDTO.getChildCode().trim().isEmpty()) ? departmentSearchDTO.getChildCode().trim()
						: null;
		String locationIdx = (departmentSearchDTO.getLocationIdx() != null
				&& !departmentSearchDTO.getLocationIdx().trim().isEmpty()) ? departmentSearchDTO.getLocationIdx().trim()
						: null;
		List<DepartmentInfo> entityList = departmentInfoRepository.findBySearchParams(deptCode, childCode, locationIdx);
		return convertDepartmentToDepartmentDTO(entityList);
	}

	// 단일 부서 조회 (idx로 조회)
	public DepartmentInfoDTO getDepartmentByIdx(Long idx) {
		log.info("getDepartmentByIdx --- start");

		DepartmentInfo departmentInfo = departmentInfoRepository.findById(idx)
				.orElseThrow(() -> new RuntimeException("부서를 찾을 수 없습니다"));

		return departmentInfo.toDto();
	}

	// 부서 수정
	@LogExecution
	@Transactional
	public void updateDepartment(DepartmentInfoDTO departmentInfoDTO) {
		log.info("updateDepartment --- start");

		DepartmentInfo departmentInfo = departmentInfoRepository.findById(departmentInfoDTO.getIdx())
				.orElseThrow(() -> new RuntimeException("부서를 찾을 수 없습니다"));

		departmentInfo.updateFromDTO(departmentInfoDTO);

		departmentInfoRepository.save(departmentInfo); // 명시적 저장
	}

	// 부서 삭제 Soft Delete 적용
	@Transactional
	public void softDeleteDepartment(Long idx) {
		log.info("softDeleteDepartment --- start");

		DepartmentInfo departmentInfo = departmentInfoRepository.findById(idx)
				.orElseThrow(() -> new RuntimeException("부서를 찾을 수 없습니다."));

		departmentInfo.setIsDeleted("Y"); // 삭제 상태 변경
	}

	// Entity 리스트를 DTO 리스트로 변환
	private List<DepartmentInfoDTO> convertDepartmentToDepartmentDTO(List<DepartmentInfo> departmentList) {
		return departmentList.stream().map(DepartmentInfo::toDto).collect(Collectors.toList());
	}

	// 하위 부서 코드 조회
	public List<DepartmentCodeDTO> getSubDepartmentCodes(String departmentCode) {
		return departmentInfoRepository.findSubDepartmentsByParentCode(departmentCode);
	}
}
