package kr.co.itwillbs.de.human.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import kr.co.itwillbs.de.human.dto.PositionInfoDTO;
import kr.co.itwillbs.de.human.entity.PositionInfo;
import kr.co.itwillbs.de.human.repository.PositionInfoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PositionInfoService {
    @Autowired
    private PositionInfoRepository positionInfoRepository;

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
    
}
