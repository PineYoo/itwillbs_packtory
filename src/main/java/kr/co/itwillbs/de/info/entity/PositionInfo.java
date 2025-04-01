package kr.co.itwillbs.de.info.entity;

import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDate; // 작성일자시간

    @Column(name = "mod_id", length = 50, nullable = false)
    private String modId; // 최종작성자

    @LastModifiedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modDate; // 최종작성일자시간

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
}
