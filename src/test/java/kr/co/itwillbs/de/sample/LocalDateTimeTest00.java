package kr.co.itwillbs.de.sample;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeTest00 {

	public static void main(String... args) {
		
		
		LocalDateTime start = LocalDateTime.parse("2025-04-28T14:57:49", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		LocalDateTime end = LocalDateTime.parse("2025-03-21 09:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);

		System.out.println("start : " + start);
		System.out.println("end : " + end);
	}
}
