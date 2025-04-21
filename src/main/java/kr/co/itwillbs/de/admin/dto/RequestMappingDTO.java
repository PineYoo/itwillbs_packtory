package kr.co.itwillbs.de.admin.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RequestMappingDTO {

	private String uriPattern;
	private String description;
	private String method;
	private String methodName;
	private String simpleName;
	private String params;
	private List<String> paramList;
}
