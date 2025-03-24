package kr.co.itwillbs.de.commute.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.co.itwillbs.de.commute.dto.CommuteDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "commute")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Commute {

	@Id
	@Column(name = "idx")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idx; // 테이블 인덱스(컬럼명: idx)
	
	private String employeeId; // 사원번호
	// => @Column 어노테이션에 name 속성 생략 시 변수명을 컬럼명으로 활용하는데
	//	이 때, 하이버네이트 기본 네이밍 전략인 스네이크 표기법에 의해 단어 사이의 구분으로 '_' 사용하고 모두 소문자로 표기됨
	//	ex) itemNm => item_nm
	
	private String workStatusCode; // 근무상태코드
	
	// Spring Data JPA - auditing
	@CreatedDate	// 엔티티 생성 시점에 생성 시간 자동 기록
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") // 출력 시 사용할 포멧 지정
	private LocalDateTime regDate; // 날짜 및 시간
	
	// 빌더 패턴(Builder Pattern)으로 객체 생성하기 위한 파라미터 생성자 정의(생성자에 @Builder 어노테이션 추가)
	// => @Id 필드를 제외한 나머지 필드에 대한 정의
	@Builder
	public Commute(String employeeId, String workStatusCode, LocalDateTime regDate) {
		this.employeeId = employeeId;
		this.workStatusCode = workStatusCode;
		this.regDate = regDate;
	}
	
	
	// Commute(엔티티) -> CommuteDTO -> 변환하는 toDTO() 메서드 정의
	public CommuteDTO toDto() { 
		return CommuteDTO.builder()
				.idx(idx)
				.employeeId(employeeId)
				.workStatusCode(workStatusCode)
				.regDate(regDate)
				.build();
	}

	// 같은 값인 데이터이냐? 확인하는 메서드
//	public boolean isEqualsEntityToDto(ItemDTO itemDto) {
//		
//		if(this.id == itemDto.getId()) {
//			return true;
//		}
//		
//		return false;
//	}
	
	// 엔티티를 업데이트 하는 메서드
//	public void changeItem(ItemDTO itemDto) {
//		this.itemNm = itemDto.getItemNm();
//		this.price = itemDto.getPrice();
//		this.stockQty = itemDto.getStockQty();
//		this.itemDetail = itemDto.getItemDetail();
//		this.itemSellStatus = itemDto.getItemSellStatus();
//	}

}
