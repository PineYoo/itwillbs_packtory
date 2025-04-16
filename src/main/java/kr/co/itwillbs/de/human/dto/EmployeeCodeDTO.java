package kr.co.itwillbs.de.human.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EmployeeCodeDTO {
    private String code; 	// 코드
    private String name; 	// 코드명(또는 이름)
}
