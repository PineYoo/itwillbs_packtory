package kr.co.itwillbs.de.commute.dto;

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
public class CommuteStatusDTO {
    private String workDate;
    private int totalCount;
    private int lateCount;
}
