package kr.co.itwillbs.de.admin.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class MemberViewDTO {

	private List<MemberDTO> memberDTOList;
	private List<CodeItemDTO> roleItemList;
	private List<CodeItemDTO> statusItemList;
}
