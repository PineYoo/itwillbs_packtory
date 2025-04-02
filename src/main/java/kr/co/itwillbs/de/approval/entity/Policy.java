package kr.co.itwillbs.de.approval.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import kr.co.itwillbs.de.approval.dto.PolicyDTO;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_policy")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long idx; // PK

    @Column(name = "type", length = 50, nullable = false)
    private String type; // 규정 유형

    @Column(name = "title", length = 100, nullable = false)
    private String title; // 제목

    @Column(name = "contents", length = 5000, nullable = false)
    private String contents; // 내용

    @Column(name = "status", length = 10, nullable = false)
    private String status; // 게시 여부

    @Column(name = "file_idxs", length = 255)
    private String fileIdxs; // 첨부파일 ID 리스트

    @Column(name = "reg_id", length = 20, nullable = false)
    private String regId; // 작성자

    @CreatedDate
    @Column(name = "reg_date", updatable = false)
    private LocalDateTime regDate; // 작성일자

    @Column(name = "mod_id", length = 20)
    private String modId; // 최종 수정자

    @LastModifiedDate
    @Column(name = "mod_date")
    private LocalDateTime modDate; // 최종 수정일자
    
    @PrePersist
    public void prePersist() {
        this.regId = (this.regId == null || this.regId.isEmpty()) ? "-" : this.regId;
        this.modId = (this.modId == null || this.modId.isEmpty()) ? "-" : this.modId;
        this.status = (this.status == null || this.status.isEmpty()) ? "Y" : this.status;
        this.fileIdxs = (this.fileIdxs == null || this.fileIdxs.isEmpty()) ? "-" : this.fileIdxs;
        this.regDate = (this.regDate == null) ? LocalDateTime.now() : this.regDate;
        this.modDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.modDate = LocalDateTime.now(); // 수정 시 최종 수정 날짜 갱신
        
        // null 값 방지 로직 추가
        if (this.status == null || this.status.isEmpty()) {
            this.status = "Y";
        }
        if (this.modId == null || this.modId.isEmpty()) {
            this.modId = "-";
        }
    }

    @Builder
    public Policy(String type, String title, String contents, String status, 
                  String fileIdxs, String regId, LocalDateTime regDate, 
                  String modId, LocalDateTime modDate) {
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

    // DTO 변환 메서드
    public PolicyDTO toDto() {
        return PolicyDTO.builder()
                .idx(idx)
                .type(type)
                .title(title)
                .contents(contents)
                .status(status)
                .fileIdxs(fileIdxs)
                .regId(regId)
                .regDate(regDate)
                .modId(modId)
                .modDate(modDate)
                .build();
    }

    // DTO 기반 엔티티 업데이트 메서드
    public void updateFromDTO(PolicyDTO dto) {
        this.type = dto.getType();
        this.title = dto.getTitle();
        this.contents = dto.getContents();
        this.status = dto.getStatus();
        this.fileIdxs = dto.getFileIdxs();
        this.modId = dto.getModId();
        this.modDate = LocalDateTime.now(); // 수정 시 modDate 갱신
    }
}
