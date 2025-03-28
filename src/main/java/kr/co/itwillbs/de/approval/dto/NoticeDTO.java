package kr.co.itwillbs.de.approval.dto;

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
	
	private String fileIdxs;
	
	private String regId;
	
	private String regDate;
	
	private String modId;
	
	private String modDate;
}
