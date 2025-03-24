package kr.co.itwillbs.de.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
	@PropertySource("classpath:properties/env.yml")
})
public class EnvPropertyConfig {
	/**
	 * application.yml 설정 파일의 노출 값에 대한 고민
	 * <br>방법 1. 별도의 파일을 생성해서 값을 주입시킨다. (href: https://jforj.tistory.com/254)
	 * <br> -> 귀찮지만 운영/개발 초기에 저 파일만 만들어 두면 부트 프로퍼티 노출로 인한 사고는 없을듯
	 * <br>방법 2. 값을 암호화하여 입력하는 방법. (href: https://goddaehee.tistory.com/321)
	 * <br> TODO -> 직관성이 떨어질것 같음. 하지만 테스트는 해보자.
	 * <br>또 다른 방법도 있겠으나, 자료 조사는 이정도로 하고 이 클래스는 방법1을 위함.
	 * <br>@Configuration 에 대해 (href: https://docs.spring.io/spring-boot/reference/using/configuration-classes.html)
	 */
	
}

/*
 * (href: https://programmer93.tistory.com/47)
 * @ConfigurationProperties 사용법
 * 현 프로젝트 프로퍼티의 예를 들면
 * - application.yml 내용
 * spring:
 *   datasource:
 *     hikari:
 *       driver-class-name: com.mysql.cj.jdbc.Driver
 *       jdbc-url: jdbc:mysql://localhost:3306/testdb
 *       username: root
 *       password: 1234
 * - 클래스 내용
 * @ConfigurationProperties(prefix ="spring.datasource.hikari") 
 * class PropertyExample {
 * 	private String driver-class-name;
 * 	private String jdbc-url;
 * 	private String username;
 * 	private String password;
 * }
 * 가물가물 한데 자바 프로퍼티 돌아가듯, 이렇게 클래스 내 필드에 이름 맞춰서 셋 해주는거라 생각이든다.
 * TODO 향후 외부 openAPI 사용할때 프로퍼티화 할때 써봐야겠음 
*/