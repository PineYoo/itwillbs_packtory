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
public class CodeDTO {
/**
 * 공통코드 DTO CommonCodeDTO 너무 길어서 Common은 제외함
 */
	private String idx;
	private String majorCode;
	private String name;
	private String description;
	private String isDeleted;
	private String regId;
	private LocalDateTime regDate;
	private String modId;
	private LocalDateTime modDate;
}
