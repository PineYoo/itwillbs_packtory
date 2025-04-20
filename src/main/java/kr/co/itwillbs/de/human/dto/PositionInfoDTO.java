package kr.co.itwillbs.de.human.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import kr.co.itwillbs.de.common.aop.annotation.RequiredSessionIds;
import kr.co.itwillbs.de.human.entity.PositionInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredSessionIds(fields = {"regId", "modId"})
public class PositionInfoDTO {

    private Long idx; // 테이블 인덱스

    @NotEmpty(message = "직급 코드는 필수 입력 값입니다.")
    private String positionCode;
    
    @NotEmpty(message = "직급 이름은 필수 입력 값입니다.")
    private String positionName;

    @NotEmpty(message = "정렬 순서는 필수 입력 값입니다.")
    private String rankNumber;

    @NotEmpty(message = "관리자 여부는 필수 입력 값입니다.")
    private String isManager;

    @NotEmpty(message = "삭제 여부는 필수 입력 값입니다.")
    private String isDeleted;

    @NotEmpty(message = "작성자는 필수 입력 값입니다.")
    private String regId;

    private LocalDateTime regDate;  // 작성일자 (자동 생성됨)

    @NotEmpty(message = "최종작성자는 필수 입력 값입니다.")
    private String modId;

    private LocalDateTime modDate;  // 최종 수정일자 (자동 수정됨)

    @Builder
    public PositionInfoDTO(Long idx, String positionCode, String positionName, String rankNumber, String isManager, 
                           String isDeleted, String regId, LocalDateTime regDate, 
                           String modId, LocalDateTime modDate) {
        this.idx = idx;
        this.positionCode = positionCode;
        this.positionName = positionName;
        this.rankNumber = rankNumber;
        this.isManager = isManager;
        this.isDeleted = isDeleted;
        this.regId = regId;
        this.regDate = regDate;
        this.modId = modId;
        this.modDate = modDate;
    }

    public PositionInfo toEntity() {
        return PositionInfo.builder()
                .positionCode(positionCode)
                .positionName(positionName)
                .rankNumber(rankNumber)
                .isManager(isManager)
                .isDeleted(isDeleted)
                .regId(regId)
                .regDate(regDate != null ? regDate : LocalDateTime.now())  // regDate가 null이면 현재 시간으로 설정
                .modId(modId)
                .modDate(modDate != null ? modDate : LocalDateTime.now())  // modDate가 null이면 현재 시간으로 설정
                .build();
    }

    // 엔티티에서 DTO를 생성하는 메서드
    public static PositionInfoDTO fromEntity(PositionInfo positionInfo) {
        return PositionInfoDTO.builder()
                .idx(positionInfo.getIdx())
                .positionCode(positionInfo.getPositionCode())
                .positionName(positionInfo.getPositionName())
                .rankNumber(positionInfo.getRankNumber())
                .isManager(positionInfo.getIsManager())
                .isDeleted(positionInfo.getIsDeleted())
                .regId(positionInfo.getRegId())
                .regDate(positionInfo.getRegDate())
                .modId(positionInfo.getModId())
                .modDate(positionInfo.getModDate())
                .build();
    }
}
