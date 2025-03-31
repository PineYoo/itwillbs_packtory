package kr.co.itwillbs.de.info.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import kr.co.itwillbs.de.info.entity.DepartmentInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DepartmentInfoDTO {

    private Long idx; // 인덱스
    
    @NotEmpty(message = "대표부서코드는 필수 입력 값입니다.")
    private String departmentCode;
    
    @NotEmpty(message = "부모코드는 필수 입력 값입니다.")
    private String parentCode;
    
    @NotEmpty(message = "하위부서코드는 필수 입력 값입니다.")
    private String childCode;
    
    @NotEmpty(message = "정렬순서는 필수 입력 값입니다.")
    private String rankNumber;
    
    @NotEmpty(message = "삭제유무는 필수 입력 값입니다.")
    private String isDeleted;
    
    @NotEmpty(message = "장소참조는 필수 입력 값입니다.")
    private String locationIdx;
    
    @NotEmpty(message = "작성자는 필수 입력 값입니다.")
    private String regId;
    
    private LocalDateTime regDate;
    
    @NotEmpty(message = "최종작성자는 필수 입력 값입니다.")
    private String modId;
    
    private LocalDateTime modDate;
    
    @Builder
    public DepartmentInfoDTO(Long idx, String departmentCode, String parentCode, String childCode, String rankNumber,
                               String isDeleted, String locationIdx, String regId, LocalDateTime regDate, String modId,
                               LocalDateTime modDate) {
        this.idx = idx;
        this.departmentCode = departmentCode;
        this.parentCode = parentCode;
        this.childCode = childCode;
        this.rankNumber = rankNumber;
        this.isDeleted = isDeleted;
        this.locationIdx = locationIdx;
        this.regId = regId;
        this.regDate = regDate;
        this.modId = modId;
        this.modDate = modDate;
    }
    
    // DTO를 엔티티로 변환하는 메서드
    public DepartmentInfo toEntity() {
        return DepartmentInfo.builder()
                .departmentCode(departmentCode)
                .parentCode(parentCode)
                .childCode(childCode)
                .rankNumber(rankNumber)
                .isDeleted(isDeleted)
                .locationIdx(locationIdx)
                .regId(regId)
                .regDate(regDate)
                .modId(modId)
                .modDate(modDate)
                .build();
    }
}
