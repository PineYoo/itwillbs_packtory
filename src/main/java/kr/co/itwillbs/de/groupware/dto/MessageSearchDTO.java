package kr.co.itwillbs.de.groupware.dto;

import java.time.LocalDate;

import kr.co.itwillbs.de.common.vo.PageDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MessageSearchDTO {
	private LocalDate sendDate;			// 발신일
	private LocalDate receiveDate;		// 수신일
    private String senderId;				// 발신자 ID
    private String receiverId;         		// 수신자 ID
    private String type;               		// 알림 유형
    
	// 페이징용 DTO composition
	private PageDTO pageDTO = new PageDTO();
    
    @Builder
    public MessageSearchDTO(LocalDate sendDate, LocalDate receiveDate, String senderId, String receiverId, String type) {
        this.sendDate = sendDate;
        this.receiveDate = receiveDate;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.type = type;
    }
}

