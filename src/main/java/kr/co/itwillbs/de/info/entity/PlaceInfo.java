package kr.co.itwillbs.de.info.entity;

import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.*;
import kr.co.itwillbs.de.info.dto.PlaceInfoDTO;
import lombok.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_place_info")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class PlaceInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long idx; // 테이블 인덱스

    @Column(name = "location_code", length = 10, nullable = false)
    private String locationCode; // 직급 코드 (장소 코드)

    @Column(name = "post_code", length = 10, nullable = false)
    private String postCode; // 우편번호

    @Column(name = "address1", length = 10, nullable = false)
    private String address1; // 주소1 (기본 주소)

    @Column(name = "address2", length = 2, nullable = false)
    private String address2; // 주소2 (상세 주소)

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
    public PlaceInfo(String locationCode, String postCode, String address1, String address2, String isDeleted,
                     String regId, LocalDateTime regDate, String modId, LocalDateTime modDate) {
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

    public PlaceInfoDTO toDto() {
        return PlaceInfoDTO.builder()
                .idx(idx)
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
