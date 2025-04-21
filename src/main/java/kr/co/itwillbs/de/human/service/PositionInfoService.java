package kr.co.itwillbs.de.human.service;

import java.time.LocalDateTime;
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

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.human.dto.PositionInfoDTO;
import kr.co.itwillbs.de.human.dto.PositionInfoSearchDTO;
import kr.co.itwillbs.de.human.entity.PositionInfo;
import kr.co.itwillbs.de.human.repository.PositionInfoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PositionInfoService {
    @Autowired
    private PositionInfoRepository positionInfoRepository;

    @LogExecution
    // 직급 등록
    public void registerPosition(PositionInfoDTO positionInfoDTO) {
        log.info("registerPosition --- start");
        
        positionInfoRepository.save(positionInfoDTO.toEntity());
    }


    // 직급 조회
    public List<PositionInfoDTO> getPositionList() {
        log.info("getPositionList --- start");
        
        return positionInfoRepository.findByIsDeleted("N")
                .stream().map(PositionInfo::toDto)
                .collect(Collectors.toList());
    }

	/**
	 * SearchDTO를 가져와서 리스트 조건 조회하기
	 * @param searchDTO
	 * @return
	 */
	public Page<PositionInfoDTO> getPositionsBySearchDTO(PositionInfoSearchDTO searchDTO) {
		LogUtil.logStart(log);
		
		Pageable pageable = searchDTO.getPageDTO().toPageable(Sort.by("idx").descending());
		
		Specification<PositionInfo> spec = (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (searchDTO.getPositionCode() != null && !searchDTO.getPositionCode().isBlank()) {
				predicates.add(cb.equal(root.get("positionCode"), searchDTO.getPositionCode()));
			}
			if (searchDTO.getPositionName() != null && !searchDTO.getPositionName().isBlank()) {
				predicates.add(cb.like(root.get("positionName"), "%" + searchDTO.getPositionName() + "%"));
			}
			if (searchDTO.getIsManager() != null && !searchDTO.getIsManager().isBlank()) {
				predicates.add(cb.equal(root.get("isManager"), searchDTO.getIsManager()));
			}
			if (searchDTO.getIsDeleted() != null && !searchDTO.getIsDeleted().isBlank()) {
				predicates.add(cb.equal(root.get("isDeleted"), searchDTO.getIsDeleted()));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};
		
		Page<PositionInfo> result = positionInfoRepository.findAll(spec, pageable);
		
		return result.map(PositionInfo::toDto);
	}
	
    // 특정 직급 코드로 필터링된 목록 조회
    public List<PositionInfoDTO> getPositionListByCode(String positionCode) {
        log.info("getPositionListByCode --- positionCode: {}", positionCode);

        return positionInfoRepository.findByPositionCodeAndIsDeleted(positionCode, "N")
                .stream().map(PositionInfo::toDto)
                .collect(Collectors.toList());
    }

    // 인덱스로 상세 조회
    public PositionInfoDTO getPositionByIdx(Long idx) {
        log.info("getPositionByIdx --- start");
        
        return positionInfoRepository.findById(idx)
                .map(PositionInfo::toDto)
                .orElseThrow(() -> new EntityNotFoundException("직급을 찾을 수 없습니다"));
    }

    // 직급 수정
    @LogExecution
    @Transactional
    public void updatePosition(Long idx, PositionInfoDTO positionInfoDTO) {
        log.info("updatePosition --- start");
        
        PositionInfo positionInfo = positionInfoRepository.findById(idx)
                .orElseThrow(() -> new EntityNotFoundException("직급을 찾을 수 없습니다"));
        positionInfo.updateFromDTO(positionInfoDTO);
    }

    // 직급 삭제
    @Transactional
    public void softDeletePosition(Long idx) {
        log.info("softDeletePosition --- start");
        
        positionInfoRepository.softDeleteById(idx, LocalDateTime.now());
    }
    
    // EmployeeService에서 씀
    public List<PositionInfo> getValidPositions() {
        log.debug("getValidPositions() 호출됨");
        return positionInfoRepository.findValidPositions();
    }

}
