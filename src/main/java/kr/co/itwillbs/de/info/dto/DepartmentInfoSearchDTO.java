package kr.co.itwillbs.de.info.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DepartmentInfoSearchDTO {

	private String type; // 검색 유형
	
	private String departmentCode; // 대표부서코드
	private String childCode; // 하위부서코드
	private String locationIdx; // 장소참조
	
	@Builder
    public DepartmentInfoSearchDTO(String type, String departmentCode, String childCode, String locationIdx) {
        this.type = type;
        this.departmentCode = departmentCode;
        this.childCode = childCode;
        this.locationIdx = locationIdx;
    }
}
