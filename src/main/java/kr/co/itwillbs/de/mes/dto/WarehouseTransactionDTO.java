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
	private String trans_type; // 타입
	
	@NotBlank(message = "코드는 필수 입력 값입니다.")
	private String code; // 코드
	
	@NotBlank(message = "상품정보는 필수 입력 값입니다.")
	private String product_idx; // 상품_idx
	
	@NotBlank(message = "자재정보는 필수 입력 값입니다.")
	private String material_idx; // 자재_idx
	
	private String unit; // 단위
	private Integer quantity; // 개수
	private String status; // 상태
	
	private LocalDate manufacturing_date; // 제조일자
	private String source_location; // 출발지
	private String destination_location; // 도착지
	
	private String memo; // 메모
	private String is_deleted; // 삭제유무
	
	private String reg_id; // 작성자
	private LocalDate reg_date; // 작성일자시간
	private String mod_id; // 최종 작성자
	private LocalDate mod_date; // 최종작성일자
}
