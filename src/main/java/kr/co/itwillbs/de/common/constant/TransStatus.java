package kr.co.itwillbs.de.common.constant;

/**
 * <pre>
 * TODO 25.05.05
 * Enum 객체를 점점 이해 하게 되면서 참 좋은 것이라 느껴진다.
 * 어쩌다보니, 공통코드들을 전부 cache화 해서 사용하고 있기까지하고
 * 하지만 아쉬운 점은
 * 
 * 공통코드 테이블의
 * majorCode 규칙은 거의 국룰급인 "tableName_column" 구조로 되어 있어야 하고
 * minorName 은 간단명료한 명사
 * minorCode 는 이제 실제 사용하는 코드
 * 이어야 하나 ..일단 이렇게 해놓고 시간 나면 공통코드캐시에서 가져오게 해서 사용하도록 바꾸자
 * </pre>
 */
public enum TransStatus {
	STANDBY("1"),
	REQUESTED("2"),
	APPROVED("3"),
	COMPLETED("4"),
	CANCELLED("5"),
	QC_PASSED("6"),
	QC_FAILED("7"),
	TO_QC("8"),
	TO_PRODUCTION("9");
	
	private final String code;

	TransStatus(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return code;
	}
}
