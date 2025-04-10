package kr.co.itwillbs.de.groupware.dto;

import java.util.List;

import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.common.vo.PageDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NoticeSearchDTO {
	
	//	공지사항 유형
	private String type;
	//	공지사항 제목
	private String title;
	//	시작 날짜
	private String regStartDate;
	//	마감 날짜
	private String regEndDate;
	//	삭제유무
	private String isDeleted;
	//	조회수
	private String readCount;
	
//	private List<CodeItemDTO> codeItemList;
	
	// 페이징용 DTO composition
	private PageDTO pageDTO = new PageDTO();
	
	
	
}
