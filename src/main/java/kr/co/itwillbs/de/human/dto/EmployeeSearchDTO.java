package kr.co.itwillbs.de.human.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EmployeeSearchDTO {
    private String type;       // 검색 유형 (name, dept 등)
    private String searchValue; // 검색어
}
