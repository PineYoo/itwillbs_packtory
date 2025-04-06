package kr.co.itwillbs.de.sample;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SpringPasswordEncoderTest {

	@Test
	public void testPasswordEncoding() {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		// 암호화 하기전 임시 비밀번호
		String rawPassword = "1234";
		
		// 비밀번호 암호화
		String encodedPassword = passwordEncoder.encode(rawPassword);
		
		// 결과 출력
		System.out.println("rawPassword: " + rawPassword);
		System.out.println("encodedPassword: " + encodedPassword);
		System.out.println("encodedPassword.length(): " + encodedPassword.length());
		
		// 암호화된 비밀번호 검증
		boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
		System.out.println("비밀번호 검증 결과 : " + matches);
		
		// 여러 번 실행해보면 같은 비밀번호라도 다른 해시값이 생성되는 것을 확인할 수 있습니다
		for (int i = 0; i < 3; i++) {
			String newEncodedPassword = passwordEncoder.encode(rawPassword);
			System.out.println("새로운 암호화된 비밀번호 " + (i + 1) + ": " + newEncodedPassword);
			System.out.println("비밀번호 검증 결과: " + passwordEncoder.matches(rawPassword, newEncodedPassword));
		}
		
	}
}
