package kr.co.itwillbs.de.approval.service;

import kr.co.itwillbs.de.approval.dto.PolicyDTO;
import kr.co.itwillbs.de.approval.dto.PolicySearchDTO;
import kr.co.itwillbs.de.approval.entity.Policy;
import kr.co.itwillbs.de.approval.repository.PolicyRepository;
import kr.co.itwillbs.de.common.service.FileService;
import kr.co.itwillbs.de.common.vo.FileVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyService {
    
    private final PolicyRepository policyRepository;
    private final FileService fileService;
    
    // 규정 등록
    public String registerPolicy(PolicyDTO policyDTO) {
        log.info("registerPolicy --- start");

        // t_policy 테이블에 저장 (저장된 엔티티 반환)
        Policy policy = policyRepository.save(policyDTO.toEntity());

        return String.valueOf(policy.getIdx()); // 생성된 idx 반환
    }

    // 규정 목록 조회 (검색 조건 적용)
    public List<PolicyDTO> getPolicyList(PolicySearchDTO searchDTO) {
        log.info("getPolicyList --- searchDTO: {}", searchDTO);
        
        return policyRepository.searchPublishedPolicies(
                    searchDTO.getType(),
                    searchDTO.getTitle(),
                    searchDTO.getRegId()
                )
                .stream()
                .map(Policy::toDto)
                .collect(Collectors.toList());
    }

    // 규정 상세 조회 (파일 포함)
    public PolicyDTO getPolicyByIdx(Long idx) {
        log.info("getPolicyByIdx --- id: {}", idx);

        PolicyDTO policyDTO = policyRepository.findById(idx)
                .map(Policy::toDto)
                .orElseThrow(() -> new RuntimeException("해당 규정을 찾을 수 없습니다."));

        // 파일 목록 조회 (majorIdx 하드코딩 처리)
        List<FileVO> fileList = fileService.getFilesByMajorIdx("t_policy", idx);
        log.info("조회된 파일 개수: {}", fileList.size());

        for (FileVO file : fileList) {
            log.info("조회된 파일 - ID: {}, 이름: {}, 삭제 여부: {}", file.getIdx(), file.getFileName(), file.getIsDeleted());
        }

        policyDTO.setFileList(fileList);
        return policyDTO;
    }

    // 규정 수정
    @Transactional
    public void updatePolicy(Long idx, PolicyDTO policyDTO) {
        log.info("updatePolicy --- id: {}", idx);

        // 1. 기존 규정 가져오기
        Policy policy = policyRepository.findById(idx)
                .orElseThrow(() -> new RuntimeException("해당 규정을 찾을 수 없습니다."));

        // 2. 규정 내용 업데이트
        policy.updateFromDTO(policyDTO);

        // 3. 파일 리스트 가져오기 (null 방지)
        List<FileVO> fileList = Optional.ofNullable(policyDTO.getFileList()).orElse(Collections.emptyList());
        log.info("파일 리스트 개수: {}", fileList.size());

        // 4. 삭제할 파일 ID 목록 추출 후 일괄 삭제
        List<Long> deleteFileIds = fileList.stream()
                .filter(file -> "Y".equals(file.getIsDeleted()) && file.getIdx() != null)
                .map(file -> Long.valueOf(file.getIdx())) // String → Long 변환
                .collect(Collectors.toList());

        if (!deleteFileIds.isEmpty()) {
            log.info("삭제할 파일 ID 목록: {}", deleteFileIds);
            deleteFileIds.forEach(fileId -> {
                FileVO fileVO = new FileVO();
                fileVO.setIdx(String.valueOf(fileId));
                fileVO.setIsDeleted("Y"); // 삭제 상태로 변경
                
                try {
                    fileService.removeFile(fileVO);
                    log.info("삭제 완료 - 파일 ID: {}", fileId);
                } catch (Exception e) {
                    log.error("파일 삭제 실패 - 파일 ID: {}, 오류: {}", fileId, e.getMessage());
                }
            });
        }
        
        // 5. 새 파일 등록 (기존 파일 제외, 새 파일만 등록, majorIdx 하드코딩 처리)
        List<FileVO> newFiles = fileList.stream()
                .filter(file -> file.getIdx() == null) // ID가 없으면 새 파일
                .peek(fileVO -> {
                    fileVO.setMajorIdx(String.valueOf(idx)); // 규정 ID 설정
                    fileVO.setType("t_policy");
                    fileVO.setIsDeleted("N");
                })
                .collect(Collectors.toList());

        if (!newFiles.isEmpty()) {
            log.info("파일 저장 시작 - 총 {} 개", newFiles.size());
            fileService.registerFiles(newFiles); // 일괄 저장
            log.info("파일 저장 완료");
        } else {
            log.info("저장할 파일 없음");
        }
    }
    
    // 규정 삭제
    @Transactional
    public void changePolicyStatus(Long idx, String status) {
        log.info("changePolicyStatus --- id: {}, status: {}", idx, status);

        // 1. 규정 가져오기
        Policy policy = policyRepository.findById(idx)
                .orElseThrow(() -> new RuntimeException("해당 규정을 찾을 수 없습니다."));

        // 2. 규정 상태 변경
        policy.setStatus(status);
        policyRepository.save(policy);

        // 3. 관련 파일 삭제 처리 (규정 삭제 시)
        if ("N".equals(status)) {
            List<FileVO> files = fileService.getFilesByMajorIdx("t_policy", idx); // 하드코딩 적용
            if (!files.isEmpty()) {
                log.info("연결된 파일 삭제 처리 - 총 {} 개", files.size());
                files.forEach(file -> {
                    file.setIsDeleted("Y"); // 삭제 상태로 변경
                    fileService.removeFile(file);
                });
            }
        }
    }
}