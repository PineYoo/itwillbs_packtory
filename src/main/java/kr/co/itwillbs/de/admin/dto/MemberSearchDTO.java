package kr.co.itwillbs.de.admin.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberSearchDTO {

	private String memberId;
	private String memberName;
	private String role;
	private String status;
	private String isDeleted;
	private String regId;
	private String regDate;
	
	private List<CodeItemDTO> codeItemList;
}
