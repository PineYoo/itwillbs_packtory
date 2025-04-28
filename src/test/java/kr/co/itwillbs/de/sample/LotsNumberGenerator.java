package kr.co.itwillbs.de.sample;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 서버 환경에서 번호 호출 시 정말 1억분의 1확률로 동타이밍이면 동일한 값이 발생 할 수 있겠지? <br>
 * 그래서 싱글톤 패턴으로 얘 하나가 처리하도록 한다.
 */
public class LotsNumberGenerator {

	// 싱글톤 패턴
	private static final LotsNumberGenerator INSTANCE = new LotsNumberGenerator();

	// 포맷터를 static final로 선언하여 재사용
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	// StringBuilder 재사용을 위한 초기 용량 설정 (날짜 14자리 + 나노초 9자리 = 23자리 + 여유)
	// private final StringBuilder builder = new StringBuilder(30);
	// AI 형님이 그래도 쓰레드 환경에서는 이게 더 안전하다고 하신다... 몰랑 무서워
	private static final ThreadLocal<StringBuilder> builderThreadLocal = ThreadLocal
			.withInitial(() -> new StringBuilder(30));

	private LotsNumberGenerator() {
	}

	public static LotsNumberGenerator getInstance() {
		return INSTANCE;
	}

	public String generateLotNumber() {
		StringBuilder builder = builderThreadLocal.get();
		// StringBuilder 초기화 (재사용)
		builder.setLength(0);
		builder.append("lot");
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
		LotsNumberGenerator generator = LotsNumberGenerator.getInstance();

		// 성능 테스트
		long start = System.nanoTime();
		for (int i = 0; i < 100000; i++) {
			generator.generateLotNumber();
		}
		long end = System.nanoTime();

		System.out.println("생성된 ID 예시: " + generator.generateLotNumber());
		System.out.println("100,000번 생성 소요 시간: " + ((end - start) / 1_000_000) + "ms");
	}
}
