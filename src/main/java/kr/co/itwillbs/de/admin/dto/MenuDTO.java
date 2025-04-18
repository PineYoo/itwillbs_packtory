package kr.co.itwillbs.de.admin.dto;

import java.time.LocalDateTime;

import kr.co.itwillbs.de.common.aop.annotation.RequiredSessionIds;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@RequiredSessionIds(fields = {"regId", "modId"})
public class MenuDTO {

	// 테이블 PK
	private Long idx;
	// 메뉴 이름
	private String menuName;
	// 메뉴 ID 계층구조
	private String parentsIdx;
	// 메뉴 정렬 순서
	private String rankNumber;
	// 메뉴 url
	private String url;
	// description 메뉴 설명
	private String description;
	
	private String isDeleted;
	
	private String regId;
	private LocalDateTime regDate;
	
	private String modId;
	private LocalDateTime modDate;
	
}
