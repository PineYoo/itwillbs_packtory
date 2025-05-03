package kr.co.itwillbs.de.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringUtil {

	/**
	 * <pre>
	 * 우리 프로젝트 스프링부트로 전달받은 DTO 클래스 내부를 탐색하며
	 * 클래스 필드 변수들을 출력 (null 또는 "" 이 아닌 value가 있는 멤버들)
	 * 클래스 필드 안의 list는 위의 조건을 보지 않고 출력시킨다.(개선 필요)
	 * </pre>
	 * @param obj
	 * @return
	 */
	public static String objToString(Object obj) {
		if (obj == null) {
			return "Object is null";
		}

		StringBuilder sb = new StringBuilder("\n");

		if (obj instanceof List<?> list) {
			for (int i = 0; i < list.size(); i++) {
				sb.append("[").append(i).append("] ").append(objToString(list.get(i))).append("\n");
			}
		} else if (obj instanceof Map<?, ?> map) {
			for (Map.Entry<?, ?> entry : map.entrySet()) {
				sb.append("[").append(entry.getKey()).append("] => ").append(objToString(entry.getValue())).append("\n");
			}
		} else {
			Class<?> cls = obj.getClass();
			Method[] arrMethod = cls.getMethods();
			try {
				for (Method m : arrMethod) {
					if (m.getName().startsWith("get") 
							&& !m.getName().equals("getClass") 
							&& m.getParameterCount() == 0
							&& m.invoke(obj) != null
							&& !"".equals(m.invoke(obj))) {
						sb.append(m.getName()).append(" : ").append(m.invoke(obj)).append("\n");
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}
	
	/**
	 * String 값을 받아서
	 * <br>Long 값 체크
	 * TODO 25.03.17 이런거 더 만들어야 할까?
	 * @param str 문자열
	 * @return boolean -> true: 정수 값, false: 정수가 아닌 값
	 */
	public static boolean isLongValue(String str){
		LogUtil.logStart(log);
		try {
			Long.parseLong(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
}
