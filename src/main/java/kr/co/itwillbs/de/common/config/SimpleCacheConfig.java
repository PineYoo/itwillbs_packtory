package kr.co.itwillbs.de.common.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableCaching
public class SimpleCacheConfig {
	/**
	 * <pre>
	 * 특별한 설정 없이 기본 SimpleCacheManager 사용
	 * @EnableCaching은 스프링에서 캐시 기능을 사용할 수 있게 해주는 트리거
	 * 따로 CacheManager 빈을 정의하지 않으면 스프링부트는 SimpleCacheManager를 기본 제공하며, 내부적으로 ConcurrentMapCacheManager를 씀
	 * 이 경우 @Cacheable("캐시이름")에 적은 이름이 자동으로 생성
	 * </pre>
	 */
	
//	private final CacheManager cacheManager;
//	
//	public SimpleCacheConfig(CacheManager cacheManager) {
//		this.cacheManager = cacheManager;
//	}

	@Bean
	public CacheManager cacheManager() {
		SimpleCacheManager cacheManager = new SimpleCacheManager();

		List<Cache> caches = new ArrayList<>();
		caches.add(new ConcurrentMapCache("majorCodes"));
		caches.add(new ConcurrentMapCache("codeItems"));

		cacheManager.setCaches(caches);
		return cacheManager;
	}
	
//	@Override
//	public void run(String... args) throws Exception {
//		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
//		log.info("Using cache manager :" + this.cacheManager.getClass().getName());
//	}
/** 
 * <pre>
 * Description:
 * The dependencies of some of the beans in the application context form a cycle:
 * Bean 설정을 하자마자 순환 참조 오류가 발생함. 오우 댕신기?
 * ┌──->──┐
 * |  simpleCacheConfig defined in file [E:\workspace\Local_Git_Repository\packtory_\bin\main\kr\co\itwillbs\de\common\config\SimpleCacheConfig.class]
 * └──<-──┘
 * </pre>
 */
}
