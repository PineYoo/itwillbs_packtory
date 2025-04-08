package kr.co.itwillbs.de.common.aop;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.itwillbs.de.admin.dto.LogDTO;
import kr.co.itwillbs.de.admin.mapper.LogMapper;
import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.common.aop.annotation.RequiredSessionIds;
import kr.co.itwillbs.de.common.util.ServletRequestUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class PacktoryAspect {
	/**
	 * <pre>
	 * 프로젝트 AOP 선언
	 * 1. 서비스에 들어와서 실행하고 나서 DB에 로그를 남기기 위함
	 * 2. 컨트롤러에 들어와 서비스를 지나고 다시 컨트롤러에 들어와서 리턴하기전까지 start log와 finish log를 자동으로 남기기 위함
	 * 3. 서비스에서 사용되는 DTO 객체에 regId, modId가 있을 경우 로그인을 하였으니 Session에서 memberId를 꺼내 입력하기 위함
	 * 일단은 이정도만 해보자!
	 * </pre>
	 */

	private final LogMapper logMapper;
	private final HttpServletRequest request;
	private final ServletRequestUtil servletRequestUtil;

	public PacktoryAspect(LogMapper logMapper, HttpServletRequest request, ServletRequestUtil servletRequestUtil) {
		this.logMapper = logMapper;
		this.request = request;
		this.servletRequestUtil = servletRequestUtil;
	}

	/**
	 * <pre>
	 * 사용법: 로그를 남길 서비스에 @LogExecution 어노테이션을 기입하면 로그 처리가 가능해진다.
	 * 1) 실행 시점: 타겟 메서드가 정상적으로 완료된 후 실행
	 * 2) 실행 조건: 메서드가 예외 없이 정상적으로 실행되어야만 실행
	 * </pre>
	 * 
	 * @param joinPoint
	 * @param logExecution
	 * @param result
	 */
	@AfterReturning(pointcut = "@annotation(logExecution)", returning = "result")
	public void logServiceMethod(JoinPoint joinPoint, LogExecution logExecution, Object result) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			/*
			 * // 메서드 정보 String serviceName =
			 * joinPoint.getTarget().getClass().getSimpleName(); String methodName =
			 * joinPoint.getSignature().getName(); String arguments =
			 * Arrays.toString(joinPoint.getArgs());
			 * 
			 * // contextPath를 포함한 경로 추출 String servletPath = request.getServletPath();
			 * String pathInfo = request.getPathInfo();
			 * 
			 * log.info("servletPath is {}, pathInfo is {}, getRequestURI() is {}",
			 * servletPath, pathInfo, request.getRequestURI()); String url = (servletPath !=
			 * null ? servletPath : "/") + (pathInfo != null ? pathInfo : ""); log.
			 * info("serviceName is {}, methodName is {}, arguments is {}, memberId is {}",
			 * serviceName, methodName, arguments, memberId);
			 */
			// 세션 정보
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String memberId = authentication != null ? authentication.getName() : null;
			String arguments = Arrays.toString(joinPoint.getArgs());

			// 로그 DTO 생성
			LogDTO logDTO = LogDTO.builder().accessId(memberId).accessType(servletRequestUtil.getAccessType(request))
					.parameters(arguments).accessDevice(servletRequestUtil.getDeviceType(request))
					.ip(servletRequestUtil.getClientIp(request)).url(request.getRequestURI()).build();
			/*
			 * .accessId(memberId) .accessType(LOG_ACCESS_TYPE_LOGIN)
			 * .accessDevice(servletRequestUtil.getDeviceType(request))
			 * .ip(servletRequestUtil.getClientIp(request)) .url("/login")
			 * .parameters("loginSuccess") .build();
			 */
			// DB에 로그 저장
			logMapper.registerLog(logDTO);

		} catch (Exception e) {
			log.error("서비스 로그 저장 중 오류 발생: {}", e.getMessage());
		}
	}

	
	/**
	 * <pre>
	 * 세션에 있는 memberId를 DTO의 여러 필드에 주입
	 * 포인트컷 설명 : packtory 서비스클래스.메서드 대상임
	 * (memberId인 이유는 시큐리티 로그인할때 username이 memberId이기 때문)
	 * DTO 객체에 여러 필드명 지정, @RequiredSessionIds(fields = {"memberId", "regId", "modId"})
	 * 하지만 사용에 주의 점이 한가지 있는데
	 * 메서드에 진입한 DTO나 DTOList가 바로 insert 작업을 하면 문제가 없지만
	 * 조회를 한 후에 입력을 하려하면 AOP로 주입한 객체를 조회하면서 새로 써버렸기에 아무일도 없는 것 처럼 되어버림
	 * </pre>
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("execution(* kr.co.itwillbs.de..service.*.*(..))")
	public Object injectSessionIds(ProceedingJoinPoint joinPoint) throws Throwable {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		Object[] args = joinPoint.getArgs();
		
		for(Object arg : args) {
			if (arg == null)
				continue;

			if (arg instanceof List<?>) {
				for (Object item : (List<?>) arg) {
					injectIfAnnotated(item);
				}
			} else {
				injectIfAnnotated(arg);
			}
		} // end of for(Object arg : args) {
		
		return joinPoint.proceed();
	}
	
	private void injectIfAnnotated(Object dto) {
		if (dto.getClass().isAnnotationPresent(RequiredSessionIds.class)) {
			injectMemberIds(dto);
		}
	}
	
	/**
	 * 위의 injectSessionIdToList 내부 코드
	 * @param dto
	 */
	private void injectMemberIds(Object dto) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String memberId = authentication != null ? authentication.getName() : null;
			log.info("{}---start sessionId is {}", Thread.currentThread().getStackTrace()[1].getMethodName(), memberId);

			if (memberId != null) {
				RequiredSessionIds annotation = dto.getClass().getAnnotation(RequiredSessionIds.class);
				String[] fields = annotation.fields();
				
				log.info("inject target fields: {}", Arrays.toString(fields));
				for (String fieldName : fields) {
					try {
						Field field = dto.getClass().getDeclaredField(fieldName);
						field.setAccessible(true);
						
						// 타입 체크
						if (!field.getType().equals(String.class)) {
							log.error("{} 필드는 String 타입이어야 합니다. 현재 타입: {}", fieldName, field.getType().getName());
							continue;
						}
						
						// 현재 값 확인
						Object currentValue = field.get(dto);
						log.info("{} 필드 현재 값: {}", fieldName, currentValue);

						// null인 경우에만 주입
						if (currentValue == null) {
							field.set(dto, memberId);
							log.info("{} 필드에 memberId 주입 성공: {}", fieldName, memberId);
						}
						
					} catch (NoSuchFieldException e) {
						log.error("DTO에 {} 필드가 없습니다: {}", fieldName, dto.getClass().getName());
					} catch (IllegalAccessException e) {
						log.error("{} 필드 접근 중 오류 발생: {}", fieldName, e.getMessage());
					}
				}
			}
		} catch (Exception e) {
			log.error("memberId 주입 중 오류 발생: {}", e.getMessage());
		}
	}
	// ===실험실0====================================================================================================================

	@AfterThrowing(pointcut = "@annotation(logExecution)", throwing = "ex")
	public void logException(JoinPoint joinPoint, LogExecution logExecution, Exception ex) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			// 1. 메서드 정보 수집
			String serviceName = joinPoint.getTarget().getClass().getSimpleName();
			String methodName = joinPoint.getSignature().getName();

			// 2. 요청 정보 수집
			String path = request.getRequestURI().substring(1);
			String memberId = SecurityContextHolder.getContext().getAuthentication().getName();

			// 3. 예외 정보 수집
			String exceptionMessage = ex.getMessage();
			String exceptionType = ex.getClass().getSimpleName();

			// 4. 로그 DTO 생성
			/*
			 * LogDTO logDTO =
			 * LogDTO.builder().serviceName(serviceName).methodName(methodName)
			 * .memberId(memberId).requestUrl(path).executionTime(LocalDateTime.now()).
			 * status("FAIL") // 실패 상태 추가 .exceptionType(exceptionType) // 예외 타입 추가
			 * .exceptionMessage(exceptionMessage) // 예외 메시지 추가 .build();
			 */
			// 5. DB에 로그 저장
			// logMapper.registerLog(logDTO);

		} catch (Exception e) {
			log.error("예외 로그 저장 실패: {}", e.getMessage());
		}
	}

	// ===실험실1====================================================================================================================

	// 메서드 실행 시간 측정 성능 측정을 위함?
	@Around("@annotation(LogExecutionTime)")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		long startTime = System.currentTimeMillis();

		Object result = joinPoint.proceed();

		long executionTime = System.currentTimeMillis() - startTime;

		log.info("{} executed in {}ms", joinPoint.getSignature().toShortString(), executionTime);

		return result;
	}

	// ===실험실2====================================================================================================================

	// SLF4J 로깅을 위한 컨트롤러/서비스 메서드 로깅
	// @Around("execution(* kr.co.itwillbs.de..controller.*.*(..)) || execution(*
	// kr.co.itwillbs.de..service.*.*(..))")
	public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		String className = joinPoint.getTarget().getClass().getSimpleName();
		String methodName = joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();

		// 메서드 시작 로그
		log.info("=== {}.{}({}) 시작 ===", className, methodName, Arrays.toString(args));

		try {
			Object result = joinPoint.proceed();

			// 메서드 종료 로그
			log.info("=== {}.{}({}) 종료 - 결과: {} ===", className, methodName, Arrays.toString(args), result);

			return result;
		} catch (Exception e) {
			// 예외 발생 시 로그
			log.error("=== {}.{}({}) 예외 발생: {} ===", className, methodName, Arrays.toString(args), e.getMessage());
			throw e;
		}
	}
	
	// ===실험실3====================================================================================================================
	/**
	 * <pre>
	 * 세션에 있는 memberId를 DTO의 하나의 필드에 주입
	 * 포인트컷 설명 : packtory 서비스클래스.메서드 && 아규먼트 첫번째가 dto 객체여야한다. 여러개 쓰는 사람 있나???요???
	 * (memberId인 이유는 시큐리티 로그인할때 username이 memberId이기 때문)
	 * DTO 객체에 특정 필드명 지정, @RequiredSessionId(fieldName = "regId", onlyIfNull = true) Null 일경우 주입
	 * DTO 객체에 특정 필드명 지정, @RequiredSessionId(fieldName = "regId", onlyIfNull = false) 항상 주입
	 * </pre>
	 * 
	 * @param joinPoint
	 * @param dto
	 * @return
	 * @throws Throwable
	 */
/*	@Around("execution(* kr.co.itwillbs.de..service.*.*(..)) && args(dto,..) && @within(RequiredSessionId)")
	public Object injectSingleSessionId(ProceedingJoinPoint joinPoint, Object dto) throws Throwable {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		// 1. DTO가 @RequiredSessionId 어노테이션이 있는지 확인
		if (dto != null && dto.getClass().isAnnotationPresent(RequiredSessionId.class)) {
			RequiredSessionId annotation = dto.getClass().getAnnotation(RequiredSessionId.class);
			String fieldName = annotation.fieldName(); // 어노테이션에서 필드명 가져오기
			try {
				// 2. 세션에서 memberId 가져오기
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				String memberId = authentication != null ? authentication.getName() : null;

				if (memberId != null) {
					// 3. DTO의 memberId 필드에 값 주입
					Field memberIdField = dto.getClass().getDeclaredField(fieldName);
					memberIdField.setAccessible(true);
					
					// 필드 타입이 String이 아닌 경우 예외 발생
					if (!memberIdField.getType().equals(String.class)) {
						throw new IllegalArgumentException(fieldName + " 필드는 String 타입이어야 합니다.");
					}
					// 현재 값이 null인 경우에만 주입
					if (memberIdField.get(dto) == null) {
						memberIdField.set(dto, memberId);
					}
				}
			} catch (NoSuchFieldException e) {
				// memberId 필드가 없는 경우 무시
				log.debug("DTO에 memberId 필드가 없습니다: {}", dto.getClass().getName());
			} catch (IllegalAccessException e) {
				log.error("memberId 필드 접근 중 오류 발생: {}", e.getMessage());
			}
		}

		// 4. 원래 메서드 실행
		return joinPoint.proceed();
	}
	*/
}
