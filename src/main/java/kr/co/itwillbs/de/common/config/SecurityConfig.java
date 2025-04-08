package kr.co.itwillbs.de.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.annotation.PostConstruct;
import kr.co.itwillbs.de.common.securityHandler.CustomAuthenticationFailureHandler;
import kr.co.itwillbs.de.common.securityHandler.CustomAuthenticationSuccessHandler;
import kr.co.itwillbs.de.common.securityHandler.CustomLogoutHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
	private final CustomAuthenticationFailureHandler authenticationFailureHandler;
	private final CustomLogoutHandler logoutHandler;
	
	public SecurityConfig(CustomAuthenticationSuccessHandler authenticationSuccessHandler,
							CustomAuthenticationFailureHandler authenticationFailureHandler,
							CustomLogoutHandler logoutHandler) {
		this.authenticationSuccessHandler = authenticationSuccessHandler;
		this.authenticationFailureHandler = authenticationFailureHandler;
		this.logoutHandler = logoutHandler;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.cors(cors -> cors.disable())
			.httpBasic(httpBasic -> httpBasic.disable())
			.anonymous(anonymous -> anonymous.disable())

			.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
				.requestMatchers("/",
								"/login",
								"/error/**",
								"/js/**", "/css/**", "/img/**"
								).permitAll()
				.requestMatchers("/admin/**").hasAuthority("ADMIN") //.hasRole("ADMIN") // 이녀석은 ROLE_접두사가 붙은 문자열로 표현 hasAuthority는 접두사 붙이지 않음
				//.requestMatchers("/manager/**").hasRole("MANAGER")
				//.requestMatchers("/employee/**").hasRole("EMPLOYEE")
				.anyRequest()
				.authenticated()
			)

			.formLogin(formLogin -> formLogin
				.loginPage("/login")
				.loginProcessingUrl("/login")
				.usernameParameter("memberId") // 시큐리티가 바라는 username이 아니라 DB컬럼에 맞는 값으로 셋 
				.passwordParameter("password") // 이건 선언안해도 되는데 그래도 모양을 보려고 해봄
				//.defaultSuccessUrl("/main") // 왜 로그인 성공 핸들러가 인식이 안되니? 너야?
				.successHandler(authenticationSuccessHandler) // 로그인 성공 시 로그 기록을 위해 로그인핸들러 등록
				.failureHandler(authenticationFailureHandler) // 로그인 실패 시 로그 기록을 위해 로그인실패핸들러 등록
				//.failureUrl("/login?error=true") // 로그인 실패 핸들러 작동을 안하는데? 너니?
				.permitAll()
			)

			.logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/")
				.addLogoutHandler(logoutHandler) // 로그아웃 시 로그 기록을 위해 로그아웃핸들러 등록
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.deleteCookies("JSESSIONID") // 쿠키 삭제
				.permitAll()
			)
			
			.sessionManagement(sessionManagement -> sessionManagement
				.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 기본 값이지만 선언해 놓자
				.maximumSessions(2) // 동시 세션 수 제한 테스트니 3개
				.maxSessionsPreventsLogin(true) // 최대 세션 초과 시 새로운 로그인 차단
				.expiredUrl("/login?expired=true") // 세션 만료 시 리다이렉트 URL
			)
			
			// 시큐리티3 에서는 세션 고정 보호가 기본으로 활성화 라고 한다. 그래도 어떻게 생겼는지 봐두자
			.securityContext(securityContext -> securityContext
				.requireExplicitSave(true)); // 비활성화는 당연히 false 로
		
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@PostConstruct
	public void init() {
		log.info("SecurityConfig 초기화 - 핸들러 주입 확인");
		log.info("SuccessHandler: {}", authenticationSuccessHandler != null);
		log.info("FailureHandler: {}", authenticationFailureHandler != null);
		log.info("LogoutHandler: {}", logoutHandler != null);
	}
}
