package kr.co.itwillbs.de.mes.dto;

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
public class LotsDTO {
	
	/* TODO @NotBlank @Pattern ...
	 * 추가 해야함
	 */
	
	private String idx;
	//	계층 참조 idx
	private String parentsIdx;
	//	타입
	private String lotNumber;
	//	상품 idx
	private String productIdx;
	//	자재 idx
	private String materialIdx;
	//	작업지시 문서번호
	private String workOrderDocumentNumber;
	//	프로세스 코드
	private String processCode;
	//	단위
	private String unit;
	//	개수
	private String quantity;
	//	제조일자
	private String manufacturingDate;
	//	메모
	private String memo;
	
	private String isDeleted;
	private String regId;
	private String regDate;
	private String modId;
	private String modDate;
	
	// 페이징 변수들
	private int totalCount;
	private int rowAsc;
	private int rowNum;
	

}
