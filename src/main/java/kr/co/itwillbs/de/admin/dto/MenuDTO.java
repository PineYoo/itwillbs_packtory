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
	// 메뉴 타입(공통코드화 해야할듯?)
	private String menuType;
	// 메뉴 ID 2뎁스 이상 있을 때 묶음? 근데 이 이름이 맞아?
	private String menuId;
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
	
	public MenuDTO(String menuType, String menuId, String rankNumber, String url, String isDeleted) {
		this.menuType = menuType;
		this.menuId = menuId;
		this.rankNumber = rankNumber;
		this.url = url;
		this.isDeleted = isDeleted;
	}
	
}
