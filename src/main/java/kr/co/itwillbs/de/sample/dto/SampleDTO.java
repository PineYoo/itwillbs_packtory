package kr.co.itwillbs.de.sample.dto;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

import kr.co.itwillbs.de.sample.constant.IsDeleted;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SampleDTO {
	/**
	 * T_DEMO 테이블에 테스트하기 위한 객체
	 */
	private Long idx;
	
	private String id;
	
	private String password;
	
	private String name;
	
	private IsDeleted isDeleted;
	
	private String role;
	
	private String regId;
	
	private LocalDateTime regDate;
	
	private String modId;
	
	private LocalDateTime modDate;

	private Long readCnt;
	
	public String toStr() {
		Class<?> cls = this.getClass();
		Method[] arrMethod = cls.getMethods();
		StringBuffer sb = new StringBuffer(this.getClass().toString());
		sb.append("\n");
		try {
			for (Method m : arrMethod) {
				if (m.getName().startsWith("get") && !m.getName().equals("getClass") && m.invoke(this) != null) {
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
}
