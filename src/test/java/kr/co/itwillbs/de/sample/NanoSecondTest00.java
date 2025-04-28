package kr.co.itwillbs.de.sample;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NanoSecondTest00 {

	public static String generate() {
		// 기본 날짜/시간 포맷 (yyyyMMddHHmmss)
		LocalDateTime now = LocalDateTime.now();
		String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

		// 나노초 정보 (마이크로초 포함)
		Instant instant = Instant.now();
		long nanos = instant.getNano();

		// 트랜잭션 ID 생성 - 날짜시간 + 나노초
		return formattedDate + String.format("%09d", nanos);
	}

	public static void main(String... strings) {
		System.out.println("생성된 트랜잭션 ID: " + generate());
	}
}
