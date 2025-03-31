package kr.co.itwillbs.de.info.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.info.dto.DepartmentInfoDTO;
import kr.co.itwillbs.de.info.dto.DepartmentInfoSearchDTO;
import kr.co.itwillbs.de.info.entity.DepartmentInfo;
import kr.co.itwillbs.de.info.repository.DepartmentInfoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DepartmentInfoService {

	@Autowired
    private DepartmentInfoRepository departmentInfoRepository;
        
	// 부서 등록
    public void registerDepartment(DepartmentInfoDTO departmentInfoDTO) {
        log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
        departmentInfoRepository.save(departmentInfoDTO.toEntity()); // DTO를 Entity로 변환하여 저장
    }

    // 부서 리스트 조회
    public List<DepartmentInfoDTO> getDepartmentList() {
        log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
        List<DepartmentInfo> entityList = departmentInfoRepository.findAll();
        return convertDepartmentToDepartmentDTO(entityList); // Entity 리스트를 DTO 리스트로 변환
    }

    // 부서 검색
    public List<DepartmentInfoDTO> searchDepartments(DepartmentInfoSearchDTO departmentSearchDTO) {
        log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
        List<DepartmentInfo> departmentList = departmentInfoRepository.findBySearchParams(
                departmentSearchDTO.getDepartmentCode(),
                departmentSearchDTO.getChildCode(),
                departmentSearchDTO.getLocationIdx());
        return convertDepartmentToDepartmentDTO(departmentList); // 검색된 부서 리스트 반환
    }

    // 단일 부서 조회 (idx로 조회)
    public DepartmentInfoDTO getDepartmentByIdx(Long idx) {
        log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
        DepartmentInfo departmentInfo = departmentInfoRepository.findById(idx)
                .orElseThrow(() -> new RuntimeException("부서를 찾을 수 없습니다"));
        return departmentInfo.toDto(); // Entity를 DTO로 변환하여 반환
    }

    // 부서 수정
    @Transactional
    public void updateDepartment(DepartmentInfoDTO departmentInfoDTO) {
        log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
        DepartmentInfo departmentInfo = departmentInfoRepository.findById(departmentInfoDTO.getIdx())
                .orElseThrow(() -> new RuntimeException("부서를 찾을 수 없습니다"));
        departmentInfo.updateFromDTO(departmentInfoDTO); // DTO를 통해 Entity 값을 수정
    }

    // 부서 삭제
    public void deleteDepartment(Long idx) {
        log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
        Optional<DepartmentInfo> departmentInfo = departmentInfoRepository.findById(idx);
        if (departmentInfo.isPresent()) {
            departmentInfoRepository.delete(departmentInfo.get()); // 부서 삭제
        } else {
            throw new RuntimeException("부서를 찾을 수 없습니다");
        }
    }

    // Entity 리스트를 DTO 리스트로 변환
    private List<DepartmentInfoDTO> convertDepartmentToDepartmentDTO(List<DepartmentInfo> departmentList) {
        return departmentList.stream()
                .map(DepartmentInfo::toDto)
                .collect(Collectors.toList());
    }
}
