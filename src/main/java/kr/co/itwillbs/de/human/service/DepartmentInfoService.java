package kr.co.itwillbs.de.human.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.common.util.LogUtil;
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
			String deptName = codeItems.stream().filter(item -> item.getMinorCode().equals(entity.getDepartmentCode()))
					.map(CodeItemDTO::getMinorName).findFirst().orElse(null);

			dto.setDepartmentName(deptName);

			return dto;
		}).collect(Collectors.toList());
	}

	/**
	 * SearchDTO를 가져와서 리스트 조건 조회하기
	 * @param searchDTO
	 * @return
	 */
	public Page<DepartmentInfoDTO> getDepartmentsBySearchDTO(DepartmentInfoSearchDTO searchDTO) {
		LogUtil.logStart(log);
		
		Pageable pageable = searchDTO.getPageDTO().toPageable(Sort.by("idx").descending());

		Specification<DepartmentInfo> spec = (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (searchDTO.getDepartmentCode() != null && !searchDTO.getDepartmentCode().isBlank()) {
				predicates.add(cb.equal(root.get("departmentCode"), searchDTO.getDepartmentCode()));
			}
			if (searchDTO.getChildCode() != null && !searchDTO.getChildCode().isBlank()) {
				predicates.add(cb.like(root.get("childCode"), "%" + searchDTO.getChildCode() + "%"));
			}
			if (searchDTO.getLocationIdx() != null && !searchDTO.getLocationIdx().isBlank()) {
		        predicates.add(cb.like(root.get("locationIdx"), "%" + searchDTO.getLocationIdx() + "%"));
		    }

			return cb.and(predicates.toArray(new Predicate[0]));
		};

		Page<DepartmentInfo> result = departmentInfoRepository.findAll(spec, pageable);

		return result.map(DepartmentInfo::toDto);
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

	// 하위 부서 코드 조회
	public List<DepartmentCodeDTO> getSubDepartmentCodes(String departmentCode) {
		return departmentInfoRepository.findSubDepartmentsByParentCode(departmentCode);
	}
}
