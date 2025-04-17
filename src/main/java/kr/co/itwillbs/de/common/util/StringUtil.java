package kr.co.itwillbs.de.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringUtil {

	/**
	 * Object 안에 null이 아닌 필드 값 출력
	 * @param obj
	 * @return StringBuffer.toString()
	 */
	public static String objToString(Object obj) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		if (obj == null) {
			return "Object is null";
		}

		Class<?> cls = obj.getClass(); // static 메서드에서는 this 대신 전달된 객체 사용
		Method[] arrMethod = cls.getMethods();
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		try {
			for (Method m : arrMethod) {
				if (m.getName().startsWith("get") && !m.getName().equals("getClass") && m.invoke(obj) != null && !"".equals(m.invoke(obj))) {
					sb.append(m.getName());
					sb.append(" : ");
					sb.append(m.invoke(obj));
					sb.append("\n");
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	/**
	 * Object 안에 null이 아닌 필드 값 출력
	 * @param obj
	 * @return StringBuffer.toString()
	 */
	@Deprecated
	public String old_objToString(Object obj) {
		log.debug("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		Class<?> cls = this.getClass();
		Method[] arrMethod = cls.getMethods();
		StringBuffer sb = new StringBuffer(/*this.getClass().toString() 유틸에서는 여기 클래스가 찍히니 패스!*/);
		sb.append("\n");
		try {
			for (Method m : arrMethod) {
				if(m.getName().startsWith("get") && !m.getName().equals("getClass") && m.invoke(this) != null) {
					sb.append(m.getName());
					sb.append(" : ");
					sb.append(m.invoke(this));
					sb.append("\n ");
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
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
		log.debug("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			Long.parseLong(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
}
