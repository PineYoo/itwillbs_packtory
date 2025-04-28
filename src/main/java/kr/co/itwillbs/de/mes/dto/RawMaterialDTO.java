package kr.co.itwillbs.de.mes.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import kr.co.itwillbs.de.common.aop.annotation.RequiredSessionIds;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@RequiredSessionIds(fields = { "regId", "modId" })
public class RawMaterialDTO {

	private Long idx; // 테이블 인덱스
	private String parentsIdx; // 부모 인덱스
	private String parentsName; // 부모 이름

	private String clientIdx; // 거래처_idx
	private String clientCompanyName; // 거래처명

	@NotBlank(message = "타입은 필수 입력 값입니다.")
	private String type; // 타입
	private String typeName; // 타입명

	@NotBlank(message = "이름은 필수 입력 값입니다.")
	private String name; // 이름

	private Integer quantity; // 개수

	private String unit; // 단위
	private String unitName; // 단위명

	@PositiveOrZero(message = "가격은 0원 이상만 입력 가능합니다.")
	private Integer price; // 가격

	private String leadTime; // 소요기간
	private String expirationDate; // 평균사용기한
	private String storageCondition; // 보관조건
	private String msdsLink; // MSDS링크
	private String isDeleted; // 삭제 여부

	private String regId; // 최초등록자
	private LocalDateTime regDate; // 최초등록일
	private String modId; // 최종수정자
	private LocalDateTime modDate; // 최종수정일
}
