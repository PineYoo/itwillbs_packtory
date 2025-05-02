package kr.co.itwillbs.de.mes.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import kr.co.itwillbs.de.common.aop.annotation.RequiredSessionIds;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@RequiredSessionIds(fields = { "regId", "modId" })
public class WarehouseTransactionDTO {

	private Long idx; // 테이블 인덱스

	@NotBlank(message = "타입은 필수 입력 값입니다.")
	private String transType; // 타입
	private String transTypeName; // 타입이름

	@NotBlank(message = "코드는 필수 입력 값입니다.")
	private String code; // 코드

//	@NotBlank(message = "상품정보는 필수 입력 값입니다.") // 둘중 하나만 있으면 됨
	private String productIdx; // 상품_idx
	private String productName; // 상품이름

//	@NotBlank(message = "자재정보는 필수 입력 값입니다.") // 둘중 하나만 있으면 됨
	private String materialIdx; // 자재_idx
	private String materialName; // 자재이름

	private String unit; // 단위
	private String unitName;
	private Integer quantity; // 개수
	private String status; // 상태
	private String statusName;

	private String manufacturingDate; // 제조일자
	private String sourceLocation; // 출발지 idx값
	private String sourceLocationName; // 출발지 이름
	private String destinationLocation; // 도착지 idx값
	private String destinationLocationName; // 도착지 이름

	private String memo; // 메모
	private String isDeleted; // 삭제유무

	private String regId; // 작성자
	private LocalDate regDate; // 작성일자시간
	private String modId; // 최종 작성자
	private LocalDate modDate; // 최종작성일자

	// t_raw_material.qc_type 품질 검사 변수 추가
	private String qcType;

	// 페이징 변수들
	private int totalCount;
	private int rowAsc;
	private int rowNum;
}
