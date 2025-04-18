package kr.co.itwillbs.de.common.config;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc // 역시 Java config 를 하니 application.yml 에 
public class WebMvcConfiguration implements WebMvcConfigurer{

	@Override 
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		/* '/js/**'로 호출하는 자원은 '/static/js/' 폴더 아래에서 찾는다. */ 
		registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");//.setCachePeriod(60 * 60 * 24 * 365); 
		/* '/css/**'로 호출하는 자원은 '/static/css/' 폴더 아래에서 찾는다. */ 
		registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");//.setCachePeriod(60 * 60 * 24 * 365); 
		/* '/img/**'로 호출하는 자원은 '/static/img/' 폴더 아래에서 찾는다. */ 
		registry.addResourceHandler("/img/**").addResourceLocations("classpath:/static/img/");//.setCachePeriod(60 * 60 * 24 * 365); 
		/* '/font/**'로 호출하는 자원은 '/static/font/' 폴더 아래에서 찾는다. 현 프로젝트에서 font는 /css/font에 존재.. */ 
		//registry.addResourceHandler("/font/**").addResourceLocations("classpath:/static/font/").setCachePeriod(60 * 60 * 24 * 365); 
	}
	
//	@Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/*")
//                .allowedOrigins("http://localhost:8080") // 프론트엔드 주소
//                .allowedMethods("GET", "POST", "PUT", "DELETE")
//                .allowCredentials(true);
//    }
	/**
	 * 이 설정으로 PUT과 DELETE 는 전송이 가능해졌다. 근데 얘가 뭐하는건지는 시간이 필요 할듯.
	 * 출처 : https://stackoverflow.com/questions/34048617/spring-boot-how-to-use-hiddenhttpmethodfilter
	 * 여기도 참고해보자 : https://goldswan.tistory.com/42
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public FilterRegistrationBean hiddenHttpMethodFilter() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new HiddenHttpMethodFilter());
		filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
		return filterRegistrationBean;
	}
}
