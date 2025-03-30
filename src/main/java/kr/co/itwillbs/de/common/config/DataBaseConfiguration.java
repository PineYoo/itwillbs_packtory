package kr.co.itwillbs.de.common.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration // 부트에 configuration 으로 작성 했을 때 application.properties||yml 설정 을 둘다 불러주는 경우와 아닌 경우가 있다는 것도 공부 필요
//@PropertySource("classpath:/application.yml")
@MapperScan(basePackages = {"kr.co.itwillbs.de.*.mapper"})
public class DataBaseConfiguration {

	private final ApplicationContext applicationContext; 
	// mapper.xml 위치를 Resource 객체로 가져오기 위함
	
	public DataBaseConfiguration(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	// ApplicationContext이 Bean을 관리하는 역할을 수행하고
	// SpringBootApplication이 실행될 때 @Configuration 어노테이션이 붙은 java파일을 설정정보로 등록한다.
	// 이 때 @Bean으로 등록된 메서드들을 기반으로 빈 목록을 생성한다.
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	HikariConfig hikariConfig() {
		//application.properties 파일에서 spring.datasource.hikari로 시작하는 설정 값들을 가져와 Hikari 설정 객체를 반환한다.
		return new HikariConfig();
	}

	@Bean
	DataSource dataSource() {
		// HikariDataSource를 사용하기 위해 Hikari 설정 객체를 생성자로 넣은 HikariDataSource 객체 반환
		return new HikariDataSource(hikariConfig());
	}
	
	@Bean
	SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		// SqlSessionFactory는 Mybatis-spring 때 처럼 SqlSessionFactoryBean을 통해 생성되는데
		// 각종 typeAliase(DTO), mapperLocation(xml위치), DB접속정보(datasource)를 property를 통해 추가한다.
		// 즉 setter를 통해 추가된다.
		SqlSessionFactoryBean session = new SqlSessionFactoryBean();
		session.setDataSource(dataSource);
		session.setConfigLocation(applicationContext.getResource("classpath:kr/co/itwillbs/de/mybatis/mybatis-config.xml"));
		session.setMapperLocations(applicationContext.getResources("classpath:kr/co/itwillbs/de/mybatis/mapper/**/*Mapper.xml"));
		//session.setTypeAliasesPackage("kr.co.itwillbs.*"); //config에서 선언하는거랑 이 방법에 대한 공부 필요 뭔지 모르겠음!
		return session.getObject();
	}
	
	@Bean
	SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		// SqlSessionTemplate은 SqlSession인터페이스 타입의 객체이며
		// 결국 SqlSessionFactory의 각종 설정 정보를 통해 만들어지므로 생성자로 넣게 된다.
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
