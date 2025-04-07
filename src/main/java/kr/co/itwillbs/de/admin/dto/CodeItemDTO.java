package kr.co.itwillbs.de.admin.dto;

import java.time.LocalDateTime;

import kr.co.itwillbs.de.common.aop.annotation.RequiredSessionIds;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredSessionIds(fields = {"regId", "modId"})
public class CodeItemDTO {
/**
 * 공통코드 DTO CommonCodeItemsDTO 너무 길어서 Common 제외함
 */
	private String idx;
	private String majorCode;
	private String minorCode;
	private String minorName;
	private String description;
	private String isDeleted;
	private String rankNumber;
	private String regId;
	private LocalDateTime regDate;
	private String modId;
	private LocalDateTime modDate;
}
