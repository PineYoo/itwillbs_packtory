package kr.co.itwillbs.de.groupware.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import kr.co.itwillbs.de.groupware.entity.Message;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MessageDTO {

    private Long idx; // PK

    @NotEmpty(message = "알림 유형은 필수 입력 값입니다.")
    private String type; // 알림 유형

    @NotEmpty(message = "제목은 필수 입력 값입니다.")
    private String title; // 제목

    @NotEmpty(message = "내용은 필수 입력 값입니다.")
    private String contents; // 내용

    @NotEmpty(message = "발신자는 필수 입력 값입니다.")
    private String senderId; // 발신자

    @NotEmpty(message = "수신자는 필수 입력 값입니다.")
    private String receiverId; // 수신자

    private String status;     // 읽음 여부
    private String fileIdxs;   // 첨부파일 ID 리스트
    private String isDeleted;  // 삭제 여부

    private LocalDateTime sendDate;    // 발신일자
    private LocalDateTime receiveDate; // 수신일자

    @Builder
    public MessageDTO(Long idx, String type, String title, String contents,
                      String senderId, String receiverId,
                      String status, String fileIdxs, String isDeleted,
                      LocalDateTime sendDate, LocalDateTime receiveDate) {
        this.idx = idx;
        this.type = type;
        this.title = title;
        this.contents = contents;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.status = status;
        this.fileIdxs = fileIdxs;
        this.isDeleted = isDeleted;
        this.sendDate = sendDate;
        this.receiveDate = receiveDate;
    }

    // DTO -> 엔티티 변환
    public Message toEntity() {
        return Message.builder()
                .type(type)
                .title(title)
                .contents(contents)
                .status(status)
                .fileIdxs(fileIdxs)
                .isDeleted(isDeleted)
                .senderId(senderId)
                .receiverId(receiverId)
                .sendDate(sendDate != null ? sendDate : LocalDateTime.now())
                .receiveDate(receiveDate)
                .build();
    }

    // 엔티티 -> DTO 변환
    public static MessageDTO fromEntity(Message entity) {
        return MessageDTO.builder()
                .idx(entity.getIdx())
                .type(entity.getType())
                .title(entity.getTitle())
                .contents(entity.getContents())
                .senderId(entity.getSenderId())
                .receiverId(entity.getReceiverId())
                .status(entity.getStatus())
                .fileIdxs(entity.getFileIdxs())
                .isDeleted(entity.getIsDeleted())
                .sendDate(entity.getSendDate())
                .receiveDate(entity.getReceiveDate())
                .build();
    }
}
