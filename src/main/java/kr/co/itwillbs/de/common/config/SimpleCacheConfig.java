package kr.co.itwillbs.de.common.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
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

	
	/* 삽질하면서 이거까지 만들었는데... 
	 * 사실 만들 필요가 있었나 싶다. 만들지 않아도 잘 돌아간다던데...
	 * 그냥 명시적으로 우리는 어떤캐시를 쓰고 있다? 정도로 해두자!
	 */
	@Bean
	CacheManager cacheManager() {
		return new ConcurrentMapCacheManager(
				"majorCodes",
				"codeItems",
				"menus",
				"menuByUri"
				);
	}
/* 25.04.23 이전 버전
	public CacheManager cacheManager() {
		SimpleCacheManager cacheManager = new SimpleCacheManager();

		List<Cache> caches = new ArrayList<>();
		caches.add(new ConcurrentMapCache("majorCodes"));
		caches.add(new ConcurrentMapCache("codeItems"));

		cacheManager.setCaches(caches);
		return cacheManager;
	}
*/	
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
