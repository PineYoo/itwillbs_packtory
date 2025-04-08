package kr.co.itwillbs.de.human.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentCodeDTO {
	private String parentCode;  // 부모 코드
	private String childCode;   // 하위 부서 코드
	private String childName;   // 하위 부서 명
}
