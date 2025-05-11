package kr.co.itwillbs.de.common.config;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
//@EnableWebMvc // 이녀석도 Java config를 하니 application.yml 에 설정이 안먹힘. 
public class WebMvcConfiguration implements WebMvcConfigurer{
/**
 * 레거시 MVC에 dispatch-servlet에 설정하던 mvc 설정들 중
 * viewResolver 나
 * resources 에 대한 걸 하는 곳이라 추측됨
 */
	
	/**
	 * 와 진짜 무슨 말인지 하나도 모르겠다.
	 * addResourceHandler : 정적 리소스를 제공하는 리소스 핸들러를 추가, 핸들러를 호출하여 지정된 URL 경로 패턴 중 하나와 일치하는 요청을 처리함
	 * addResourceLocations : 정적 콘텐츠를 제공할 리소스 위치를 하나 이상 추가, 각 위치는 유효한 디렉터리를 가리켜야 함.
	 * 						여러 위치는 여러 위치를 쉼표로 구분된 목록으로 지정할 수 있으며,
	 * 						지정된 리소스에 대해 지정된 순서대로 지정된 리소스에 대해 지정된 순서대로 확인
	 * setCachePeriod : 리소스 핸들러가 제공하는 리소스에 대한 캐시 기간을 초 단위로 지정
	 * 					기본값은 다음을 수행하지 않는 것입니다. 캐시 헤더를 보내지 않고 마지막으로 수정된 타임스탬프만 사용하는 것입니다
	 * 					캐시 헤더를 보내려면 0으로 설정합니다. 를 보내려면 0으로 설정하고, 지정된 최대 유효 기간 값을 가진 캐시 헤더를 보내려면 양수(초)로 설정합니다
	 * 					@param 캐시 기간 리소스를 캐시할 시간(초)
	 * resourceChain : 사용할 리소스 리졸버 및 트랜스포머 체인을 구성함 (이게 chain은 필터아닌가?)
	 * 				이는 예를 들어 리소스 URL에 버전 전략을 적용하는 데 유용할 수 있음 (싸우까??)
	 * 				이 메서드가 호출되지 않으면 기본적으로 간단한 {@link PathResourceResolver}가 사용되어 URL 경로를 구성된 위치의 리소스에 대한 URL 경로를 일치시키기 위해 사용됨
	 * 				@param cacheResources 리소스 확인 결과를 캐시할지 여부
	 * 				프로덕션의 경우 “true”로 설정하는 것이 좋습니다(개발의 경우 “false”)
	 * 				연쇄 메서드 호출을 위해 동일한 {@link ResourceHandlerRegistration} 인스턴스를 반환합니다. (내가 졌다..)
	 * addResolver : 체인에 리소스 리졸버를 추가함, 추가할 리소스 리졸버를 @param 리졸버로 지정 @return 체인 메서드 호출을 위한 현재 인스턴스 (이건 또 뭔...)
	 */
	@Override 
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
			.addResourceHandler("/js/**") // '/js/**' 로 호출하는 자원 <script src="*.js"> or <script th:src="@{/*.js}">
			.addResourceLocations("classpath:/static/js/") //'classpath:/static/js/' 폴더 아래에서 찾음
			.setCachePeriod(0) // 캐시는 '초'단위 (60 * 60 * 24 * 365) 1년을 나타냄
			.resourceChain(true) // 사용할 
			; 
		registry
			.addResourceHandler("/css/**") // '/css/**'로 호출하는 자원 <link href="*.css"> or <link th:href="@{/*.css}">
			.addResourceLocations("classpath:/static/css/") //'classpath:/static/css/' 폴더 아래에서 찾음
			.setCachePeriod(0) // 캐시는 '초'단위
			.resourceChain(true) // 사용할
			;
			// 보통은 img가 아니라 image 인데.. 어쩌다보니 이렇게 됐음
		registry
			.addResourceHandler("/img/**") // '/img/**'로 호출하는 자원 <img src="*.ico"> or <img th:src="@{/*.png}">
			.addResourceLocations("classpath:/static/img/") //'classpath:/static/img/' 폴더 아래에서 찾음
			.setCachePeriod(0) // 캐시는 '초'단위
			.resourceChain(true) // 사용할
			;
		
		/* 지금 프로젝트에서 font는 /css/font에 존재.. */
//		registry
//			.addResourceHandler("/font/**") // '/font/**'로 호출하는 자원
//			.addResourceLocations("classpath:/static/font/") //'classpath:/static/font/' 폴더 아래에서 찾음
//			.setCachePeriod(0) // 캐시는 '초'단위
//			.resourceChain(true)
//			; 
		
		// 퍼블리셔가 존재 할 경우 node 나 특수한 폴더가 존재 할 수 있다. 위와 똑같이 아래에 추가하자
	}
	
	/**
	 * 이 설정으로 PUT과 DELETE 는 전송이 가능해졌다. 근데 얘가 뭐하는건지는 시간이 필요 할듯.
	 * 출처 : https://stackoverflow.com/questions/34048617/spring-boot-how-to-use-hiddenhttpmethodfilter
	 * 여기도 참고해보자 : https://goldswan.tistory.com/42
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	FilterRegistrationBean hiddenHttpMethodFilter() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new HiddenHttpMethodFilter());
		filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
		return filterRegistrationBean;
	}
}
