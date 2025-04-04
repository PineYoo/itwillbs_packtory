package kr.co.itwillbs.de.approval.entity;

import jakarta.persistence.*;
import kr.co.itwillbs.de.approval.dto.MessageDTO;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_message")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "type", length = 50)
    private String type; // 알림 유형

    @Column(name = "title", length = 50)
    private String title; // 제목

    @Column(name = "contents", length = 500)
    private String contents; // 내용

    @Column(name = "status", length = 10)
    private String status; // 읽음 여부

    @Column(name = "file_idxs", length = 50)
    private String fileIdxs; // 첨부파일 ID 리스트

    @Column(name = "is_deleted", length = 2)
    private String isDeleted; // 삭제 여부

    @Column(name = "sender_id", length = 50, nullable = false)
    private String senderId; // 발신자 ID

    @Column(name = "send_date")
    private LocalDateTime sendDate; // 발신일

    @Column(name = "receiver_id", length = 50, nullable = false)
    private String receiverId; // 수신자 ID

    @Column(name = "receive_date")
    private LocalDateTime receiveDate; // 수신일

    @PrePersist
    public void prePersist() {
        this.status = (this.status == null || this.status.isEmpty()) ? "안읽음" : this.status;
        this.fileIdxs = (this.fileIdxs == null || this.fileIdxs.isEmpty()) ? "-" : this.fileIdxs;
        this.isDeleted = (this.isDeleted == null || this.isDeleted.isEmpty()) ? "N" : this.isDeleted;
        this.sendDate = (this.sendDate == null) ? LocalDateTime.now() : this.sendDate;
    }

    @PreUpdate
    public void preUpdate() {
        this.sendDate = (this.sendDate == null) ? LocalDateTime.now() : this.sendDate;
    }

    @Builder
    public Message(String type, String title, String contents, String status,
                   String fileIdxs, String isDeleted, String senderId, LocalDateTime sendDate,
                   String receiverId, LocalDateTime receiveDate) {
        this.type = type;
        this.title = title;
        this.contents = contents;
        this.status = status;
        this.fileIdxs = fileIdxs;
        this.isDeleted = isDeleted;
        this.senderId = senderId;
        this.sendDate = sendDate;
        this.receiverId = receiverId;
        this.receiveDate = receiveDate;
    }

    // DTO 변환 메서드
    public MessageDTO toDto() {
        return MessageDTO.builder()
                .idx(idx)
                .type(type)
                .title(title)
                .contents(contents)
                .status(status)
                .fileIdxs(fileIdxs)
                .isDeleted(isDeleted)
                .senderId(senderId)
                .sendDate(sendDate)
                .receiverId(receiverId)
                .receiveDate(receiveDate)
                .build();
    }

    // DTO 기반 엔티티 업데이트
    public void updateFromDTO(MessageDTO dto) {
        this.type = dto.getType();
        this.title = dto.getTitle();
        this.contents = dto.getContents();
        this.status = dto.getStatus();
        this.fileIdxs = dto.getFileIdxs();
        this.isDeleted = dto.getIsDeleted();
        this.senderId = dto.getSenderId();
        this.sendDate = dto.getSendDate();
        this.receiverId = dto.getReceiverId();
        this.receiveDate = dto.getReceiveDate();
    }
}
