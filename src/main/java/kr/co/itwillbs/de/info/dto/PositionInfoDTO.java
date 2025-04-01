package kr.co.itwillbs.de.info.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotEmpty;
import kr.co.itwillbs.de.info.entity.PositionInfo;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PositionInfoDTO {

    private Long idx; // 테이블 인덱스

    @NotEmpty(message = "직급 코드는 필수 입력 값입니다.")
    private String positionCode;

    @NotEmpty(message = "정렬 순서는 필수 입력 값입니다.")
    private String rankNumber;

    @NotEmpty(message = "관리자 여부는 필수 입력 값입니다.")
    private String isManager;

    @NotEmpty(message = "삭제 여부는 필수 입력 값입니다.")
    private String isDeleted;

    @NotEmpty(message = "작성자는 필수 입력 값입니다.")
    private String regId;

    private LocalDateTime regDate;

    @NotEmpty(message = "최종작성자는 필수 입력 값입니다.")
    private String modId;

    private LocalDateTime modDate;

    @Builder
    public PositionInfoDTO(Long idx, String positionCode, String rankNumber, String isManager, 
                           String isDeleted, String regId, LocalDateTime regDate, 
                           String modId, LocalDateTime modDate) {
        this.idx = idx;
        this.positionCode = positionCode;
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
