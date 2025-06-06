package kr.co.itwillbs.de.admin.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LogDTO {

	// 테이블 PK
	private String idx;
	// 멤버 id (시큐리티에서 멤버 테이블에 관리자 등록 가능하면 같은 테이블을 보면 될듯)
	private String accessId;
	// 접속 타입(예시. 1:front, 2:admin, 3: partner)
	private String accessType;
	private String accessTypeName;
	// 접속 장치(예시. 1:PC, 2:Mobile)
	private String accessDevice;
	private String accessDeviceName;
	// HttpServletRequest.getRemoteAddr()?
	private String ip;
	// HttpServletRequest.parameterMap()?
	private String parameters;
	// HttpServletRequest.getRequestURI?L?
	private String url;
	// DB마다 now(), sysdate 값
	private LocalDateTime accessDate;
	
	// 뭔가 url보다 메소드 명이 좋을것 같기도?
	private String methodName;
	// 실행 시간 측정을 위한 변수 추가
	private LocalDateTime executionTime;
	
	// 페이징 변수들
	private int totalCount;
	private int rowAsc;
	private int rowNum;
	
	@Builder
	public LogDTO(String accessId, String accessType, String accessDevice, String ip, String parameters, String url,
			LocalDateTime accessDate) {
		this.accessId = accessId;
		this.accessType = accessType;
		this.accessDevice = accessDevice;
		this.ip = ip;
		this.parameters = parameters;
		this.url = url;
		this.accessDate = accessDate;
	}
	
}
