package kr.co.itwillbs.de.approval.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class MessageSearchDTO {
    private LocalDateTime startDate;   // 시작일
    private LocalDateTime endDate;     // 종료일
    private String senderId;           // 발신자 ID
    private String receiverId;         // 수신자 ID
    private String type;               // 알림 유형
    
    @Builder
    public MessageSearchDTO(LocalDateTime startDate, LocalDateTime endDate, String senderId, String receiverId, String type) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.type = type;
    }
}

