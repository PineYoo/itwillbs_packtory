package kr.co.itwillbs.de.info.entity;

import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.*;
import kr.co.itwillbs.de.info.dto.PositionInfoDTO;
import lombok.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_position_info")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class PositionInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long idx; // 테이블 인덱스

    @Column(name = "position_code", length = 10, nullable = false)
    private String positionCode; // 직급 코드

    @Column(name = "rank_number", length = 10, nullable = false)
    private String rankNumber; // 정렬 순서

    @Column(name = "is_manager", length = 2, nullable = false)
    private String isManager; // 관리자 여부 (Y/N)

    @Column(name = "is_deleted", length = 2, nullable = false)
    private String isDeleted; // 삭제 여부 (Y/N)

    @Column(name = "reg_id", length = 50, nullable = false)
    private String regId; // 작성자

    @CreatedDate
    private LocalDateTime regDate; // 작성일자시간 (자동 생성)

    @Column(name = "mod_id", length = 50, nullable = false)
    private String modId; // 최종작성자

    @LastModifiedDate
    private LocalDateTime modDate; // 최종작성일자시간 (자동 수정)
    
    @PrePersist
    public void prePersist() {
        this.regId = (this.regId == null || this.regId.isEmpty()) ? "-" : this.regId;
        this.modId = (this.modId == null || this.modId.isEmpty()) ? "-" : this.modId;
        this.isDeleted = (this.isDeleted == null || this.isDeleted.isEmpty()) ? "N" : this.isDeleted;
        this.regDate = (this.regDate == null) ? LocalDateTime.now() : this.regDate;
        this.modDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.modDate = LocalDateTime.now(); // 수정 시 최종 수정 날짜 갱신
        
        // null 값 방지 로직 추가
        if (this.isDeleted == null || this.isDeleted.isEmpty()) {
            this.isDeleted = "N";
        }
        if (this.modId == null || this.modId.isEmpty()) {
            this.modId = "-";
        }
    }

    @Builder
    public PositionInfo(String positionCode, String rankNumber, String isManager, String isDeleted,
                        String regId, LocalDateTime regDate, String modId, LocalDateTime modDate) {
        this.positionCode = positionCode;
        this.rankNumber = rankNumber;
        this.isManager = isManager;
        this.isDeleted = isDeleted;
        this.regId = regId;
        this.regDate = regDate;
        this.modId = modId;
        this.modDate = modDate;
    }

    public PositionInfoDTO toDto() {
        return PositionInfoDTO.builder()
                .idx(idx)
                .positionCode(positionCode)
                .rankNumber(rankNumber)
                .isManager(isManager)
                .isDeleted(isDeleted)
                .regId(regId)
                .regDate(regDate)
                .modId(modId)
                .modDate(modDate)
                .build();
    }

    // 직급 수정 메서드 (DTO로부터 데이터 반영)
    public void updateFromDTO(PositionInfoDTO dto) {
        this.positionCode = dto.getPositionCode();
        this.rankNumber = dto.getRankNumber();
        this.isManager = dto.getIsManager();
        // regId와 regDate는 수정하지 않음
        this.isDeleted = dto.getIsDeleted();
        this.modId = dto.getModId();
        this.modDate = LocalDateTime.now(); // 최종 수정일자는 항상 현재시간으로 갱신
    }
}
