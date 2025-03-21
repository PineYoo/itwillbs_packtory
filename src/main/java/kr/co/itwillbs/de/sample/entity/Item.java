package kr.co.itwillbs.de.sample.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.co.itwillbs.de.sample.constant.ItemSellStatus;
import kr.co.itwillbs.de.sample.dto.ItemDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "item")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Item {

	@Id
	@Column(name = "item_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; // 상품코드(컬럼명: item_id)
	
	@Column(length = 50, nullable = false)
	private String itemNm; // 상품명
	// => @Column 어노테이션에 name 속성 생략 시 변수명을 컬럼명으로 활용하는데
	//	이 때, 하이버네이트 기본 네이밍 전략인 스네이크 표기법에 의해 단어 사이의 구분으로 '_' 사용하고 모두 소문자로 표기됨
	//	ex) itemNm => item_nm
	
	@Column(nullable = false)
	private int price; //가격
	
	@Column(nullable = false)
	private int stockQty; //재고수량
	
	@Column(nullable = false)
	private String itemDetail; //상품 상세 설명
	
	// 상품판매상태값은 enum 객체와 연결하여 관리하며, enum 값을 문자열로 관리하기 위한 설정 추가
	@Enumerated(EnumType.STRING)
	private ItemSellStatus itemSellStatus; // 상품판매상태
	
	// Spring Data JPA - auditing
	@CreatedDate	// 엔티티 생성 시점에 생성 시간 자동 기록
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") // 출력 시 사용할 포멧 지정
	private LocalDateTime regTime; // 상품등록일시
	
	@LastModifiedDate	// 엔티티 수정 되는 시점에 수정 시간 자동 기록
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") // 출력 시 사용할 포멧 지정
	private LocalDateTime updateTime; // 상품 수정일시
	
	// 빌더 패턴(Builder Pattern)으로 객체 생성하기 위한 파라미터 생성자 정의(생성자에 @Builder 어노테이션 추가)
	// => @Id 필드를 제외한 나머지 필드에 대한 정의
	@Builder
	public Item(String itemNm, int price, int stockQty, String itemDetail, ItemSellStatus itemSellStatus,
			LocalDateTime regTime, LocalDateTime updateTime) {
		this.itemNm = itemNm;
		this.price = price;
		this.stockQty = stockQty;
		this.itemDetail = itemDetail;
		this.itemSellStatus = itemSellStatus;
		this.regTime = regTime;
		this.updateTime = updateTime;
	}
	
	// Item(엔티티) -> ItemDTO -> 변환하는 toDTO() 메서드 정의
	public ItemDTO toDto() { 
		return ItemDTO.builder()
				.id(id)
				.itemNm(itemNm)
				.price(price)
				.stockQty(stockQty)
				.itemDetail(itemDetail)
				.itemSellStatus(itemSellStatus)
				.regTime(regTime)
				.updateTime(updateTime)
				.build();
	}

	// 같은 값인 데이터이냐? 확인하는 메서드
	public boolean isEqualsEntityToDto(ItemDTO itemDto) {
		
		if(this.id == itemDto.getId()) {
			return true;
		}
		
		return false;
	}
	
	// 엔티티를 업데이트 하는 메서드
	public void changeItem(ItemDTO itemDto) {
		this.itemNm = itemDto.getItemNm();
		this.price = itemDto.getPrice();
		this.stockQty = itemDto.getStockQty();
		this.itemDetail = itemDto.getItemDetail();
		this.itemSellStatus = itemDto.getItemSellStatus();
	}
}
