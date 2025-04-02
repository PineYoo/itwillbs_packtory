package kr.co.itwillbs.de.approval.service;

import kr.co.itwillbs.de.approval.dto.PolicyDTO;
import kr.co.itwillbs.de.approval.dto.PolicySearchDTO;
import kr.co.itwillbs.de.approval.entity.Policy;
import kr.co.itwillbs.de.approval.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyService {
    private final PolicyRepository policyRepository;

    // 규정 등록
    public void registerPolicy(PolicyDTO policyDTO) {
        log.info("registerPolicy --- start");
        policyRepository.save(policyDTO.toEntity());
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

    // 규정 상세 조회
    public PolicyDTO getPolicyByIdx(Long idx) {
        log.info("getPolicyById --- id: {}", idx);
        return policyRepository.findById(idx)
                .map(Policy::toDto)
                .orElseThrow(() -> new RuntimeException("해당 규정을 찾을 수 없습니다."));
    }

    // 규정 수정
    @Transactional
    public void updatePolicy(Long idx, PolicyDTO policyDTO) {
        log.info("updatePolicy --- id: {}", idx);
        Policy policy = policyRepository.findById(idx)
                .orElseThrow(() -> new RuntimeException("해당 규정을 찾을 수 없습니다."));
        policy.updateFromDTO(policyDTO);
    }

    // 규정 삭제 (소프트 삭제 개념으로 게시 여부만 변경)
    @Transactional
    public void changePolicyStatus(Long idx, String status) {
        log.info("changePolicyStatus --- id: {}, status: {}", idx, status);
        Policy policy = policyRepository.findById(idx)
                .orElseThrow(() -> new RuntimeException("해당 규정을 찾을 수 없습니다."));
        policy.setStatus(status);  // "DRAFT"로 변경
    }
}
