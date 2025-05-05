package kr.co.itwillbs.de.mes.dto;

import org.springframework.util.StringUtils;

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
public class QcStandardDTO {

	private Long idx;
	private String type;
	
	@NotBlank(message = "품질기준 이름은 필수 입력 값입니다.")
	private String name;
	
	private String unit;
	private String targetValue;
	private String minValue;
	private String maxValue;
	private String memo;
	private String isDeleted;
	private String regId;
	private String regDate;
	private String modId;
	private String modDate;
	
	// 25.05.05 추가 필드
	private int quantity;
	private float acceptablePassRate; // QC 통과율.. 여기까지 고민하니까 머리가 너무 아프다
	
	// 페이징 변수들
	private int totalCount;
	private int rowAsc;
	private int rowNum;
	
	/**
	 * <pre>
	 * 품질 검사 기준을 DB에서 가져와 있는 객체이기에 여기에서 검사를 진행 한다.
	 * idx 기준으로 입력값 체크 함 (이렇게 보니 targetValue가 의미가 모호해짐)
	 * </pre>
	 * @param inputValue
	 * @return true: 적합결과, false: 부적합결과
	 */
	public boolean isPassed(String inputValue) {
		if(!StringUtils.hasLength(inputValue)) {
			return false;
		}
		
		// 계산을 하지 않음. 단순 비교연산자이기에 float이면 되지 않을까?
		if(StringUtils.hasLength(minValue) && StringUtils.hasLength(maxValue)) {
			try {
				float value = Float.parseFloat(inputValue);
				
				return value >= Float.parseFloat(minValue) && value <= Float.parseFloat(maxValue);
			} catch (NumberFormatException e) {
				return false; // 이건 얄짤 없이 넘겨도 되나?
			}
		} else {
			return "pass".equalsIgnoreCase(inputValue);
		}
	}
}

