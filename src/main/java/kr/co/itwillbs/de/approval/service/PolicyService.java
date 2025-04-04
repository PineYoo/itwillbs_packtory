package kr.co.itwillbs.de.approval.service;

import kr.co.itwillbs.de.approval.dto.PolicyDTO;
import kr.co.itwillbs.de.approval.dto.PolicySearchDTO;
import kr.co.itwillbs.de.approval.entity.Policy;
import kr.co.itwillbs.de.approval.repository.PolicyRepository;
import kr.co.itwillbs.de.common.service.FileService;
import kr.co.itwillbs.de.common.util.FileUtil;
import kr.co.itwillbs.de.common.vo.FileVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyService {
    
    private final PolicyRepository policyRepository;
    private final FileService fileService;
    private final FileUtil fileUtil;
    
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
        log.info("updatePolicy --- start");

        Policy policy = policyRepository.findById(idx)
                .orElseThrow(() -> new EntityNotFoundException("규정을 찾을 수 없습니다"));
        
        policy.updateFromDTO(policyDTO);

        // ✅ 파일도 같이 저장
        if (policyDTO.getUploadFiles() != null && !policyDTO.getUploadFiles().isEmpty()) {
            addPolicyFiles(String.valueOf(idx), policyDTO.getUploadFiles());
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
        	deletePolicyFiles(idx);
    
        }
    }
    
    // 파일 업로드
    @Transactional
    public void addPolicyFiles(String majorIdx, List<MultipartFile> files) {
        List<FileVO> fileList = files.stream()
                .filter(file -> StringUtils.hasLength(file.getOriginalFilename()))
                .map(file -> {
                    try {
                        FileVO fileVO = fileUtil.setFile(file);
                        fileVO.setMajorIdx(majorIdx);
                        fileVO.setType("t_policy");
                        fileVO.setIsDeleted("N");
                        return fileVO;
                    } catch (Exception e) {
                        log.error("파일 처리 중 오류", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        if (!fileList.isEmpty()) {
            fileService.registerFiles(fileList);
        }
    }
    
    // 파일 삭제
    @Transactional
    public void deletePolicyFiles(Long policyIdx) {
        List<FileVO> files = fileService.getFilesByMajorIdx("t_policy", policyIdx);
        files.forEach(file -> {
            file.setIsDeleted("Y");
            fileService.removeFile(file);
        });
    }
}