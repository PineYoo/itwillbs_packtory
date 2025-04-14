package kr.co.itwillbs.de.mes.dto;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.ToString;


@Entity
@EntityListeners(AuditingEntityListener.class)
@ToString
@Table(name = "T_DEMO")
public class DemoVO /*implements Serializable*/ {
	/**
	 * VO 변하지 않는 데이터 객체
	 * 오직 Read만 가능해야 함(Getter)
	 */
	
	//private static final long serialVersionUID = 4242403764505642874L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idx;
	
	private String id;
	
	private String password;
	
	private String name;
	
	private String isDeleted;
	
	private String role;
	
	private String regId;
	
	@CreatedDate
	private LocalDateTime regDate;
	
	private String modId;
	
	@LastModifiedDate
	private LocalDateTime modDate;

	private Long readCnt;
	
	// Getter
	public Long getIdx() {
		return idx;
	}

	public String getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public String getRole() {
		return role;
	}

	public String getRegId() {
		return regId;
	}

	public LocalDateTime getRegDate() {
		return regDate;
	}

	public String getModId() {
		return modId;
	}

	public LocalDateTime getModDate() {
		return modDate;
	}
	
	public Long getReadCnt() {
		return readCnt;
	}
	
	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public void setRegDate(LocalDateTime regDate) {
		this.regDate = regDate;
	}

	public void setModId(String modId) {
		this.modId = modId;
	}

	public void setModDate(LocalDateTime modDate) {
		this.modDate = modDate;
	}

	public void setReadCnt(Long readCnt) {
		this.readCnt = readCnt;
	}
	// @NoArgsConstructor
	public DemoVO() {
		
	}
	// @AllArgsConstructor
	@Builder
	public DemoVO(String id, String password, String name, String isDeleted, String role, String regId, String modId) {
		this.id = id;
		this.password = password;
		this.name = name;
		this.isDeleted = isDeleted;
		this.role = role;
		this.regId = regId;
		this.modId = modId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, idx, isDeleted, modDate, modId, name, password, regDate, regId, role);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DemoVO other = (DemoVO) obj;
		return Objects.equals(id, other.id) && Objects.equals(idx, other.idx)
				&& Objects.equals(isDeleted, other.isDeleted) && Objects.equals(modDate, other.modDate)
				&& Objects.equals(modId, other.modId) && Objects.equals(name, other.name)
				&& Objects.equals(password, other.password) && Objects.equals(regDate, other.regDate)
				&& Objects.equals(regId, other.regId) && Objects.equals(role, other.role);
	}
	
	// JPA - update
	public void change(String id, String password, String name, String isDeleted, String role, String regId, String modId) {
		this.id = id;
		this.password = password;
		this.name = name;
		this.isDeleted = isDeleted;
		this.role = role;
		this.regId = regId;
		this.modId = modId;
	}
	
	public void delete(String isDeleted) {
		this.isDeleted = isDeleted;
	}
	
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
