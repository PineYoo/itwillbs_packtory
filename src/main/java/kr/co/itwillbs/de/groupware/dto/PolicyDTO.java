package kr.co.itwillbs.de.groupware.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import kr.co.itwillbs.de.common.aop.annotation.RequiredSessionIds;
import kr.co.itwillbs.de.common.vo.FileVO;
import kr.co.itwillbs.de.groupware.entity.Policy;
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
public class PolicyDTO {

    private Long idx; // PK

    @NotEmpty(message = "규정 유형은 필수 입력 값입니다.")
    private String type; // 규정 유형

    @NotEmpty(message = "제목은 필수 입력 값입니다.")
    private String title; // 제목

    @NotEmpty(message = "내용은 필수 입력 값입니다.")
    private String contents; // 내용

    @NotEmpty(message = "상태는 필수 입력 값입니다.")
    private String status; // 상태

    private String fileIdxs; // 첨부파일 ID 리스트 (선택 입력)

    private String regId; // 작성자

    private LocalDateTime regDate; // 작성일자 (자동 생성됨)

    private String modId; // 최종 수정자

    private LocalDateTime modDate; // 최종 수정일자 (자동 수정됨)
    
    private List<FileVO> fileList;
    
    private List<MultipartFile> policyFiles;
    
    public List<MultipartFile> getUploadFiles() {
        return this.policyFiles;
    }

    @Builder
    public PolicyDTO(Long idx, String type, String title, String contents, String status, 
                     String fileIdxs, String regId, LocalDateTime regDate, 
                     String modId, LocalDateTime modDate) {
        this.idx = idx;
        this.type = type;
        this.title = title;
        this.contents = contents;
        this.status = status;
        this.fileIdxs = fileIdxs;
        this.regId = regId;
        this.regDate = regDate;
        this.modId = modId;
        this.modDate = modDate;
    }

    // DTO - 엔티티 변환 메서드
    public Policy toEntity() {
        return Policy.builder()
                .type(type)
                .title(title)
                .contents(contents)
                .status(status)
                .fileIdxs(fileIdxs)
                .regId(regId)
                .regDate(regDate != null ? regDate : LocalDateTime.now())  // null이면 현재 시간
                .modId(modId)
                .modDate(modDate != null ? modDate : LocalDateTime.now())  // null이면 현재 시간
                .build();
    }

    // 엔티티 - DTO 변환 메서드
    public static PolicyDTO fromEntity(Policy policy) {
        return PolicyDTO.builder()
                .idx(policy.getIdx())
                .type(policy.getType())
                .title(policy.getTitle())
                .contents(policy.getContents())
                .status(policy.getStatus())
                .fileIdxs(policy.getFileIdxs())
                .regId(policy.getRegId())
                .regDate(policy.getRegDate())
                .modId(policy.getModId())
                .modDate(policy.getModDate())
                .build();
    }
}
