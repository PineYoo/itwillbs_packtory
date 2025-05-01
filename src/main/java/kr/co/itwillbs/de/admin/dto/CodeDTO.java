package kr.co.itwillbs.de.admin.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
	@NotBlank(message = "코드는 필수입니다.")
	private String majorCode;
	
	@NotBlank(message = "이름은 필수입니다.")
	private String name;
	
	@Size(max=50, message = "설명은 50자 이내여야 합니다.")
	private String description;
	
	@Pattern(regexp = "^[YN]$")
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
