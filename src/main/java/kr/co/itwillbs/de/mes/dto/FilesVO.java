package kr.co.itwillbs.de.mes.dto;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FilesVO {
	/**
	 * T_FILES 테이블의 객체
	 */
	
	private Long idx;
	
	private String majorType;
	
	private String majorId;
	
	private String filePath;
	
	private String fileName;
	
	private String rank;
	
	private String isDeleted;
	
	private String regId;
	
	private LocalDateTime regDate;
	
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

	/**
	 * 파일 저장 후 리턴에 사용 됨
	 * @param filePath
	 * @param fileName
	 */
	public FilesVO(String filePath, String fileName) {
		this.filePath = filePath;
		this.fileName = fileName;
	}
}
