package kr.co.itwillbs.de;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing //Spring Data JPA 의 Auditing 기능 활성화
public class PacktoryDeApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(PacktoryDeApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(PacktoryDeApplication.class);
	}
}
