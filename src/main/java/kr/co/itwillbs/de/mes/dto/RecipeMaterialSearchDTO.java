package kr.co.itwillbs.de.mes.dto;

import java.util.List;

import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.common.vo.PageDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class RecipeMaterialSearchDTO {

	// TODO 25.04.21 레시피 테이블 완성 후 JOIN 걸어서 가져올 이름
	private String recipeMasterName;
	
	private String processIdx;
	private String masterIdx;	// 자재리스트 -> 공정 리스트로 돌아갈 때 필요
	
	private String type;		// 타입
	private String name;		// 이름
	private String unit;		// 단위
	private String mixSeq;		// 혼합순서
	private String isDeleted;	// 삭제여부
	
	private String regStartDate;	// 작성 시작일
	private String regEndDate;		// 작성 종료일
	
	private List<CodeItemDTO> typeList;
	
	// 페이징용 DTO composition
	private PageDTO pageDTO = new PageDTO();
	
}
