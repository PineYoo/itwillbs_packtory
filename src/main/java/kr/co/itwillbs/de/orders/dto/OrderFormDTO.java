package kr.co.itwillbs.de.orders.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import kr.co.itwillbs.de.common.aop.annotation.RequiredSessionIds;
import kr.co.itwillbs.de.mes.dto.RawMaterialStockDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredSessionIds(fields = {"regId", "modId"})
public class OrderFormDTO { // Wrapper DTO로 묶어버림

//	private OrderDTO orderDTO; // 주문DTO
//	private OrderDetailDTO orderDetailDTO; // 주문 상세DTO
	
	private Long idx; 					// 테이블_인덱스
	private String documentNumber; 		// 문서번호
	private String tradeCode; 			// 거래종류_코드
	private String statusCode; 			// 주문상태_코드
	private String companyName; 		// 거래처명
	private String companyNumber; 		// 사업자번호
	@NotBlank(message = "요청예정일자는 필수 입력 값입니다.")
	private String requestDate; 		// 요청일자
	private String expectedDate; 		// 예정일
	@NotBlank(message = "요청마감일자는 필수 입력 값입니다.")
	private String dueDate; 			// 마감일자
	@NotBlank(message = "목적지명은 필수 입력 값입니다.")
	private String destinationName;		// 목적지명
	@NotBlank(message = "받는사람은 필수 입력 값입니다.")
	private String receiver; 			// 받는사람
	@NotBlank(message = "전화번호1은 필수 입력 값입니다.")
	private String phone1; 				// 전화번호1
	private String phone2; 				// 전화번호2
	@NotBlank(message = "우편번호는 필수 입력 값입니다.")
	private String postCode; 			// 우편번호
	/* @NotBlank(message = "납품주소는 필수 입력 값입니다.") */
	private String address1; 			// 목적지주소1
	@NotBlank(message = "상세주소는 필수 입력 값입니다.")
	private String address2; 			// 목적지주소2
	private String specialIssue; 		// 특이사항
	@Pattern(regexp = "^[YN]$")
	private String isDeleted; 			// 삭제유무
	private String regId; 				// 작성자
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime regDate; 		// 작성일자시간
	private String modId; 				// 최종 작성자
	private LocalDateTime modDate;		// 최종작성일자
	
	//OderDetail
	private Long detailIdx;				// 디테일테이블 _인덱스
	private String departmentCode;		// 담당자 부서코드
	private String subDepartmentCode;	// 담당자 하위부서코드
	private String clientName;			// 담당자 이름
	private String clientPhone;			// 담당자 연락처
	private String lotNumber;			// LOT
	private String invoiceIssue;		// 세금계산서 발행
	private String paymentStatus;		// 수금처리
	private String unissuedStatus;		// 미발행
	private String supplyAmount;		// 공급가액
	private String vatAmount;			// 부가세액
	private String totalAmount;			// 합계금액
	private String issueCode;			// 이슈코드
	private String issueRemaks;			// 이슈비고
	
	@NotEmpty(message = "주문 아이템은 한 개 이상 등록되어야 합니다.")
	private List<OrderItemsDTO> orderItems; // 주문 아이템 리스트
	
	private List<RawMaterialStockDTO> materialStock; // 주문 아이템 리스트

	// OrderItems
//	private Long ItemIdx;				// 아이템테이블 _인덱스
//    private String productCode; 		// 물품코드
//    private String productNumber; 		// 물품번호
//    private String productName; 		// 아이템명
//    private String productValue; 		// 개수
//    private Integer unitPrice; 			// 개수별 금액
//    private Integer totalPrice; 		// 합계액
	
}
