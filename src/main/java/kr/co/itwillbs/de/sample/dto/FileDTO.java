package kr.co.itwillbs.de.sample.dto;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FileDTO {
	/**
	 * T_FILES 테이블의 객체
	 */
	
	private MultipartFile[] multipartFiles;
	
	private Long idx;
	
	private String majorType;
	
	private String majorId;
	
	private String filePath;
	
	private String fileName;
	
	private String rank;
	
	private String isDeleted;
	
	private String regId;
	
	private LocalDateTime regDate;
}
