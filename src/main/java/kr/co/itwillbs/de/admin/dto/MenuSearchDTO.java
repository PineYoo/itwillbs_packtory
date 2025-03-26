package kr.co.itwillbs.de.admin.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class MenuSearchDTO {

	// 테이블 PK
	private String idx;
	// 메뉴 타입(공통코드화 해야할듯?)
	private String menuType;
	// 메뉴 ID 2뎁스 이상 있을 때 묶음? 근데 이 이름이 맞아?
	private String menuId;
	// 메뉴 url
	private String url;
	
	private String isDeleted;
	
	private String regId;
	private LocalDateTime regDate;
	
	private String modId;
	private LocalDateTime modDate;
}
