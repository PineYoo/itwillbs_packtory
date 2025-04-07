package kr.co.itwillbs.de.approval.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MessageSearchDTO {
	private LocalDateTime sendDate;			// 발신일
	private LocalDateTime receiveDate;		// 수신일
    private String senderId;				// 발신자 ID
    private String receiverId;         		// 수신자 ID
    private String type;               		// 알림 유형
    
    @Builder
    public MessageSearchDTO(LocalDateTime sendDate, LocalDateTime receiveDate, String senderId, String receiverId, String type) {
        this.sendDate = sendDate;
        this.receiveDate = receiveDate;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.type = type;
    }
}

