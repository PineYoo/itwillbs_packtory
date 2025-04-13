package kr.co.itwillbs.de.admin.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminMainController {

	private final CacheManager cacheManager;
	
	public AdminMainController(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	
	@GetMapping("/caches")
	@ResponseBody
	public Map<String, Object> getCacheInfo() {
		Map<String, Object> result = new LinkedHashMap<>();

		for (String name : cacheManager.getCacheNames()) {
			Cache cache = cacheManager.getCache(name);
			Map<String, Object> cacheDetails = new HashMap<>();
			cacheDetails.put("name", name);

			if (cache != null && cache.getNativeCache() instanceof ConcurrentMap) {
				ConcurrentMap<?, ?> nativeMap = (ConcurrentMap<?, ?>) cache.getNativeCache();
				cacheDetails.put("size", nativeMap.size());
//				cacheDetails.put("keys", nativeMap.keySet()); // nativeMap.keySet() 안에 SimpleKey.EMPTY 인 spring 내부 객체 포함으로 Jackson은 이걸 판단할 수 없었음
				Set<?> keys = nativeMap.keySet();
				List<String> stringKeys = keys.stream()
						.map(Object::toString) // 그래서 전부 스트링으로! 또는 custom formatting
						.collect(Collectors.toList());
				cacheDetails.put("keys", stringKeys);
				result.put(name, cacheDetails);
			}
		}
		return result;
	}
	
	
	/**
	 * 
	 * @return
	 */
	@GetMapping(value={"","/"})
	public String mainView(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return "admin/view";
	}
	
	@GetMapping(value={"/dashboard","/dashboard/"})
	public String dashBoardView(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return "admin/dashBoard";
	}
	
	
	
}
