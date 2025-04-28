package kr.co.itwillbs.de.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LotNumberUtil {

	// 싱글톤 패턴
	private static final LotNumberUtil INSTANCE = new LotNumberUtil();
	
	// 포매터 재사용을 위함
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	// 롯 넘버 접두사
	private static final String PREFIX_LOTNUMBER = "lots";
	
	// StringBuilder 재사용을 위한 초기 용량 설정 (날짜 14자리 + 나노초 9자리 = 23자리 + 여유) -> 이러면 이론상 1억분의 1?
	// private final StringBuilder builder = new StringBuilder(30);
	// 그냥 builder로 적당히 하려 했는데 AI님이 이게 쓰레드 환경에서 안전하다 함
	private static final ThreadLocal<StringBuilder> builderThreadLocal = ThreadLocal.withInitial(() -> new StringBuilder(30));

	// 생성자는 숨기고
	private LotNumberUtil() {
	}
	
	// 호출하면 만들어둔 애가 튀어나가게
	public static LotNumberUtil getInstance() {
		return INSTANCE;
	}
	
	public String generateLotNumber() {
		LogUtil.logStart(log);
		StringBuilder builder = builderThreadLocal.get();
		// StringBuilder 초기화 (재사용)
		builder.setLength(0);

		// 접두사 추가
		builder.append(PREFIX_LOTNUMBER);
		
		// 날짜/시간 포맷팅 (미리 생성된 포맷터 사용)
		LocalDateTime now = LocalDateTime.now();
		builder.append(now.format(FORMATTER));

		// 나노초 추가 (0-패딩)
		long nanos = Instant.now().getNano();
		padNanos(builder, nanos);

		return builder.toString();
	}

	// 나노초를 효율적으로 0-패딩
	private void padNanos(StringBuilder sb, long nanos) {
		// 나노초는 최대 9자리
		if (nanos < 10) {
			sb.append("00000000").append(nanos);
		} else if (nanos < 100) {
			sb.append("0000000").append(nanos);
		} else if (nanos < 1000) {
			sb.append("000000").append(nanos);
		} else if (nanos < 10000) {
			sb.append("00000").append(nanos);
		} else if (nanos < 100000) {
			sb.append("0000").append(nanos);
		} else if (nanos < 1000000) {
			sb.append("000").append(nanos);
		} else if (nanos < 10000000) {
			sb.append("00").append(nanos);
		} else if (nanos < 100000000) {
			sb.append("0").append(nanos);
		} else {
			sb.append(nanos);
		}
	}

	public static void main(String[] args) {
		LotNumberUtil generator = LotNumberUtil.getInstance();

		LotNumberUtil a = LotNumberUtil.getInstance();
		LotNumberUtil b = LotNumberUtil.getInstance();
		LotNumberUtil c = LotNumberUtil.getInstance();
		
		// 싱글톤 확인
		System.out.println(a);
		System.out.println(b);
		System.out.println(c);
		
		// 성능 테스트
		long start = System.nanoTime();
		for (int i = 0; i < 100_000; i++) {
			generator.generateLotNumber();
		}
		long end = System.nanoTime();

		System.out.println("생성된 ID 예시: " + generator.generateLotNumber());
		System.out.println("100,000번 생성 소요 시간: " + ((end - start) / 100_000) + "ms");
	}
}
