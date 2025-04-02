package kr.co.itwillbs.de.human.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotEmpty;
import kr.co.itwillbs.de.human.entity.PlaceInfo;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PlaceInfoDTO {

    private Long idx; // 테이블 인덱스

    @NotEmpty(message = "장소 코드는 필수 입력 값입니다.")
    private String locationCode;

    @NotEmpty(message = "우편번호는 필수 입력 값입니다.")
    private String postCode;

    @NotEmpty(message = "주소1은 필수 입력 값입니다.")
    private String address1;

    @NotEmpty(message = "주소2는 필수 입력 값입니다.")
    private String address2;

    @NotEmpty(message = "삭제 여부는 필수 입력 값입니다.")
    private String isDeleted;

    @NotEmpty(message = "작성자는 필수 입력 값입니다.")
    private String regId;

    private LocalDateTime regDate;

    @NotEmpty(message = "최종작성자는 필수 입력 값입니다.")
    private String modId;

    private LocalDateTime modDate;

    @Builder
    public PlaceInfoDTO(Long idx, String locationCode, String postCode, String address1, String address2, 
                        String isDeleted, String regId, LocalDateTime regDate, String modId, LocalDateTime modDate) {
        this.idx = idx;
        this.locationCode = locationCode;
        this.postCode = postCode;
        this.address1 = address1;
        this.address2 = address2;
        this.isDeleted = isDeleted;
        this.regId = regId;
        this.regDate = regDate;
        this.modId = modId;
        this.modDate = modDate;
    }

    public PlaceInfo toEntity() {
        return PlaceInfo.builder()
                .locationCode(locationCode)
                .postCode(postCode)
                .address1(address1)
                .address2(address2)
                .isDeleted(isDeleted)
                .regId(regId)
                .regDate(regDate)
                .modId(modId)
                .modDate(modDate)
                .build();
    }
}
