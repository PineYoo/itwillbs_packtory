package kr.co.itwillbs.de.admin.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class MenuPermissionDTO {

	// 테이블Seq, PK
	private String idx;
	// 메뉴 이름
	@NotBlank(message = "메뉴는 필수 입력값입니다.")
	private String menuIdx;
	private String menuName;
	
	private String menuParentsIdx;
	private String rankNumber;
	private String url;
	
	private String ownerMemberId;
	private String ownerMemberName;
	
	@NotBlank(message = "그룹은 필수 입력값입니다.")
	private String groupId;	//department_id
	private String groupName; //department_name
	
	@NotBlank(message = "권한값은 필수 입력값입니다.")
	private String permissionCode;
	
	private String description;
	
	@Pattern(regexp = "^[YN]$", message = "사용 유무는 필수 입력값입니다.")
	private String isDeleted;
	
	private String regId;
	private LocalDateTime regDate;
	
	private String modId;
	private LocalDateTime modDate;
	
	// 페이징 변수들
	private int totalCount;
	private int rowAsc;
	private int rowNum;
	
}
