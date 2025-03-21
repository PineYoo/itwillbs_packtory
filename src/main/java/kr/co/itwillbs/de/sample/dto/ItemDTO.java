package kr.co.itwillbs.de.sample.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import kr.co.itwillbs.de.sample.constant.ItemSellStatus;
import kr.co.itwillbs.de.sample.entity.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// DTO 클래스 정의
// 계층간 데이터를 주고 받을 때 Entity 클래스 자체를 사용하지 않고
// 별도의 데이터 전송용 객체인 DTO 사용하는 것이 일반적이다.
// => 요청과 응답 객체 형태가 항상 엔티티와 같지 않기 때문에 계층간 데이터 이동에는 DTO 사용

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ItemDTO {
	
	private Long id; // 상품코드(컬럼명: item_id)
	
	// Validation 의존성 라이브러리를 활용하여 입력값 검증을 수행할 수 있음 => 다양한 어노테이션 활용
	// @NotEmpty : 문자열이 null 이거나 길이가 0인 널스트링("")을 허용하지 않음
	@NotEmpty(message = "상품명은 필수 입력 값입니다.")
	private String itemNm; // 상품명
	
	// @NotNull : 객체가 null 을 허용하지 않음
	@NotNull
	@Positive(message = "가격은 양수만 입력 가능합니다.") // 양수만 허용(0 제외)
	private Integer price; //가격
	
	@NotNull(message = "재고수량은 필수 입력 값입니다.")
	@PositiveOrZero(message = "재고 수량은 0개 이상만 입력 가능합니다.") // 양수만 허용(0 포함)
	@Max(value=9999999, message="재고 수량은 9,999,999개를 초과할 수 없습니다.")
	private Integer stockQty; //재고수량

	// @NotBlank : 문자열이 null 이거나 길이가 0인 널스트링(""), 공백만 허용된 문자열을 허용하지 않음
	@NotBlank(message = "상품상세설명은 필수 입력 값입니다.")
	private String itemDetail; //상품 상세 설명
	
	@NotNull(message = "상품판매상태는 필수 입력 값입니다.")
	private ItemSellStatus itemSellStatus; // 상품판매상태
	
	private LocalDateTime regTime; // 상품등록일시
	private LocalDateTime updateTime; // 상품 수정일시
	
	@Builder
	public ItemDTO(Long id, String itemNm, int price, int stockQty, String itemDetail, ItemSellStatus itemSellStatus,
			LocalDateTime regTime, LocalDateTime updateTime) {
		this.id = id;
		this.itemNm = itemNm;
		this.price = price;
		this.stockQty = stockQty;
		this.itemDetail = itemDetail;
		this.itemSellStatus = itemSellStatus;
		this.regTime = regTime;
		this.updateTime = updateTime;
	}
	
	
	// ItemDTO -> Item(엔티티)로 변환하는 toEntity() 메서드 정의
	public Item toEntity() {
		return Item.builder()
				.itemNm(itemNm)
				.price(price)
				.stockQty(stockQty)
				.itemDetail(itemDetail)
				.itemSellStatus(itemSellStatus)
				.build();
	}
}
