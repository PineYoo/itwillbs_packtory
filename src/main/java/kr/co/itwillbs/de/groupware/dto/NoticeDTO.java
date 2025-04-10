package kr.co.itwillbs.de.groupware.dto;

import java.time.LocalDateTime;

import kr.co.itwillbs.de.sample.constant.IsDeleted;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDTO {
	
	private String idx;
	
	private String type;
	
	private String title;
	
	private String contents;
	
	private String isBanner;
	
	private String isDeleted;
	
	private String readCount;
	
	private String regId;
	
	private String regName;
	
	private String regDate;
	
	private String modId;
	
	private String modName;
	
	private String modDate;
	
	//	공통코드 타입으로 조회한 코드이름 저장용
	//	Mapper에서 Alias 써서 codeName 을 가져옴
	private String codeName;
}
