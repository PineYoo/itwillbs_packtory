package kr.co.itwillbs.de.info.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.info.entity.PositionInfo;

import org.springframework.data.repository.query.Param;

@Repository
public interface PositionInfoRepository extends JpaRepository<PositionInfo, Long> {

    // 삭제되지 않은 직급 목록만 조회
    List<PositionInfo> findByIsDeleted(String isDeleted);

    // 특정 직급 코드로 필터링된 목록 조회
    List<PositionInfo> findByPositionCodeAndIsDeleted(String positionCode, String isDeleted);

//    @Transactional
//    @Modifying
//    @Query("UPDATE PositionInfo p SET p.isDeleted = 'Y', p.modDate = :modDate WHERE p.idx = :idx")
//    void softDeleteById(@Param("idx") Long idx, @Param("modDate") LocalDateTime modDate);
    
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE PositionInfo p SET p.isDeleted = 'Y', p.modDate = :modDate WHERE p.idx = :idx")
    void softDeleteById(@Param("idx") Long idx, @Param("modDate") LocalDateTime modDate);
}
