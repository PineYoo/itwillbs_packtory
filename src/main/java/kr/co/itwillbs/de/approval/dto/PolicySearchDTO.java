package kr.co.itwillbs.de.approval.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PolicySearchDTO {
    private String type;      // 규정 유형
    private String title;     // 제목
    private String regId;     // 작성자 (사원번호)
    
    @Builder
    public PolicySearchDTO(String type, String title, String regId) {
        this.type = type;
        this.title = title;
        this.regId = regId;
    }
}
