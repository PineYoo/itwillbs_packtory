package kr.co.itwillbs.de.common.util;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * <pre>
 * 각자 클래스에서 사용법은 기존과 크게 달라지지 않음
 * 여전히 Controller, Service 등에서 @Slf4j를 붙여놓고
 * 아래의 LogUtil.logStart, LogUtil.logEnd, LogUtil.logDetail 를 사용하면 됨
 * 스태틱 메서드이기에 클래스 생성자에 유틸 클래스 주입 없이 바로 사용 가능함
 * </pre>
 */
@Component
public class LogUtil {

	// 클래스 앞에 @Slf4j lombok으로 선언하면 아래의 한줄이 생성된다. 오랜만에 보넹
	// private static final Logger log = LoggerFactory.getLogger(LogUtil.class);
	// import org.slf4j.LoggerFactory; 를 임포트 해야 한다.

	/**
	 * 기본값격인 로깅 유무 ---start, ---end
	 * <br>true: 로깅함, false: 로깅하지 않음
	 */
	public static boolean DEFAULTED_LOGGING_ENABLED;
	
	/**
	 * 디테일 로깅 유무 db나 request, api 등에서 전달 받은 데이터 확인용 로깅
	 * <br>true: 로깅함, false: 로깅하지 않음
	 */
	public static boolean DETAILED_LOGGING_ENABLED;
	
	/**
	 * dev: info, prod: trace 
	 */
	public static String LOG_LEVEL;

	@Value("${logging.detailed.enabled:false}")
	public boolean defaultedLoggingEnabled;
	
	@Value("${logging.detailed.enabled:false}")
	public boolean detailedLoggingEnabled;
	
	@Value("${logging.detailed.level:INFO}")
	public String logLevel;
	
	@PostConstruct
	public void init() {
		// @Value로 프로퍼티 값을 주입할 때 static 일 경우 실행 순서의 문제로 값이 들어가지 않는다.(기억이 잘...)
		// @PostConstruct 은 bean이 초기화 됨과 그 후 의존성 주입이 가능, bean lifecycle에서 오직 한 번만 수행된다는 것을 보장까지 함
		// 그래서 FileUtil에서는 어디서 찾은 방법으로 setter에 @Value를 사용해서 썼는데. @PostConstruct.. 뭐 둘다 어떤 의미상 굉장히 같네?? (신기함이 줄었다)
		DEFAULTED_LOGGING_ENABLED = defaultedLoggingEnabled;
		DETAILED_LOGGING_ENABLED = detailedLoggingEnabled;
		LOG_LEVEL = logLevel;
	}

	/**
	 * LogUtil.logStart
	 * <br>DEFAULTED_LOGGING_ENABLED이 true일 때만 로그를 남긴다.
	 * @param logger
	 */
	public static void logStart(Logger logger) {
		if(!DEFAULTED_LOGGING_ENABLED) {return;}
		String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		logByConfiguredLevel(logger,"{}---start", methodName);
	}

	/**
	 * LogUtil.logEnd
	 * <br>DEFAULTED_LOGGING_ENABLED이 true일 때만 로그를 남긴다.
	 * @param logger
	 */
	public static void logEnd(Logger logger) {
		if(DEFAULTED_LOGGING_ENABLED) {return;}
			
		String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		logByConfiguredLevel(logger, "{}---end", methodName);
	}

	/**
	 * 사용예) LogUtil.logDetail(log, "requestDTO : {}", fooDTO)
	 * 
	 * @param logger  @Slf4j lombok으로 생성한 log를 주입
	 * @param message
	 * @param args
	 */
	public static void logDetail(Logger logger, String message, Object... args) {
		if(!DETAILED_LOGGING_ENABLED) {return;}
		
		logByConfiguredLevel(logger,message, args);
	}

	private static void logByConfiguredLevel(Logger logger, String message, Object... args) {
		switch(LOG_LEVEL.toUpperCase()) {
		case "TRACE":
			logger.trace(message, args);
			break;
		case "DEBUG":
			logger.debug(message, args);
			break;
		case "INFO":
		default:
			logger.info(message, args);
			break;
		}
	}
}
